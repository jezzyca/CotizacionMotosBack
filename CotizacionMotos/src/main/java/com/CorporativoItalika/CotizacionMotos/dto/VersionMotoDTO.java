package com.CorporativoItalika.CotizacionMotos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionMotoDTO {

    private Long idVersion;
    private String nombreVersion;
    private BigDecimal precioBase;
    private String descripcion;
    private String modeloNombre;
    private String marca;
    private Integer anio;
}
