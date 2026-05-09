package com.buses.examen.Progra.sales.domain;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Verifica el mapeo JPA y los constraints únicos del modelo de dominio de bus-ticket. */
@DataJpaTest
class BusTicketJpaMappingTest {

    @Autowired
    private EntityManager entityManager;

    /** Verifica que los enlaces opcionales (comprobante, movimiento) se persisten correctamente. */
    @Test
    void shouldPersistOptionalLinksAndUniqueTicketCode() {
        final Pais pais = new Pais("PE", "Peru");
        entityManager.persist(pais);
        final Ciudad origen = new Ciudad(pais, "Lima", "LIM");
        final Ciudad destino = new Ciudad(pais, "Cusco", "CUS");
        entityManager.persist(origen); entityManager.persist(destino);
        final Compania compania = new Compania("Buses Sur", "20123456789");
        entityManager.persist(compania);
        final Bus bus = new Bus(compania, "ABC-123", "Volvo", 2);
        entityManager.persist(bus);
        final Asiento asientoUno = new Asiento(bus, "1", 1, "REG");
        entityManager.persist(asientoUno);
        final Ruta ruta = new Ruta(origen, destino, 90, 120);
        entityManager.persist(ruta);
        final Servicio servicio = new Servicio(ruta, bus, OffsetDateTime.now().plusDays(1), OffsetDateTime.now().plusDays(1).plusHours(2), BigDecimal.valueOf(40), EstadoServicio.PROGRAMADO, 2);
        entityManager.persist(servicio);
        final Cliente cliente = new Cliente("A", "B", "123", "a@a.com", "999");
        entityManager.persist(cliente);
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, "A B", "VISA", "1111", 12, 2030, "tok", "4111******1111", "999");
        entityManager.persist(tarjeta);

        final Compra compra = new Compra(cliente, tarjeta, CanalCompra.WEB, "op", OffsetDateTime.now());
        entityManager.persist(compra);
        final Ticket ticket = Ticket.emitir(compra, servicio, cliente, asientoUno, "TK-1", BigDecimal.valueOf(40L));
        compra.agregarTicket(ticket);
        entityManager.persist(ticket);

        final Comprobante comprobante = new Comprobante();
        compra.vincularComprobante(comprobante);
        entityManager.persist(comprobante);
        final MovimientoPuntos mov = new MovimientoPuntos(cliente, compra);
        entityManager.persist(mov);

        assertThat(tarjeta.getCvv()).isNull();
        assertThat(compra.getTickets()).hasSize(1);
        assertThat(compra.getComprobante()).isNotNull();
    }

    /** Verifica que la base de datos rechaza dos tickets con el mismo codigoTicket. */
    @Test
    void shouldRejectDuplicateTicketCodeAtDatabaseLevel() {
        final BaseGraph graph = persistBaseGraph();

        final Compra compra = new Compra(graph.cliente, graph.tarjeta, CanalCompra.WEB, "op-dup-ticket", OffsetDateTime.now());
        entityManager.persist(compra);

        final Ticket first = Ticket.emitir(compra, graph.servicio, graph.cliente, graph.asientoUno, "TK-DUP", BigDecimal.valueOf(40L));
        final Ticket duplicate = Ticket.emitir(compra, graph.servicio, graph.cliente, graph.asientoDos, "TK-DUP", BigDecimal.valueOf(40L));
        compra.agregarTicket(first);

        entityManager.persist(first);
        entityManager.flush();

        assertThatThrownBy(() -> {
            entityManager.persist(duplicate);
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica que la base de datos rechaza dos reservas activas para el mismo asiento y servicio. */
    @Test
    void shouldRejectDuplicateActiveSeatReservationForSameServiceAndSeat() {
        final BaseGraph graph = persistBaseGraph();

        final ReservaAsiento first = ReservaAsiento.activa(graph.servicio, graph.asientoUno);
        final ReservaAsiento duplicate = ReservaAsiento.activa(graph.servicio, graph.asientoUno);

        entityManager.persist(first);
        entityManager.flush();

        assertThatThrownBy(() -> {
            entityManager.persist(duplicate);
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_pais_codigo_iso — dos países no pueden compartir código ISO. */
    @Test
    void shouldRejectDuplicatePaisCodigoIso() {
        entityManager.persist(new Pais("AR", "Argentina"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Pais("AR", "Argentina Duplicada"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_ciudad_pais_codigo — el código de ciudad es único dentro de su país. */
    @Test
    void shouldRejectDuplicateCiudadCodigoWithinSamePais() {
        Pais pais = new Pais("CL", "Chile");
        entityManager.persist(pais);
        entityManager.persist(new Ciudad(pais, "Santiago", "SCL"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Ciudad(pais, "Santiago Duplicada", "SCL"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_bus_placa — dos buses no pueden tener la misma placa. */
    @Test
    void shouldRejectDuplicateBusPlaca() {
        Compania compania = new Compania("Trans Express", "20999000001");
        entityManager.persist(compania);
        entityManager.persist(new Bus(compania, "XYZ-999", "Mercedes", 40));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Bus(compania, "XYZ-999", "Scania", 45));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_compania_nombre_comercial — el nombre comercial es único. */
    @Test
    void shouldRejectDuplicateCompaniaNombreComercial() {
        entityManager.persist(new Compania("Oltursa", "20100200001"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Compania("Oltursa", "20100200002"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_compania_ruc_nit — el RUC/NIT es único por compañía. */
    @Test
    void shouldRejectDuplicateCompaniaRucNit() {
        entityManager.persist(new Compania("Civa", "20200300001"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Compania("Civa Duplicada", "20200300001"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_cliente_documento — el documento de identidad es único por cliente. */
    @Test
    void shouldRejectDuplicateClienteDocumento() {
        entityManager.persist(new Cliente("Juan", "Perez", "DNI-001", "juan@mail.com", "111"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Cliente("Juan", "Otro", "DNI-001", "otro@mail.com", "222"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_cliente_email — el email es único por cliente. */
    @Test
    void shouldRejectDuplicateClienteEmail() {
        entityManager.persist(new Cliente("Ana", "Lopez", "DNI-002", "ana@mail.com", "333"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Cliente("Ana", "Otro", "DNI-003", "ana@mail.com", "444"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_asiento_bus_numero_piso — número y piso son únicos dentro del mismo bus. */
    @Test
    void shouldRejectDuplicateAsientoBusNumeroPiso() {
        Compania compania = new Compania("FlotaNorte", "20400500001");
        entityManager.persist(compania);
        Bus bus = new Bus(compania, "FN-001", "Scania", 50);
        entityManager.persist(bus);
        entityManager.persist(new Asiento(bus, "5", 1, "VIP"));
        entityManager.flush();
        assertThatThrownBy(() -> {
            entityManager.persist(new Asiento(bus, "5", 1, "REG"));
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    /** Verifica el constraint uk_comprobante_compra — existe exactamente un comprobante por compra. */
    @Test
    void shouldRejectDuplicateComprobanteForSameCompra() {
        final BaseGraph graph = persistBaseGraph();
        final Compra compra = new Compra(graph.cliente, graph.tarjeta, CanalCompra.WEB, "op-comp-dup", OffsetDateTime.now());
        entityManager.persist(compra);
        final Comprobante first = new Comprobante();
        compra.vincularComprobante(first);
        entityManager.flush();
        assertThatThrownBy(() -> {
            final Comprobante duplicate = new Comprobante();
            duplicate.vincularCompra(compra);
            entityManager.persist(duplicate);
            entityManager.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    private BaseGraph persistBaseGraph() {
        final Pais pais = new Pais("PE", "Peru");
        entityManager.persist(pais);
        final Ciudad origen = new Ciudad(pais, "Lima", "LIM");
        final Ciudad destino = new Ciudad(pais, "Cusco", "CUS");
        entityManager.persist(origen);
        entityManager.persist(destino);

        final Compania compania = new Compania("Buses Sur", "20123456789");
        entityManager.persist(compania);
        final Bus bus = new Bus(compania, "ABC-123", "Volvo", 2);
        entityManager.persist(bus);

        final Asiento asientoUno = new Asiento(bus, "1", 1, "REG");
        final Asiento asientoDos = new Asiento(bus, "2", 1, "REG");
        entityManager.persist(asientoUno);
        entityManager.persist(asientoDos);

        final Ruta ruta = new Ruta(origen, destino, 90, 120);
        entityManager.persist(ruta);
        final Servicio servicio = new Servicio(
                ruta,
                bus,
                OffsetDateTime.now().plusDays(1),
                OffsetDateTime.now().plusDays(1).plusHours(2),
                BigDecimal.valueOf(40),
                EstadoServicio.PROGRAMADO,
                2
        );
        entityManager.persist(servicio);

        final Cliente cliente = new Cliente("A", "B", "123", "a@a.com", "999");
        entityManager.persist(cliente);
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, "A B", "VISA", "1111", 12, 2030, "tok", "4111******1111", "999");
        entityManager.persist(tarjeta);
        entityManager.flush();

        return new BaseGraph(cliente, tarjeta, servicio, asientoUno, asientoDos);
    }

    private record BaseGraph(
            Cliente cliente,
            Tarjeta tarjeta,
            Servicio servicio,
            Asiento asientoUno,
            Asiento asientoDos
    ) {
    }
}
