package com.CorporativoItalika.CotizacionMotos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccesorioSeleccionadoDTO {

    @NotNull(message = "El ID del accesorio es requerido")
    private Long idAccesorio;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
