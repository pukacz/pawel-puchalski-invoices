package pl.coderstrust.invoices.controller;

import org.junit.Before;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.service.InvoiceBook;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "spring.config.name=filedatabase")
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "file")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvoiceBook service;
    @Autowired
    private WebApplicationContext wac;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void shouldGetEmptyListOfInvoices() throws Exception {
        when(service.getAllInvoices()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/invoices")
                    .with(csrf())
                    .with(user("user").password("password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void testGetInvoiceById() throws Exception {
        when(service.getInvoiceById(0L)).thenReturn(new Invoice(0L, "", null, null, null, null));
        this.mockMvc.perform(get("/invoices/0")
                    .with(csrf())
                    .with(user("user").password("password")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }
}
