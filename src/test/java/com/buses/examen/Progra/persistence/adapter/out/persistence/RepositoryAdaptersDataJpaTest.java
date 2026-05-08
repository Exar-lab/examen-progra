package com.buses.examen.Progra.persistence.adapter.out.persistence;

import com.buses.examen.Progra.customer.adapter.out.persistence.JpaClienteRepository;
import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.application.port.out.BusRepositoryPort;
import com.buses.examen.Progra.fleet.adapter.out.persistence.JpaAsientoRepository;
import com.buses.examen.Progra.fleet.adapter.out.persistence.JpaBusRepository;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.loyalty.adapter.out.persistence.JpaMovimientoPuntosRepository;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.sales.application.port.out.CompraRepositoryPort;
import com.buses.examen.Progra.sales.application.port.out.TicketRepositoryPort;
import com.buses.examen.Progra.sales.adapter.out.persistence.JpaCompraRepository;
import com.buses.examen.Progra.sales.adapter.out.persistence.JpaTicketRepository;
import com.buses.examen.Progra.sales.domain.CanalCompra;
import com.buses.examen.Progra.sales.domain.Comprobante;
import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.Ticket;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.adapter.out.persistence.JpaServicioRepository;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        JpaClienteRepository.class,
        JpaServicioRepository.class,
        JpaBusRepository.class,
        JpaAsientoRepository.class,
        JpaTicketRepository.class,
        JpaCompraRepository.class,
        JpaMovimientoPuntosRepository.class
})
class RepositoryAdaptersDataJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClienteRepositoryPort clienteRepositoryPort;

    @Autowired
    private ServicioRepositoryPort servicioRepositoryPort;

    @Autowired
    private BusRepositoryPort busRepositoryPort;

    @Autowired
    private AsientoRepositoryPort asientoRepositoryPort;

    @Autowired
    private TicketRepositoryPort ticketRepositoryPort;

    @Autowired
    private CompraRepositoryPort compraRepositoryPort;

    @Autowired
    private MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort;

    /** Verifica que el adaptador de cliente consulta por email y pasaporte. */
    @Test
    public void shouldFindCustomerByEmailAndPassport() {
        final Cliente cliente = new Cliente("Juan", "Perez", "P-123", "CR", "juan@mail.com", "999");
        entityManager.persist(cliente);
        entityManager.flush();

        assertThat(clienteRepositoryPort.findByEmail("juan@mail.com")).isPresent();
        assertThat(clienteRepositoryPort.findByDocumentoIdentidad("P-123")).isPresent();
    }

    /** Verifica que el adaptador de servicio filtra por ruta y ventana temporal. */
    @Test
    public void shouldFindServicesByRouteAndDateWindow() {
        final BaseGraph graph = persistBaseGraph();
        persistNonMatchingService(graph);

        final List<Servicio> servicios = servicioRepositoryPort.findByRutaIdAndSalidaProgramadaBetween(
                graph.ruta.getId(),
                OffsetDateTime.now().plusHours(1),
                OffsetDateTime.now().plusDays(1).plusHours(2)
        );

        assertThat(servicios)
                .hasSize(1)
                .extracting(Servicio::getId)
                .containsExactly(graph.servicio.getId());
    }

    /** Verifica que el adaptador de asientos recupera la disposición por bus. */
    @Test
    public void shouldRetrieveBusSeatingLayoutByBusId() {
        final BaseGraph graph = persistBaseGraph();
        final Asiento asientoDos = asientoRepositoryPort.save(new Asiento(graph.bus, "2", 1, "REG"));

        final Bus bus = busRepositoryPort.findById(graph.bus.getId()).orElseThrow();
        final List<Asiento> seats = asientoRepositoryPort.findByBusId(bus.getId());

        assertThat(bus.getId()).isEqualTo(graph.bus.getId());
        assertThat(seats)
                .extracting(Asiento::getId)
                .containsExactlyInAnyOrder(graph.asiento.getId(), asientoDos.getId());
        assertThat(seats)
                .extracting(Asiento::getBusId)
                .containsOnly(graph.bus.getId());
    }

    /** Verifica que el adaptador de ticket encuentra el código único emitido. */
    @Test
    public void shouldFindTicketByUniqueCode() {
        final BaseGraph graph = persistBaseGraph();
        final Compra compra = new Compra(graph.cliente, graph.tarjeta, CanalCompra.WEB, "op-1", OffsetDateTime.now());
        entityManager.persist(compra);
        final Ticket ticket = Ticket.emitir(compra, graph.servicio, graph.cliente, graph.asiento, "TK-100", BigDecimal.valueOf(42L));
        compra.agregarTicket(ticket);
        entityManager.persist(ticket);
        entityManager.flush();

        final Ticket found = ticketRepositoryPort.findByCodigoTicket("TK-100").orElseThrow();

        assertThat(found.getId()).isEqualTo(ticket.getId());
        assertThat(found.getCodigoTicket()).isEqualTo("TK-100");
    }

    /** Verifica que el adaptador de compra persiste el agregado y asigna identificadores. */
    @Test
    public void shouldSavePurchaseAggregateAndPopulateIdentifiers() {
        final BaseGraph graph = persistBaseGraph();
        final Compra compra = new Compra(graph.cliente, graph.tarjeta, CanalCompra.WEB, "op-2", OffsetDateTime.now());
        final Ticket ticket = Ticket.emitir(compra, graph.servicio, graph.cliente, graph.asiento, "TK-200", BigDecimal.valueOf(45L));
        final Comprobante comprobante = Comprobante.emitirParaCompra(compra, "FACTURA", "F001", "000777", BigDecimal.valueOf(45L), "CRC");
        compra.agregarTicket(ticket);

        final Compra persisted = compraRepositoryPort.save(compra);

        assertThat(persisted.getId()).isNotNull();
        assertThat(persisted.getTickets()).hasSize(1);
        assertThat(persisted.getTickets().getFirst().getId()).isNotNull();
        assertThat(persisted.getComprobante()).isNotNull();
        assertThat(persisted.getComprobante().getId()).isNotNull();
        assertThat(persisted.getComprobante().getNumero()).isEqualTo(comprobante.getNumero());
    }

    /** Verifica que el historial de puntos se ordena por fecha descendente. */
    @Test
    public void shouldReturnLoyaltyHistorySortedByDateDesc() {
        final BaseGraph graph = persistBaseGraph();
        final Compra compra = new Compra(graph.cliente, graph.tarjeta, CanalCompra.WEB, "op-3", OffsetDateTime.now());
        entityManager.persist(compra);

        final MovimientoPuntos first = new MovimientoPuntos(graph.cliente, compra);
        final MovimientoPuntos second = new MovimientoPuntos(graph.cliente, compra);
        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.flush();

        final List<MovimientoPuntos> history = movimientoPuntosRepositoryPort.findByClienteIdOrderByFechaMovimientoDesc(graph.cliente.getId());

        assertThat(history).hasSize(2);
        assertThat(history)
                .extracting(MovimientoPuntos::getFechaMovimiento)
                .isSortedAccordingTo((left, right) -> right.compareTo(left));
    }

    private BaseGraph persistBaseGraph() {
        final Pais pais = new Pais("CR", "Costa Rica");
        entityManager.persist(pais);
        final Ciudad origen = new Ciudad(pais, "San Jose", "SJO");
        final Ciudad destino = new Ciudad(pais, "Liberia", "LIR");
        entityManager.persist(origen);
        entityManager.persist(destino);
        final Compania compania = new Compania("Tica Bus", "3101123456");
        entityManager.persist(compania);
        final Bus bus = new Bus(compania, "CR-123", "Volvo", 40);
        entityManager.persist(bus);
        final Asiento asiento = new Asiento(bus, "1", 1, "REG");
        entityManager.persist(asiento);
        final Ruta ruta = new Ruta(origen, destino, 240, 220);
        entityManager.persist(ruta);
        final Servicio servicio = new Servicio(
                ruta,
                bus,
                OffsetDateTime.now().plusDays(1),
                OffsetDateTime.now().plusDays(1).plusHours(4),
                BigDecimal.valueOf(30),
                EstadoServicio.PROGRAMADO,
                40
        );
        entityManager.persist(servicio);
        final Cliente cliente = new Cliente("Ana", "Rojas", "P-999", "CR", "ana@mail.com", "888");
        entityManager.persist(cliente);
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, "Ana Rojas", "VISA", "1111", 12, 2030, "tok-1", "4111******1111", "999");
        entityManager.persist(tarjeta);
        entityManager.flush();
        return new BaseGraph(cliente, tarjeta, ruta, servicio, bus, asiento);
    }

    private void persistNonMatchingService(final BaseGraph graph) {
        final Pais pais = new Pais("NI", "Nicaragua");
        entityManager.persist(pais);
        final Ciudad origen = new Ciudad(pais, "Managua", "MGA");
        final Ciudad destino = new Ciudad(pais, "Leon", "LEO");
        entityManager.persist(origen);
        entityManager.persist(destino);
        final Ruta wrongRoutePath = new Ruta(origen, destino, 90, 50);
        entityManager.persist(wrongRoutePath);

        final Servicio wrongRoute = new Servicio(
                wrongRoutePath,
                graph.bus,
                OffsetDateTime.now().plusHours(3),
                OffsetDateTime.now().plusHours(6),
                BigDecimal.valueOf(25),
                EstadoServicio.PROGRAMADO,
                40
        );
        final Servicio outOfWindow = new Servicio(
                graph.ruta,
                graph.bus,
                OffsetDateTime.now().plusDays(3),
                OffsetDateTime.now().plusDays(3).plusHours(2),
                BigDecimal.valueOf(25),
                EstadoServicio.PROGRAMADO,
                40
        );
        entityManager.persist(wrongRoute);
        entityManager.persist(outOfWindow);
        entityManager.flush();
    }

    private record BaseGraph(Cliente cliente, Tarjeta tarjeta, Ruta ruta, Servicio servicio, Bus bus, Asiento asiento) {
    }
}
