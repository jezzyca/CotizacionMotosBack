package com.CorporativoItalika.CotizacionMotos.service;

import com.CorporativoItalika.CotizacionMotos.dto.*;
import com.CorporativoItalika.CotizacionMotos.repository.*;
import com.CorporativoItalika.CotizacionMotos.entity.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogoService {

    private final ModeloMotoRepository modeloRepository;
    private final VersionMotoRepository versionRepository;
    private final AccesorioRepository accesorioRepository;

    @Transactional(readOnly = true)
    public List<ModeloMotoDTO> obtenerModelos() {
        log.info("Obteniendo catálogo de modelos");

        List<ModeloMoto> modelos = modeloRepository.findByActivoTrue();

        return modelos.stream()
                .map(this::convertirModeloADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VersionMotoDTO> obtenerVersionesPorModelo(Long idModelo) {
        log.info("Obteniendo versiones para modelo: {}", idModelo);

        List<VersionMoto> versiones = versionRepository.findByModeloIdModelo(idModelo);

        return versiones.stream()
                .filter(v -> v.getActivo())
                .map(this::convertirVersionADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VersionMotoDTO> obtenerTodasVersiones() {
        log.info("Obteniendo todas las versiones disponibles");

        List<VersionMoto> versiones = versionRepository.findAllActiveWithModelo();

        return versiones.stream()
                .map(this::convertirVersionADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VersionMotoDTO obtenerVersionPorId(Long id) {
        log.info("Obteniendo versión con ID: {}", id);

        VersionMoto version = versionRepository.findByIdWithModelo(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada"));

        return convertirVersionADTO(version);
    }

    @Transactional(readOnly = true)
    public List<AccesorioDTO> obtenerAccesorios() {
        log.info("Obteniendo catálogo de accesorios");

        List<Accesorio> accesorios = accesorioRepository.findByActivoTrue();

        return accesorios.stream()
                .map(this::convertirAccesorioADTO)
                .collect(Collectors.toList());
    }

    private ModeloMotoDTO convertirModeloADTO(ModeloMoto modelo) {
        ModeloMotoDTO dto = new ModeloMotoDTO();
        dto.setIdModelo(modelo.getIdModelo());
        dto.setNombre(modelo.getNombre());
        dto.setMarca(modelo.getMarca());
        dto.setAnio(modelo.getAnio());
        return dto;
    }

    private VersionMotoDTO convertirVersionADTO(VersionMoto version) {
        VersionMotoDTO dto = new VersionMotoDTO();
        dto.setIdVersion(version.getIdVersion());
        dto.setNombreVersion(version.getNombreVersion());
        dto.setPrecioBase(version.getPrecioBase());
        dto.setDescripcion(version.getDescripcion());
        dto.setModeloNombre(version.getModelo().getNombre());
        dto.setMarca(version.getModelo().getMarca());
        dto.setAnio(version.getModelo().getAnio());
        return dto;
    }

    private AccesorioDTO convertirAccesorioADTO(Accesorio accesorio) {
        AccesorioDTO dto = new AccesorioDTO();
        dto.setIdAccesorio(accesorio.getIdAccesorio());
        dto.setNombre(accesorio.getNombre());
        dto.setDescripcion(accesorio.getDescripcion());
        dto.setPrecio(accesorio.getPrecio());
        dto.setCantidad(0);
        dto.setSubtotal(null);
        return dto;
    }
}