package com.CorporativoItalika.CotizacionMotos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanciamientoDTO {
    private Integer plazoMeses;
    private BigDecimal tasaInteres;
    private BigDecimal pagoMensual;
    private BigDecimal totalIntereses;
    private BigDecimal totalPagar;
}
