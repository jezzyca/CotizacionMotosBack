package com.CorporativoItalika.CotizacionMotos.repository;


import com.CorporativoItalika.CotizacionMotos.entity.AccesorioCotizacion;
import com.CorporativoItalika.CotizacionMotos.entity.Cotizacion;
import com.CorporativoItalika.CotizacionMotos.entity.ModeloMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccesorioCotizacionRepository extends JpaRepository<AccesorioCotizacion, Long> {

    List<AccesorioCotizacion> findByCotizacionIdCotizacion(Long idCotizacion);
}

