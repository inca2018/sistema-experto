package com.inca.mc_main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Paginacion {
    public  long totalElements;
    public int currentPage;
    public int pageSize;
    public  long totalPages;
}
