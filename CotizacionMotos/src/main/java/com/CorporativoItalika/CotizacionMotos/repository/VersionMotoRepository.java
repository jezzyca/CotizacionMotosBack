package com.CorporativoItalika.CotizacionMotos.repository;

import com.CorporativoItalika.CotizacionMotos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CorporativoItalika.CotizacionMotos.entity.VersionMoto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VersionMotoRepository extends JpaRepository<VersionMoto, Long> {

    List<VersionMoto> findByActivoTrue();

    List<VersionMoto> findByModeloIdModelo(Long idModelo);

    @Query("SELECT v FROM VersionMoto v " +
            "JOIN FETCH v.modelo " +
            "WHERE v.activo = true")
    List<VersionMoto> findAllActiveWithModelo();

    @Query("SELECT v FROM VersionMoto v " +
            "JOIN FETCH v.modelo " +
            "WHERE v.idVersion = :id")
    Optional<VersionMoto> findByIdWithModelo(Long id);

}
