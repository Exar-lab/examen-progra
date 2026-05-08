package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.application.port.out.ComprobanteRepositoryPort;
import com.buses.examen.Progra.sales.domain.Comprobante;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link ComprobanteRepositoryPort}.
 */
@Component
public class JpaComprobanteRepository implements ComprobanteRepositoryPort {

    private final SpringDataComprobanteRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaComprobanteRepository(final SpringDataComprobanteRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Comprobante save(final Comprobante comprobante) {
        return repository.save(comprobante);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Comprobante> findById(final Long id) {
        return repository.findById(id);
    }
}
