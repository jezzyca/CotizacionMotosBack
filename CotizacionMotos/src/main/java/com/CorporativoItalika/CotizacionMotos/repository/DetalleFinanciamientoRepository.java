package com.CorporativoItalika.CotizacionMotos.repository;


import com.CorporativoItalika.CotizacionMotos.entity.DetalleFinanciamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DetalleFinanciamientoRepository extends JpaRepository<DetalleFinanciamiento, Long> {

    List<DetalleFinanciamiento> findByCotizacionIdCotizacion(Long idCotizacion);

    void deleteByCotizacionIdCotizacion(Long idCotizacion);
}
