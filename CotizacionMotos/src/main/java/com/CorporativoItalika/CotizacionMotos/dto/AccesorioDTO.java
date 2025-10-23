package com.CorporativoItalika.CotizacionMotos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccesorioDTO {

    private Long idAccesorio;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidad;
    private BigDecimal subtotal;
}
