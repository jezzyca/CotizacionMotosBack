package com.CorporativoItalika.CotizacionMotos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MODELO_MOTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloMoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long idModelo;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "marca", length = 50, nullable = false)
    private String marca;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
