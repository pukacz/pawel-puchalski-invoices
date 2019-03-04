package pl.coderstrust.invoices.database.hibernate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.coderstrust.Application;
import pl.coderstrust.invoices.model.Invoice;

import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class HibernateDatabaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveInvoiceTest() throws Exception {
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
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Invoice.class, new DeserializationInvoice());
        mapper.registerModule(module);
        Invoice addedInvoice = mapper.readValue(addedInvoiceJson, Invoice.class);

        //than
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
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Invoice.class, new DeserializationInvoice());
        mapper.registerModule(module);
        Invoice addedInvoice = mapper.readValue(addedInvoiceJson, Invoice.class);
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
        String addInvoiceToDBJson = mockMvc.perform(post("/invoices/add")
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
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Invoice.class, new DeserializationInvoice());
        mapper.registerModule(module);
        return mapper.readValue(addInvoiceToDBJson, Invoice.class);
    }

}

class DeserializationInvoice extends StdDeserializer<Invoice> {

    public DeserializationInvoice() {
        this(null);
    }

    public DeserializationInvoice(Class<?> invoice) {
        super(invoice);
    }

    @Override
    public Invoice deserialize(JsonParser jp, DeserializationContext invoice) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Long id = (Long) ((IntNode) node.get("id")).longValue();
        String issue = node.get("issue").asText();
        String issueDateString = node.get("issueDate").asText();
        LocalDate issueDate = LocalDate.parse(issueDateString);

        return new Invoice(id,
                issue,
                issueDate,
                null,
                null,
                null);
    }
}
