package com.CorporativoItalika.CotizacionMotos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Long idCliente;
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 200, message = "El nombre no debe exceder 200 caracteres")
    private String nombre;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 digitos")
    private String telefono;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "El email no debe exceder 100 caracteres")
    private String email;
}
