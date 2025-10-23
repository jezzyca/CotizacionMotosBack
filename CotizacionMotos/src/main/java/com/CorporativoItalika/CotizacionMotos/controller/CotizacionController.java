package com.CorporativoItalika.CotizacionMotos.controller;

import com.CorporativoItalika.CotizacionMotos.dto.*;
import com.CorporativoItalika.CotizacionMotos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cotizaciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed.origins}")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<ApiResponse<CotizacionResponseDTO>> crearCotizacion(
            @Valid @RequestBody CotizacionRequestDTO request
    ) {
        try {
            log.info("Solicitud de creación de cotización recibida");
            CotizacionResponseDTO cotizacion = cotizacionService.crearCotizacion(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(cotizacion, "Cotización creada exitosamente"));

        } catch (RuntimeException e) {
            log.error("Error al crear cotización: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear cotización", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CotizacionResponseDTO>> obtenerCotizacion(
            @PathVariable Long id
    ) {
        try {
            CotizacionResponseDTO cotizacion = cotizacionService.obtenerCotizacion(id);
            return ResponseEntity.ok(ApiResponse.success(cotizacion, "Cotización obtenida"));

        } catch (RuntimeException e) {
            log.error("Error al obtener cotización: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<List<CotizacionResponseDTO>>> obtenerCotizacionesPorCliente(
            @PathVariable Long idCliente
    ) {
        try {
            List<CotizacionResponseDTO> cotizaciones =
                    cotizacionService.obtenerCotizacionesPorCliente(idCliente);
            return ResponseEntity.ok(
                    ApiResponse.success(cotizaciones, "Cotizaciones obtenidas")
            );

        } catch (Exception e) {
            log.error("Error al obtener cotizaciones del cliente", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener cotizaciones"));
        }
    }
}

@RestController
@RequestMapping("/catalogos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed.origins}")
class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping("/modelos")
    public ResponseEntity<ApiResponse<List<ModeloMotoDTO>>> obtenerModelos() {
        try {
            List<ModeloMotoDTO> modelos = catalogoService.obtenerModelos();
            return ResponseEntity.ok(
                    ApiResponse.success(modelos, "Modelos obtenidos exitosamente")
            );
        } catch (Exception e) {
            log.error("Error al obtener modelos", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener modelos"));
        }
    }

    @GetMapping("/versiones")
    public ResponseEntity<ApiResponse<List<VersionMotoDTO>>> obtenerVersiones() {
        try {
            List<VersionMotoDTO> versiones = catalogoService.obtenerTodasVersiones();
            return ResponseEntity.ok(
                    ApiResponse.success(versiones, "Versiones obtenidas exitosamente")
            );
        } catch (Exception e) {
            log.error("Error al obtener versiones", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener versiones"));
        }
    }

    @GetMapping("/versiones/modelo/{idModelo}")
    public ResponseEntity<ApiResponse<List<VersionMotoDTO>>> obtenerVersionesPorModelo(
            @PathVariable Long idModelo
    ) {
        try {
            List<VersionMotoDTO> versiones =
                    catalogoService.obtenerVersionesPorModelo(idModelo);
            return ResponseEntity.ok(
                    ApiResponse.success(versiones, "Versiones obtenidas exitosamente")
            );
        } catch (Exception e) {
            log.error("Error al obtener versiones del modelo", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener versiones"));
        }
    }

    @GetMapping("/versiones/{id}")
    public ResponseEntity<ApiResponse<VersionMotoDTO>> obtenerVersion(
            @PathVariable Long id
    ) {
        try {
            VersionMotoDTO version = catalogoService.obtenerVersionPorId(id);
            return ResponseEntity.ok(
                    ApiResponse.success(version, "Versión obtenida exitosamente")
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/accesorios")
    public ResponseEntity<ApiResponse<List<AccesorioDTO>>> obtenerAccesorios() {
        try {
            List<AccesorioDTO> accesorios = catalogoService.obtenerAccesorios();
            return ResponseEntity.ok(
                    ApiResponse.success(accesorios, "Accesorios obtenidos exitosamente")
            );
        } catch (Exception e) {
            log.error("Error al obtener accesorios", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener accesorios"));
        }
    }
}

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
