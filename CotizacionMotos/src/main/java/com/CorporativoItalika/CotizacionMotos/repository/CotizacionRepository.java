package com.CorporativoItalika.CotizacionMotos.repository;

import com.CorporativoItalika.CotizacionMotos.entity.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository  extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findByClienteIdCliente(Long idCliente);

    List<Cotizacion> findByEstatus(String estatus);

    @Query("SELECT c FROM Cotizacion c " +
           "JOIN FETCH c.cliente " +
           "JOIN FETCH c.version v " +
           "JOIN FETCH v.modelo " +
           "WHERE c.idCotizacion = :id")
    Optional<Cotizacion> findByIdWithDetails(Long id);
}
