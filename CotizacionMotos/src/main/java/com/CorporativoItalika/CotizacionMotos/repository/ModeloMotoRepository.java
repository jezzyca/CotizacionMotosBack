package com.CorporativoItalika.CotizacionMotos.repository;

import com.CorporativoItalika.CotizacionMotos.entity.ModeloMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModeloMotoRepository extends JpaRepository<ModeloMoto, Long> {

    List<ModeloMoto> findByActivoTrue();

    List<ModeloMoto> findByMarca(String marca);

    List<ModeloMoto> findByAnio(Integer anio);
}
