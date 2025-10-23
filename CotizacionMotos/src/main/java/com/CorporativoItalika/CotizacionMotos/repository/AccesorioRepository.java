package com.CorporativoItalika.CotizacionMotos.repository;

import com.CorporativoItalika.CotizacionMotos.entity.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {

    List<Accesorio> findByActivoTrue();
}
