package com.buses.examen.Progra.customer.adapter.in.web;

import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.adapter.in.web.mapper.CustomerWebMapper;
import com.buses.examen.Progra.customer.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(CustomerWebMapper.class)
@WithMockUser
class CustomerControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterCustomerUseCase registerCustomerUseCase;

    @MockitoBean
    private CustomerQueryUseCase customerQueryUseCase;

    /**
     * Verifica que el registro de cliente responde con estado creado.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldRegisterCustomerReturningCreated() throws Exception {
        when(registerCustomerUseCase.register(any(RegisterCustomerCommand.class)))
                .thenReturn(new RegisterCustomerResult(17L));

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombres":"Ana",
                                  "apellidos":"Perez",
                                  "documentoIdentidad":"P-123",
                                  "nacionalidad":"CR",
                                  "email":"ana@mail.com",
                                  "telefono":"888",
                                  "username":"ana_user",
                                  "password":"ClaveSegura123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(17L));
    }

    /**
     * Verifica que una solicitud inválida de cliente responde con 400.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn400WhenRegisterCustomerRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombres":"",
                                  "apellidos":"Perez",
                                  "documentoIdentidad":"P-123",
                                  "nacionalidad":"CR",
                                  "email":"invalid-mail",
                                  "telefono":"888",
                                  "username":"",
                                  "password":"123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica que el registro de tarjeta responde con estado creado.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldRegisterCardReturningCreated() throws Exception {
        when(registerCustomerUseCase.registerCard(any(RegisterCardCommand.class)))
                .thenReturn(new RegisterCardResult(50L, "4111******1111"));

        mockMvc.perform(post("/api/customers/10/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titular":"Ana Perez",
                                  "numeroTarjeta":"4111111111111111",
                                  "fechaExpiracion":"12/2030",
                                  "cvv":"999"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(50L))
                .andExpect(jsonPath("$.enmascarada").value("4111******1111"));
    }

    /**
     * Verifica que una solicitud inválida de tarjeta responde con 400.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn400WhenRegisterCardRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/customers/10/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titular":"Ana Perez",
                                  "numeroTarjeta":"",
                                  "fechaExpiracion":"13/30",
                                  "cvv":"9"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(registerCustomerUseCase, never()).registerCard(any(RegisterCardCommand.class));
    }

    /**
     * Verifica que la búsqueda por documento inexistente responde con 404.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn404WhenCustomerDocumentMissing() throws Exception {
        when(customerQueryUseCase.findByDocumentoIdentidad("P-404")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/document/P-404"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica que la búsqueda por documento existente devuelve el cliente.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldLookupCustomerByDocument() throws Exception {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-1", "CR", "ana@mail.com", "111");
        setId(cliente, 33L);
        when(customerQueryUseCase.findByDocumentoIdentidad("P-1")).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/customers/document/P-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(33L));
    }

    private static void setId(final Object target, final Long id) {
        try {
            final Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
