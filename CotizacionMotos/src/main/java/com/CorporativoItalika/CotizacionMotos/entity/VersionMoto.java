package com.CorporativoItalika.CotizacionMotos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "VERSION_MOTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_version")
    private Long idVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo", nullable = false)
    private ModeloMoto modelo;

    @Column(name = "nombre_version", length = 100, nullable = false)
    private String nombreVersion;

    @Column(name = "precio_base", precision = 12, scale = 2, nullable = false)
    private BigDecimal precioBase;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo;

}
