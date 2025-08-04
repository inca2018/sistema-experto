package com.inca.mc_main.util;

import com.inca.mc_main.expose.CvReactiveController;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class Util {

    public static String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim().toLowerCase(); // normaliza para búsqueda
    }

    public static void tokenizar(Class<? extends CvReactiveController> react,String texto){
        try (
                InputStream tokenModelStream = react.getClass().getResourceAsStream("/models/es-token.bin");
                InputStream posModelStream = react.getClass().getResourceAsStream("/models/es-pos-maxent.bin")
        ) {
            if (tokenModelStream == null || posModelStream == null) {
                //return Mono.error(new IOException("Modelos lingüísticos no encontrados en la ruta /models/"));
                log.info(" Error cargando modelos lingüísticos");

            }

            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            Tokenizer tokenizer = new TokenizerME(tokenModel);
            String[] tokens = tokenizer.tokenize(texto);

            POSModel posModel = new POSModel(posModelStream);
            POSTaggerME tagger = new POSTaggerME(posModel);
            String[] tags = tagger.tag(tokens);

            List<String> palabrasFiltradas = new ArrayList<>();
            for (int i = 0; i < tokens.length; i++) {
                if (tags[i].startsWith("VERB") || tags[i].startsWith("PROPN")
                        || tags[i].startsWith("NOUN") || tags[i].startsWith("ADJ")) {
                    palabrasFiltradas.add(tokens[i]);
                }
            }

            //return Mono.just(palabrasFiltradas);

        } catch (IOException e) {
            log.error(" Error cargando modelos lingüísticos", e);
            //return Mono.error(e);
        }
    }
}
