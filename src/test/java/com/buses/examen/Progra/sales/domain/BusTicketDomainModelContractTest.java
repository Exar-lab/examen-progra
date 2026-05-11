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
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.sales.exception.MaxTicketsExceededException;
import com.buses.examen.Progra.sales.exception.PurchaseWindowExpiredException;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import com.buses.examen.Progra.service.exception.CapacidadAgotadaException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Verifica contratos de dominio del modelo bus-ticket: invariantes, campos y excepciones. */
class BusTicketDomainModelContractTest {

    /** Verifica que las 14 entidades JPA exponen los campos de dominio requeridos. */
    @Test
    void shouldExposeMandatoryEntitySetAndRequiredCoreFields() {
        final Map<Class<?>, Set<String>> entityFields = Map.ofEntries(
                Map.entry(Cliente.class, Set.of("id", "nombres", "apellidos", "documentoIdentidad", "email", "telefono", "puntosAcumulados", "activo")),
                Map.entry(Tarjeta.class, Set.of("id", "cliente", "titular", "marca", "ultimo4", "mesExpiracion", "anioExpiracion", "tokenReferencia", "enmascarada", "activa")),
                Map.entry(Pais.class, Set.of("id", "codigoIso", "nombre")),
                Map.entry(Ciudad.class, Set.of("id", "pais", "nombre", "codigo")),
                Map.entry(Compania.class, Set.of("id", "nombreComercial", "rucNit", "activa")),
                Map.entry(Bus.class, Set.of("id", "compania", "placa", "modelo", "capacidadTotal", "activo")),
                Map.entry(Ruta.class, Set.of("id", "ciudadOrigen", "ciudadDestino", "duracionMinutos", "distanciaKm", "activa")),
                Map.entry(Servicio.class, Set.of("id", "ruta", "bus", "salidaProgramada", "llegadaProgramada", "precioBase", "estado", "capacidadDisponible")),
                Map.entry(Compra.class, Set.of("id", "cliente", "tarjeta", "fechaCompra", "total", "estado", "canal", "codigoOperacionExterna")),
                Map.entry(Ticket.class, Set.of("id", "compra", "servicio", "cliente", "asiento", "codigoTicket", "precioFinal", "estado", "fechaEmision")),
                Map.entry(Comprobante.class, Set.of("id", "compra", "tipo", "serie", "numero", "fechaEmision", "montoTotal", "moneda", "estado")),
                Map.entry(MovimientoPuntos.class, Set.of("id", "cliente", "compra", "tipoMovimiento", "puntos", "saldoPosterior", "fechaMovimiento", "motivo")),
                Map.entry(Asiento.class, Set.of("id", "bus", "numero", "piso", "categoria", "activo")),
                Map.entry(ReservaAsiento.class, Set.of("id", "servicio", "asiento", "ticket", "estadoReserva", "expiraEn", "creadoEn"))
        );

        assertThat(entityFields).hasSize(14);
        entityFields.forEach((type, fields) -> {
            assertThat(type.isAnnotationPresent(Entity.class)).isTrue();
            assertThat(fields).allMatch(f -> hasField(type, f));
            assertThat(getFields(type)).anySatisfy(field -> assertThat(field.isAnnotationPresent(Id.class)).isTrue());
        });
    }

    /** Verifica que una compra admite hasta 5 tickets y que el CVV no se persiste en la tarjeta. */
    @Test
    void shouldCreateCompraWithAtMostFiveTicketsAndWithoutPersistingCvv() {
        final Pais pais = new Pais("PE", "Peru");
        final Ciudad lima = new Ciudad(pais, "Lima", "LIM");
        final Ciudad cusco = new Ciudad(pais, "Cusco", "CUS");
        final Compania compania = new Compania("Buses Sur", "20123456789");
        final Bus bus = new Bus(compania, "ABC-123", "Volvo 9700", 40);
        final Asiento asiento = new Asiento(bus, "1", 1, "REGULAR");
        final Ruta ruta = new Ruta(lima, cusco, 90, 120.0);
        final Servicio servicio = new Servicio(ruta, bus, OffsetDateTime.now().plusDays(3), OffsetDateTime.now().plusDays(3).plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);
        final Cliente cliente = new Cliente("Ana", "Perez", "12345678", "CR", "ana@mail.com", "999999999");
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, "Ana Perez", "VISA", "1111", 12, 2030, "tok_123", "411111******1111");
        final Compra compra = new Compra(cliente, tarjeta, CanalCompra.WEB, "op-1", servicio.getSalidaProgramada().minusDays(1));

        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-001", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-002", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-003", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-004", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-005", BigDecimal.valueOf(50L)));

        assertThat(compra.getTickets()).hasSize(5);
        assertThat(tarjeta.getCvv()).isNull();
    }

    /** Verifica que agregar un sexto ticket lanza MaxTicketsExceededException. */
    @Test
    void shouldRejectSixthTicketForCompra() {
        final Pais pais = new Pais("PE", "Peru");
        final Ciudad lima = new Ciudad(pais, "Lima", "LIM");
        final Ciudad cusco = new Ciudad(pais, "Cusco", "CUS");
        final Compania compania = new Compania("Buses Sur", "20123456789");
        final Bus bus = new Bus(compania, "ABC-123", "Volvo 9700", 40);
        final Asiento asiento = new Asiento(bus, "1", 1, "REGULAR");
        final Ruta ruta = new Ruta(lima, cusco, 90, 120.0);
        final OffsetDateTime compraTime = OffsetDateTime.now();
        final Servicio servicio = new Servicio(ruta, bus, compraTime.plusDays(2), compraTime.plusDays(2).plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);
        final Cliente cliente = new Cliente("Ana", "Perez", "12345678", "CR", "ana@mail.com", "999999999");
        final Compra compra = new Compra(cliente, null, CanalCompra.WEB, "op-1", compraTime);

        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-001", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-002", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-003", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-004", BigDecimal.valueOf(50L)));
        compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-005", BigDecimal.valueOf(50L)));

        assertThatThrownBy(() -> compra.agregarTicket(Ticket.emitir(compra, servicio, cliente, asiento, "TKT-006", BigDecimal.valueOf(50L))))
                .isInstanceOf(MaxTicketsExceededException.class)
                .hasMessageContaining("Maximo 5 tickets");
    }

    /** Verifica la ventana de compra inclusiva [fechaCompra, fechaCompra+7d]. */
    @Test
    void shouldEnforceInclusivePurchaseWindowBounds() {
        final Pais pais = new Pais("PE", "Peru");
        final Ciudad lima = new Ciudad(pais, "Lima", "LIM");
        final Ciudad cusco = new Ciudad(pais, "Cusco", "CUS");
        final Compania compania = new Compania("Buses Sur", "20123456789");
        final Bus bus = new Bus(compania, "ABC-123", "Volvo 9700", 40);
        final Asiento asiento = new Asiento(bus, "1", 1, "REGULAR");
        final Ruta ruta = new Ruta(lima, cusco, 90, 120.0);
        final OffsetDateTime purchaseTime = OffsetDateTime.now();
        final Cliente cliente = new Cliente("Ana", "Perez", "12345678", "CR", "ana@mail.com", "999999999");
        final Compra compra = new Compra(cliente, null, CanalCompra.WEB, "op-1", purchaseTime);

        final Servicio beforePurchase = new Servicio(ruta, bus, purchaseTime.minusMinutes(1), purchaseTime.plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);
        final Servicio allowedAtLowerBound = new Servicio(ruta, bus, purchaseTime, purchaseTime.plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);
        final Servicio allowedAtUpperBound = new Servicio(ruta, bus, purchaseTime.plusDays(7), purchaseTime.plusDays(7).plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);
        final Servicio denied = new Servicio(ruta, bus, purchaseTime.plusDays(8), purchaseTime.plusDays(8).plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 40);

        final Ticket lowerBoundTicket = Ticket.emitir(compra, allowedAtLowerBound, cliente, asiento, "TKT-ALLOW-LOW", BigDecimal.valueOf(50L));
        final Ticket upperBoundTicket = Ticket.emitir(compra, allowedAtUpperBound, cliente, asiento, "TKT-ALLOW-UP", BigDecimal.valueOf(50L));

        assertThatThrownBy(() -> Ticket.emitir(compra, beforePurchase, cliente, asiento, "TKT-BEFORE", BigDecimal.valueOf(50L)))
                .isInstanceOf(PurchaseWindowExpiredException.class)
                .hasMessageContaining("7 dias");
        assertThat(lowerBoundTicket).isNotNull();
        assertThat(upperBoundTicket).isNotNull();
        assertThatThrownBy(() -> Ticket.emitir(compra, denied, cliente, asiento, "TKT-DENY", BigDecimal.valueOf(50L)))
                .isInstanceOf(PurchaseWindowExpiredException.class)
                .hasMessageContaining("7 dias");
    }

    /** Verifica que el código de ticket es inmutable al no exponer mutador público. */
    @Test
    void shouldBlockTicketCodeMutation() {
        assertThat(Ticket.class.getDeclaredMethods())
                .noneMatch(method -> Modifier.isPublic(method.getModifiers())
                        && method.getName().toLowerCase().contains("codigo")
                        && method.getParameterCount() == 1);
    }

    /** Verifica que reservarCupo agota la capacidad correctamente y lanza CapacidadAgotadaException al excederla. */
    @Test
    void shouldRespectCapacityAndRejectOverflow() {
        final Pais pais = new Pais("PE", "Peru");
        final Ciudad lima = new Ciudad(pais, "Lima", "LIM");
        final Ciudad cusco = new Ciudad(pais, "Cusco", "CUS");
        final Compania compania = new Compania("Buses Sur", "20123456789");
        final Bus bus = new Bus(compania, "ABC-123", "Volvo 9700", 2);
        final Ruta ruta = new Ruta(lima, cusco, 90, 120.0);
        final OffsetDateTime departure = OffsetDateTime.now().plusDays(1);
        final Servicio servicio = new Servicio(ruta, bus, departure, departure.plusHours(2), BigDecimal.valueOf(50), EstadoServicio.PROGRAMADO, 2);

        servicio.reservarCupo();
        servicio.reservarCupo();

        assertThat(servicio.getCapacidadDisponible()).isEqualTo(0);
        assertThatThrownBy(servicio::reservarCupo)
                .isInstanceOf(CapacidadAgotadaException.class)
                .hasMessageContaining("Capacidad agotada");
    }

    /** Verifica que el puerto RoutePlannerPort es una interfaz con exactamente un método. */
    @Test
    void shouldKeepRoutePlannerPortSeam() {
        assertThat(RoutePlannerPort.class.isInterface()).isTrue();
        assertThat(RoutePlannerPort.class.getDeclaredMethods()).hasSize(1);
        assertThat(RoutePlannerPort.class.getDeclaredMethods()[0].getName()).isEqualTo("findBestRoutes");
    }

    /** Verifica que los enlaces opcionales entre agregados (comprobante, movimiento, ticket) funcionan en memoria. */
    @Test
    void shouldLinkOptionalEntitiesInsideAggregate() {
        final Cliente cliente = new Cliente("Ana", "Perez", "12345678", "CR", "ana@mail.com", "999999999");
        final Compra compra = new Compra();
        final Comprobante comprobante = new Comprobante();
        final MovimientoPuntos movimientoPuntos = new MovimientoPuntos(cliente, compra);
        final ReservaAsiento reservaAsiento = new ReservaAsiento();

        compra.vincularComprobante(comprobante);
        reservaAsiento.vincularTicket(null);

        assertThat(compra.getComprobante()).isNotNull();
        assertThat(movimientoPuntos.getCompra()).isEqualTo(compra);
        assertThat(reservaAsiento.getTicket()).isNull();
    }

    private static boolean hasField(final Class<?> type, final String name) {
        return getFields(type).stream().anyMatch(f -> f.getName().equals(name));
    }

    private static Set<Field> getFields(final Class<?> type) {
        return Set.of(type.getDeclaredFields());
    }
}
