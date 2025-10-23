package com.CorporativoItalika.CotizacionMotos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CotizacionResponseDTO {

    private Long idCotizacion;
    private String modelo;
    private String version;
    private LocalDateTime fechaCotizacion;
    private CostosDesglosadosDTO costos;
    private BigDecimal costoTotal;
    private BigDecimal montoEnganche;
    private BigDecimal porcentajeEnganche;
    private BigDecimal montoFinanciar;
    private List<FinanciamientoDTO> opcionesFinanciamiento;
    private ClienteDTO cliente;
    private List<AccesorioDTO> accesorios;
}
