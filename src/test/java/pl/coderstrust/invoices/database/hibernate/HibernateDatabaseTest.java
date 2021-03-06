package pl.coderstrust.invoices.database.hibernate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.coderstrust.Application;
import pl.coderstrust.invoices.database.InvoiceJsonSerializer;
import pl.coderstrust.invoices.model.Invoice;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@TestPropertySource(properties = "spring.config.name=hibernate")
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HibernateDatabaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private MockMvc mockMvc;
    private InvoiceJsonSerializer jsonConverter = new InvoiceJsonSerializer();
    @Autowired
    private WebApplicationContext wac;
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
    public void saveInvoiceTest() throws Exception {
        //given
        String jsonContent = "{\"id\":3,\"issue\":\"Poznan\",\"issueDate\":\"2019-03-03\",\"seller\":{\"id\":2,\"name\":\"Zenek z poczty\",\"taxIdentificationNumber\":\"\"},\"buyer\":{\"id\":1,\"name\":\"Polbicycle\",\"taxIdentificationNumber\":\"\"},\"entries\":[{\"id\":4,\"unit\":\"sztuki\",\"productName\":\"rower\",\"amount\":\"1\",\"price\":1845,\"vat\":\"VAT_23\"}]}";

        //when
        String addedInvoiceJson = mockMvc.perform(post("/invoices/add")
                .with(csrf())
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n"
                        + "    \"id\": 0,\n"
                        + "    \"issue\": \"Poznan\",\n"
                        + "    \"issueDate\": \"2019-03-03\",\n"
                        + "    \"seller\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Zenek z poczty\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"buyer\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Polbicycle\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"entries\": [\n"
                        + "        {\n"
                        + "            \"id\": 0,\n"
                        + "            \"unit\": \"sztuki\",\n"
                        + "            \"productName\": \"rower\",\n"
                        + "            \"amount\": 1,\n"
                        + "            \"price\": 1845,\n"
                        + "            \"vat\": 0\n"
                        + "        }\n"
                        + "    ]\n"
                        + "}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Invoice addedInvoice = jsonConverter.getInvoiceFromJson(addedInvoiceJson);

        //than
        Assert.assertEquals(jsonContent, addedInvoiceJson);
        mockMvc.perform(get("/invoices/" + addedInvoice.getId().toString())
                 .with(csrf())
                 .with(user("user").password("password")))
                .andDo(print())
                .andExpect(jsonPath("$.issueDate", is("2019-03-03")));

    }

    @Test
    public void updateInvoiceTest() throws Exception {
        //given
        Invoice addedInvoiceToDB = addInvoiceAndGetID();
        String addedInvoiceId = String.valueOf(addedInvoiceToDB.getId());

        //when
        String addedInvoiceJson = mockMvc.perform(post("/invoices/add")
                .with(csrf())
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n"
                        + "    \"id\": " + addedInvoiceId + ",\n"
                        + "    \"issue\": \"Szczecin\",\n"
                        + "    \"issueDate\": \"2019-02-27\",\n"
                        + "    \"seller\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Zenek z poczty\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"buyer\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Polbicycle\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"entries\": [\n"
                        + "        {\n"
                        + "            \"id\": 0,\n"
                        + "            \"unit\": \"sztuki\",\n"
                        + "            \"productName\": \"rower\",\n"
                        + "            \"amount\": 1,\n"
                        + "            \"price\": 1845,\n"
                        + "            \"vat\": 0\n"
                        + "        }\n"
                        + "    ]\n"
                        + "}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Invoice addedInvoice = jsonConverter.getInvoiceFromJson(addedInvoiceJson);
        //than
        mockMvc.perform(get("/invoices/" + addedInvoice.getId().toString())
                  .with(csrf())
                  .with(user("user").password("password")))
                .andExpect(jsonPath("$.issue", is("Szczecin")));
    }

    @Test
    public void getInvoicesTest() throws Exception {
        //given
        addInvoiceAndGetID();

        //than
        mockMvc.perform(get("/invoices")
                  .with(csrf())
                  .with(user("user").password("password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueDate", is("2019-03-02")));
    }

    @Test
    public void getInvoiceByIdTest() throws Exception {
        //given
        Invoice addedInvoiceToDB = addInvoiceAndGetID();
        String addedInvoiceId = String.valueOf(addedInvoiceToDB.getId());

        //than
        mockMvc.perform(get("/invoices/" + addedInvoiceId)
                 .with(csrf())
                 .with(user("user").password("password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueDate", is("2019-03-02")))
                .andExpect(jsonPath("$.issue", is("Konin")));
    }

    @Test
    public void getInvoiceByDateTest() throws Exception {
        //given
        Invoice addedInvoiceToDB = addInvoiceAndGetID();
        String addedInvoiceIssueDate = String.valueOf(addedInvoiceToDB.getIssueDate());

        //than
        mockMvc.perform(get("/invoices/byDates?fromDate=" + addedInvoiceIssueDate + "&toDate=" + addedInvoiceIssueDate)
                 .with(csrf())
                 .with(user("user").password("password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issue", is("Konin")))
                .andExpect(jsonPath("$[0].issueDate", is("2019-03-02")));
    }

    @Test
    public void getDeleteInvoiceByIdTest() throws Exception {
        //given
        Invoice addedInvoiceToDB = addInvoiceAndGetID();
        String addedInvoiceId = String.valueOf(addedInvoiceToDB.getId());

        //than
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/" + addedInvoiceId)
                 .with(csrf())
                 .with(user("user").password("password")))
                .andExpect(status().isOk());
    }

    private Invoice addInvoiceAndGetID() throws Exception {
        String addedInvoiceToDBJson = mockMvc.perform(post("/invoices/add")
                .with(csrf())
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n"
                        + "    \"id\": 0,\n"
                        + "    \"issue\": \"Konin\",\n"
                        + "    \"issueDate\": \"2019-03-02\",\n"
                        + "    \"seller\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Zenek z poczty\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"buyer\": {\n"
                        + "        \"id\": 0,\n"
                        + "        \"name\": \"Polbicycle\",\n"
                        + "        \"taxIdentificationNumber\": \"\"\n"
                        + "    },\n"
                        + "    \"entries\": [\n"
                        + "        {\n"
                        + "            \"id\": 0,\n"
                        + "            \"unit\": \"sztuki\",\n"
                        + "            \"productName\": \"rower\",\n"
                        + "            \"amount\": 1,\n"
                        + "            \"price\": 1845,\n"
                        + "            \"vat\": 0\n"
                        + "        }\n"
                        + "    ]\n"
                        + "}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonConverter.getInvoiceFromJson(addedInvoiceToDBJson);
    }

    @Test
    public void shouldGetId() {
        //when
        Long actual = new HibernateDatabase().getIdFromObject(1234);
        //then
        Assert.assertEquals(Long.valueOf(1234), actual);
    }

    @Test
    public void shouldThrowExceptionWhenWrongId() {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Argument Id must be Long type.");
        //when
        Long actual = new HibernateDatabase().getIdFromObject("1,2.3#4");
        //then
        Assert.assertEquals(Long.valueOf(1234), actual);
    }
}
