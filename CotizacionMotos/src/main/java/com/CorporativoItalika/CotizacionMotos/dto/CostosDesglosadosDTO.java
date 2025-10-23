package com.CorporativoItalika.CotizacionMotos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostosDesglosadosDTO {

    private BigDecimal precioSinIva;
    private BigDecimal iva;
    private BigDecimal precioConIva;
    private BigDecimal gastosAdministrativos;
    private BigDecimal costoSeguro;
    private BigDecimal costoAccesorios;
}
