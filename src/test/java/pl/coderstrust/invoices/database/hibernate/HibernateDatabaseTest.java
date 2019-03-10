package pl.coderstrust.invoices.database.hibernate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.coderstrust.Application;
import pl.coderstrust.invoices.database.InvoiceJsonSerializer;
import pl.coderstrust.invoices.model.Invoice;

@TestPropertySource(properties = "spring.config.name=hibernate")
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HibernateDatabaseTest {

    @Autowired
    private MockMvc mockMvc;
    private InvoiceJsonSerializer jsonConverter = new InvoiceJsonSerializer();

    @Test
    public void saveInvoiceTest() throws Exception {
        //given
        String jsonContent = "{\"id\":3,\"issue\":\"Poznan\",\"issueDate\":\"2019-03-03\",\"seller\":{\"id\":2,\"name\":\"Zenek z poczty\",\"taxIdentificationNumber\":\"\"},\"buyer\":{\"id\":1,\"name\":\"Polbicycle\",\"taxIdentificationNumber\":\"\"},\"entries\":[{\"id\":4,\"unit\":\"sztuki\",\"productName\":\"rower\",\"amount\":\"1\",\"price\":1845,\"vat\":\"VAT_23\"}]}";

        //when
        String addedInvoiceJson = mockMvc.perform(post("/invoices/add")
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
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        Invoice addedInvoice = jsonConverter.getInvoiceFromJson(addedInvoiceJson);

        //than
        Assert.assertEquals(jsonContent, addedInvoiceJson);
        mockMvc.perform(get("/invoices/" + addedInvoice.getId().toString()))
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
        mockMvc.perform(get("/invoices/" + addedInvoice.getId().toString()))
            .andExpect(jsonPath("$.issue", is("Szczecin")));
    }

    @Test
    public void getInvoicesTest() throws Exception {
        //given
        addInvoiceAndGetID();

        //than
        mockMvc.perform(get("/invoices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].issueDate", is("2019-03-02")));
    }

    @Test
    public void getInvoiceByIdTest() throws Exception {
        //given
        Invoice addedInvoiceToDB = addInvoiceAndGetID();
        String addedInvoiceId = String.valueOf(addedInvoiceToDB.getId());

        //than
        mockMvc.perform(get("/invoices/" + addedInvoiceId))
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
        mockMvc.perform(get("/invoices/byDates?fromDate=" + addedInvoiceIssueDate + "&toDate=" + addedInvoiceIssueDate))
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/" + addedInvoiceId))
            .andExpect(status().isOk());
    }

    private Invoice addInvoiceAndGetID() throws Exception {
        String addedInvoiceToDBJson = mockMvc.perform(post("/invoices/add")
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
        //given
        HibernateDatabase database = new HibernateDatabase();

        //when
        Long actual = database.getIdFromObject(1234);

        //then
        Assert.assertEquals(Long.valueOf(1234), actual);
    }
}
