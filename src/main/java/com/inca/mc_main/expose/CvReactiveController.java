package com.inca.mc_main.expose;

import com.fasterxml.jackson.databind.JsonNode;
import com.inca.mc_main.business.*;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.dto.response.ArchivoProcesadoResponse;
import com.inca.mc_main.dto.response.ResultadoProcesamiento;
import com.inca.mc_main.dto.response.ResumenProcesamientoResponse;
import com.inca.mc_main.exception.ApiException;
import com.inca.mc_main.exception.response.BasicResponse;
import com.inca.mc_main.mapper.CvExtraccionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lector-cv")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CvReactiveController {

    private final RequerimientoService requerimientoService;
    private final OpenAiSkillExtractorService openAiSkillExtractorService;
    private final CvExtraccionService cvExtraccionService;
    private final PerfilProfesionalService perfilProfesionalService;
    private final SkillService skillService;

    @PostMapping(value = "/procesar-masivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<BasicResponse<ResumenProcesamientoResponse>>> procesarCVs(
            @RequestPart("idRequerimiento") String idRequerimiento,
            @RequestPart("idOrigen") String idOrigen,
            @RequestPart("files") List<FilePart> files) {

        return Flux.fromIterable(files)
                .flatMap(filePart -> DataBufferUtils.join(filePart.content())
                        .flatMap(buffer -> {
                            String nombreArchivo = filePart.filename();
                            try {
                                byte[] bytes = new byte[buffer.readableByteCount()];
                                buffer.read(bytes);
                                DataBufferUtils.release(buffer);

                                String textoPlano = extraerTexto(nombreArchivo, bytes);

                                return procesarCVTexto(textoPlano, idRequerimiento,idOrigen)
                                        .map(msg -> new ResultadoProcesamiento(nombreArchivo, true, msg))
                                        .onErrorResume(ex -> Mono.just(new ResultadoProcesamiento(nombreArchivo, false, ex.getMessage())));

                            } catch (Exception e) {
                                return Mono.just(new ResultadoProcesamiento(nombreArchivo, false, e.getMessage()));
                            }
                        })
                )
                .collectList()
                .map(resultados -> {
                    List<ArchivoProcesadoResponse> exitosos = resultados.stream()
                            .filter(ResultadoProcesamiento::exito)
                            .map(res -> new ArchivoProcesadoResponse(res.archivo(), res.mensaje()))
                            .toList();

                    List<ArchivoProcesadoResponse> fallidos = resultados.stream()
                            .filter(res -> !res.exito())
                            .map(res -> new ArchivoProcesadoResponse(res.archivo(), res.mensaje()))
                            .toList();

                    String mensajeFinal = String.format("Se procesaron correctamente %d archivo(s).", exitosos.size());
                    if (!fallidos.isEmpty()) {
                        mensajeFinal += String.format(" No se procesaron %d archivo(s).", fallidos.size());
                    }

                    ResumenProcesamientoResponse resumen = ResumenProcesamientoResponse.builder()
                            .mensaje(mensajeFinal)
                            .archivosProcesados(exitosos)
                            .archivosNoProcesados(fallidos)
                            .build();

                    return ResponseEntity.ok(
                            BasicResponse.<ResumenProcesamientoResponse>builder()
                                    .status(true)
                                    .code(200)
                                    .message("Procesamiento masivo completado")
                                    .data(resumen)
                                    .build()
                    );
                });
    }


    private String extraerTexto(String nombreArchivo, byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("El archivo " + nombreArchivo + " está vacío.");
        }

        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "pdf" -> {
                try (PDDocument doc = PDDocument.load(bytes)) {
                    return new PDFTextStripper().getText(doc);
                }
            }
            case "docx" -> {
                try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(bytes))) {
                    return doc.getParagraphs().stream()
                            .map(XWPFParagraph::getText)
                            .collect(Collectors.joining(" "));
                }
            }
            default -> throw new IllegalArgumentException("El archivo " + nombreArchivo + " tiene una extensión no soportada (" + extension + "). Solo se permiten archivos PDF o DOCX.");
        }
    }


    private Mono<String> procesarCVTexto(String textoPlano, String idRequerimiento,String idOrigen) {
        return openAiSkillExtractorService.extraerInformacionDesdeTextoCV(textoPlano)
                .doOnNext(json -> log.info("JSON extraído desde OpenAI:\n{}", json.toPrettyString()))
                .flatMap(json -> {
                    if (!esFormatoCVValido(json)) {
                        log.warn("El texto analizado no parece ser un CV. Proceso detenido.");
                        return Mono.error(new ApiException(false, 422, "El texto no tiene formato de Currículum Vitae (CV).", null));
                    }

                    CvExtraccionDTO dto = CvExtraccionMapper.convertirJsonNodeADTO(json);
                    dto.setIdRequerimiento(Integer.valueOf(idRequerimiento));
                    dto.setIdOrigenCV(Integer.valueOf(idOrigen));

                    log.info("Guardando escaneo de CV : {}", dto);

                    return cvExtraccionService.guardarExtraccion(dto)
                            .then(perfilProfesionalService.procesarPerfilesDesdeDTO(dto))
                            .then(skillService.procesarSkillsDesdeDTO(dto))
                            .thenReturn("Procesado correctamente CV: " + dto.getNombreCompleto());
                })
                .onErrorMap(ex -> {
                    log.error("Error procesando CV: {}", ex.getMessage(), ex);
                    return ex instanceof ApiException ? ex : new ApiException(false, 500, "Error interno procesando el CV.", null);
                });
    }

    private boolean esFormatoCVValido(JsonNode json) {
        return json.has("esFormatoCV") && json.get("esFormatoCV").asBoolean();
    }


}
