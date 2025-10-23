package com.CorporativoItalika.CotizacionMotos.repository;

import com.CorporativoItalika.CotizacionMotos.dto.ClienteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CorporativoItalika.CotizacionMotos.entity.Cliente;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefono(String telefono);

    boolean existsByEmail(String email);
}
