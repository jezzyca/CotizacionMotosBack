package com.CorporativoItalika.CotizacionMotos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CotizacionRequestDTO {

    @NotNull(message = "El ID de versión es requerido")
    private Long idVersion;

    @NotNull(message = "Los datos del cliente son requeridos")
    private ClienteDTO cliente;

    @NotNull(message = "El monto de enganche es requerido")
    @DecimalMin(value = "0.0", message = "El enganche debe ser positivo")
    private BigDecimal montoEnganche;

    @NotNull(message = "Los gastos administrativos son requeridos")
    @DecimalMin(value = "0.0", message = "los gastos debe ser positivos")
    private BigDecimal gastosAdministrativos;

    @NotNull(message = "El costo del seguro es requerido")
    @DecimalMin(value = "0.0", message = "El costo del seguro debe ser positivo")
    private BigDecimal costoSeguro;

    private List<AccesorioSeleccionadoDTO> accesorios;

    public Long getIdVersion() {
        return idVersion;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public BigDecimal getMontoEnganche() {
        return montoEnganche;
    }

    public BigDecimal getGastosAdministrativos() {
        return gastosAdministrativos;
    }

    public BigDecimal getCostoSeguro() {
        return costoSeguro;
    }

    public List<AccesorioSeleccionadoDTO> getAccesorios() {
        return accesorios;
    }
}
