package com.inca.mc_main.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse<T> {
    private boolean status;  // false si es error
    private int code;        // código HTTP
    private String message;  // mensaje de error
    private T data;          // detalles adicionales (puede ser null)
}