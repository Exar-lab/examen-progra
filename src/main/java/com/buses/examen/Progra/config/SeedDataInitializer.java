package com.buses.examen.Progra.config;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.EntityManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/** Siembra datos mínimos para ejecución inicial y pruebas. */
@Component
public class SeedDataInitializer implements ApplicationRunner {

    private final EntityManager entityManager;

    public SeedDataInitializer(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Siembra datos mínimos si la base de datos está vacía.
     *
     * @param args argumentos de arranque de la aplicación
     */
    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final long paisCount = entityManager.createQuery("select count(p) from Pais p", Long.class).getSingleResult();
        if (paisCount > 0) {
            return;
        }

        final Pais pais = new Pais("PE", "Peru");
        entityManager.persist(pais);
        final Ciudad lima = new Ciudad(pais, "Lima", "LIM");
        final Ciudad cusco = new Ciudad(pais, "Cusco", "CUS");
        entityManager.persist(lima);
        entityManager.persist(cusco);

        final Compania compania = new Compania("Buses Sur", "20123456789");
        entityManager.persist(compania);
        final Bus bus = new Bus(compania, "ABC-123", "Volvo 9700", 4);
        entityManager.persist(bus);

        entityManager.persist(new Asiento(bus, "1", 1, "REGULAR"));
        entityManager.persist(new Asiento(bus, "2", 1, "REGULAR"));
        entityManager.persist(new Asiento(bus, "3", 1, "REGULAR"));
        entityManager.persist(new Asiento(bus, "4", 1, "REGULAR"));

        final Ruta ruta = new Ruta(lima, cusco, 90, 120);
        entityManager.persist(ruta);
        entityManager.persist(new Servicio(
                ruta,
                bus,
                OffsetDateTime.now().plusDays(3),
                OffsetDateTime.now().plusDays(3).plusHours(2),
                50,
                EstadoServicio.PROGRAMADO,
                4));

        entityManager.persist(new Cliente("Seed", "User", "SEED-001", "seed@progra.local", "999999999"));
    }
}
