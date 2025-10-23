package com.CorporativoItalika.CotizacionMotos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "COTIZACION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Long idCotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_version", nullable = false)
    private VersionMoto version;

    @Column(name = "fecha_cotizacion")
    private LocalDateTime fechaCotizacion;

    @Column(name = "precio_sin_iva", precision = 12, scale = 2)
    private BigDecimal precioSinIva;

    @Column(name = "iva", precision = 12, scale = 2)
    private BigDecimal iva;

    @Column(name = "precio_con_iva", precision = 12, scale = 2)
    private BigDecimal precioConIva;

    @Column(name = "gastos_administrativos", precision = 12, scale = 2)
    private BigDecimal gastosAdministrativos;

    @Column(name = "costo_seguro", precision = 12, scale = 2)
    private BigDecimal costoSeguro;

    @Column(name = "costo_accesorios_total", precision = 12, scale = 2)
    private BigDecimal costoAccesoriosTotal;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "monto_enganche", precision = 12, scale = 2)
    private BigDecimal montoEnganche;

    @Column(name = "porcentaje_enganche", precision = 12, scale = 2)
    private BigDecimal porcentajeEnganche;

    @Column(name = "monto_financiar", precision = 12, scale = 2)
    private BigDecimal montoFinanciar;

    @Column(name = "estatus", length = 20)
    private String estatus;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFinanciamiento> detallesFinanciamiento = new ArrayList<>();

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccesorioCotizacion> accesorios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if(fechaCotizacion == null){
            fechaCotizacion = LocalDateTime.now();
        }
        if(estatus == null){
            estatus = "ACTIVA";
        }
    }


}



