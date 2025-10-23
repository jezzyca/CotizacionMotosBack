package com.CorporativoItalika.CotizacionMotos.service;

import com.CorporativoItalika.CotizacionMotos.dto.*;
import com.CorporativoItalika.CotizacionMotos.entity.*;
import com.CorporativoItalika.CotizacionMotos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CotizacionService {
    private final CotizacionRepository cotizacionRepository;
    private final ClienteRepository clienteRepository;
    private final VersionMotoRepository versionMotoRepository;
    private final AccesorioRepository accesorioRepository;

    private static final BigDecimal TASA_IVA = new BigDecimal("0.16");
    private static final BigDecimal TASA_INTERES_ANUAL = new BigDecimal("12.5");
    private static final BigDecimal PORCENTAJE_MINIMO_ENGANCHE = new BigDecimal("10");
    private static final int[] PLAZOS_DISPONIBLES = {24, 36, 48, 60};

    @Transactional
    public CotizacionResponseDTO crearCotizacion(CotizacionRequestDTO request) {
        log.info("Creando cotización para versión: {}", request.getIdVersion());

        VersionMoto version = versionMotoRepository.findByIdWithModelo(request.getIdVersion())
                .orElseThrow(() -> new RuntimeException("Versión de moto no encontrada"));

        BigDecimal costoBase = version.getPrecioBase();
        BigDecimal porcentajeEnganche = request.getMontoEnganche()
                .multiply(new BigDecimal("100"))
                .divide(costoBase, 2, RoundingMode.HALF_UP);

        if(porcentajeEnganche.compareTo(PORCENTAJE_MINIMO_ENGANCHE) < 0) {
            throw new RuntimeException("El enganche debe ser al menos el 10% del valor de la moto. Mínimo: $"
                    + costoBase.multiply(PORCENTAJE_MINIMO_ENGANCHE).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
            );
        }

        // Crear o buscar cliente
        Cliente cliente = crearOActualizarCliente(request.getCliente());

        // Calcular costos
        BigDecimal precioSinIva = version.getPrecioBase();
        BigDecimal iva = precioSinIva.multiply(TASA_IVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal precioConIva = precioSinIva.add(iva);

        BigDecimal costoAccesorios = BigDecimal.ZERO;
        List<AccesorioCotizacion> accesoriosCotizacion = new ArrayList<>();

        if (request.getAccesorios() != null && !request.getAccesorios().isEmpty()) {
            for (AccesorioSeleccionadoDTO accDto : request.getAccesorios()) {
                Accesorio accesorio = accesorioRepository.findById(accDto.getIdAccesorio())
                        .orElseThrow(() -> new RuntimeException("Accesorio no encontrado: " + accDto.getIdAccesorio()));

                BigDecimal subtotal = accesorio.getPrecio()
                        .multiply(new BigDecimal(accDto.getCantidad()))
                        .setScale(2, RoundingMode.HALF_UP);

                costoAccesorios = costoAccesorios.add(subtotal);

                AccesorioCotizacion ac = new AccesorioCotizacion();
                ac.setAccesorio(accesorio);
                ac.setCantidad(accDto.getCantidad());
                ac.setPrecioUnitario(accesorio.getPrecio());
                ac.setSubtotal(subtotal);
                accesoriosCotizacion.add(ac);
            }
        }

        // Calcular costo total
        BigDecimal costoTotal = precioConIva
                .add(request.getGastosAdministrativos())
                .add(request.getCostoSeguro())
                .add(costoAccesorios);

        BigDecimal montoFinanciar = costoTotal.subtract(request.getMontoEnganche());

        // Crear cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setCliente(cliente);
        cotizacion.setVersion(version);
        cotizacion.setFechaCotizacion(LocalDateTime.now());
        cotizacion.setPrecioSinIva(precioSinIva);
        cotizacion.setIva(iva);
        cotizacion.setPrecioConIva(precioConIva);
        cotizacion.setGastosAdministrativos(request.getGastosAdministrativos());
        cotizacion.setCostoSeguro(request.getCostoSeguro());
        cotizacion.setCostoAccesoriosTotal(costoAccesorios);
        cotizacion.setCostoTotal(costoTotal);
        cotizacion.setMontoEnganche(request.getMontoEnganche());
        cotizacion.setPorcentajeEnganche(porcentajeEnganche);
        cotizacion.setMontoFinanciar(montoFinanciar);
        cotizacion.setEstatus("ACTIVA");

        for (AccesorioCotizacion ac : accesoriosCotizacion) {
            ac.setCotizacion(cotizacion);
        }
        cotizacion.setAccesorios(accesoriosCotizacion);


        List<DetalleFinanciamiento> detallesFinanciamiento = calcularFinanciamiento(
                cotizacion, montoFinanciar
        );
        cotizacion.setDetallesFinanciamiento(detallesFinanciamiento);

        // Guardar cotización
        Cotizacion cotizacionGuardada = cotizacionRepository.save(cotizacion);

        log.info("Cotización creada exitosamente con ID: {}", cotizacionGuardada.getIdCotizacion());

        return convertirADTO(cotizacionGuardada);
    }

    private List<DetalleFinanciamiento> calcularFinanciamiento(
            Cotizacion cotizacion, BigDecimal montoFinanciar
    ){
        List<DetalleFinanciamiento> detalles = new ArrayList<>();
        BigDecimal tasaMensual = TASA_INTERES_ANUAL
                .divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
                .divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP);

        for (int plazo : PLAZOS_DISPONIBLES) {
            BigDecimal unMasTasa = BigDecimal.ONE.add(tasaMensual);
            BigDecimal potencia = unMasTasa.pow(plazo);
            BigDecimal numerador = tasaMensual.multiply(potencia);
            BigDecimal denominador = potencia.subtract(BigDecimal.ONE);
            BigDecimal pagoMensual = montoFinanciar
                    .multiply(numerador)
                    .divide(denominador, 2, RoundingMode.HALF_UP);

            BigDecimal totalPagar = pagoMensual.multiply(new BigDecimal(plazo));
            BigDecimal totalIntereses = totalPagar.subtract(montoFinanciar);

            DetalleFinanciamiento detalle = new DetalleFinanciamiento();
            detalle.setCotizacion(cotizacion);
            detalle.setPlazoMeses(plazo);
            detalle.setTasaInteres(TASA_INTERES_ANUAL);
            detalle.setPagoMensual(pagoMensual);
            detalle.setTotalIntereses(totalIntereses);
            detalle.setTotalPagar(totalPagar);

            detalles.add(detalle);
        }

        return detalles;
    }

    private Cliente crearOActualizarCliente(ClienteDTO dto) {
        Cliente cliente;
        if (dto.getIdCliente() != null) {
            cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            cliente = clienteRepository.findByEmail(dto.getEmail())
                    .orElse(new Cliente());
        }

        cliente.setNombre(dto.getNombre());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public CotizacionResponseDTO obtenerCotizacion(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        return convertirADTO(cotizacion);
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponseDTO> obtenerCotizacionesPorCliente(Long idCliente) {
        List<Cotizacion> cotizaciones = cotizacionRepository.findByClienteIdCliente(idCliente);
        return cotizaciones.stream()
                .map(this::convertirADTO)
                .toList();
    }

    private CotizacionResponseDTO convertirADTO(Cotizacion cotizacion) {
        CotizacionResponseDTO dto = new CotizacionResponseDTO();
        dto.setIdCotizacion(cotizacion.getIdCotizacion());
        dto.setModelo(cotizacion.getVersion().getModelo().getNombre());
        dto.setVersion(cotizacion.getVersion().getNombreVersion());
        dto.setFechaCotizacion(cotizacion.getFechaCotizacion());

        // Costos
        CostosDesglosadosDTO costos = new CostosDesglosadosDTO();
        costos.setPrecioSinIva(cotizacion.getPrecioSinIva());
        costos.setIva(cotizacion.getIva());
        costos.setPrecioConIva(cotizacion.getPrecioConIva());
        costos.setGastosAdministrativos(cotizacion.getGastosAdministrativos());
        costos.setCostoSeguro(cotizacion.getCostoSeguro());
        costos.setCostoAccesorios(cotizacion.getCostoAccesoriosTotal());
        dto.setCostos(costos);

        dto.setCostoTotal(cotizacion.getCostoTotal());
        dto.setMontoEnganche(cotizacion.getMontoEnganche());
        dto.setPorcentajeEnganche(cotizacion.getPorcentajeEnganche());
        dto.setMontoFinanciar(cotizacion.getMontoFinanciar());

        // Opciones de financiamiento
        List<FinanciamientoDTO> financiamiento = cotizacion.getDetallesFinanciamiento().stream()
                .map(d -> new FinanciamientoDTO(
                        d.getPlazoMeses(),
                        d.getTasaInteres(),
                        d.getPagoMensual(),
                        d.getTotalIntereses(),
                        d.getTotalPagar()
                ))
                .toList();
        dto.setOpcionesFinanciamiento(financiamiento);

        // Cliente
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setIdCliente(cotizacion.getCliente().getIdCliente());
        clienteDTO.setNombre(cotizacion.getCliente().getNombre());
        clienteDTO.setTelefono(cotizacion.getCliente().getTelefono());
        clienteDTO.setEmail(cotizacion.getCliente().getEmail());
        dto.setCliente(clienteDTO);

        // Accesorios
        List<AccesorioDTO> accesorios = cotizacion.getAccesorios().stream()
                .map(a -> new AccesorioDTO(
                        a.getAccesorio().getIdAccesorio(),
                        a.getAccesorio().getNombre(),
                        a.getAccesorio().getDescripcion(),
                        a.getPrecioUnitario(),
                        a.getCantidad(),
                        a.getSubtotal()
                ))
                .toList();
        dto.setAccesorios(accesorios);

        return dto;
    }
}