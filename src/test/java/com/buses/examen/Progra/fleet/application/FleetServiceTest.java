package com.buses.examen.Progra.fleet.application;

import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.application.port.out.BusRepositoryPort;
import com.buses.examen.Progra.fleet.application.port.out.CompaniaRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de flota. */
class FleetServiceTest {

    @Test
    void shouldListCompaniesBusesAndSeats() {
        final CompaniaRepositoryPort companiaRepositoryPort = mock(CompaniaRepositoryPort.class);
        final BusRepositoryPort busRepositoryPort = mock(BusRepositoryPort.class);
        final AsientoRepositoryPort asientoRepositoryPort = mock(AsientoRepositoryPort.class);
        final FleetService service = new FleetService(companiaRepositoryPort, busRepositoryPort, asientoRepositoryPort);

        final Compania compania = new Compania("Tica Bus", "3101123456");
        final Bus bus = new Bus(compania, "CR-123", "Volvo", 40);
        final Asiento asiento = new Asiento(bus, "1", 1, "REG");
        when(companiaRepositoryPort.findAll()).thenReturn(List.of(compania));
        when(busRepositoryPort.findByCompaniaId(1L)).thenReturn(List.of(bus));
        when(asientoRepositoryPort.findByBusId(2L)).thenReturn(List.of(asiento));

        assertThat(service.listCompanies()).containsExactly(compania);
        assertThat(service.listBusesByCompany(1L)).containsExactly(bus);
        assertThat(service.listSeatsByBus(2L)).containsExactly(asiento);
    }

    @Test
    void shouldFindBusById() {
        final CompaniaRepositoryPort companiaRepositoryPort = mock(CompaniaRepositoryPort.class);
        final BusRepositoryPort busRepositoryPort = mock(BusRepositoryPort.class);
        final AsientoRepositoryPort asientoRepositoryPort = mock(AsientoRepositoryPort.class);
        final FleetService service = new FleetService(companiaRepositoryPort, busRepositoryPort, asientoRepositoryPort);

        final Bus bus = new Bus(new Compania("Tica Bus", "3101123456"), "CR-123", "Volvo", 40);
        when(busRepositoryPort.findById(10L)).thenReturn(Optional.of(bus));

        assertThat(service.findBusById(10L)).contains(bus);
    }
}
