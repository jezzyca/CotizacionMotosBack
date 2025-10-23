package com.CorporativoItalika.CotizacionMotos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloMotoDTO {

    private Long idModelo;
    private String nombre;
    private String marca;
    private Integer anio;
    private List<VersionMotoDTO> versiones;
}
