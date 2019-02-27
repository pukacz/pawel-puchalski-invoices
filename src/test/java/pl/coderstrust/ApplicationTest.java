package pl.coderstrust;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.invoices.controller.InvoiceController;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.model.VAT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    private static final String SEPARATOR = File.separator;
    private static final String TEST_FOLDER = "src" + SEPARATOR + "test" + SEPARATOR + "resources" + SEPARATOR
        + "file-database" + SEPARATOR;
    private static final String INVOICES_FILE_NAME = "invoicesTest.dat";
    private static final String INVOICES_IDS_FILE_NAME = "invoicesIdsTest.cor";

    private static final File INVOICES_FILE = new File(TEST_FOLDER + INVOICES_FILE_NAME);
    private static final File INVOICES_IDS_FILE = new File(TEST_FOLDER + INVOICES_IDS_FILE_NAME);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(mockMvc).isNotNull();
        assertThat(controller).isNotNull();
    }

    @BeforeClass
    public static void clearFilesBeforeAllTests() throws IOException {
        if (INVOICES_FILE.exists()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_FILE, "rw")) {
                randomAccessFile.setLength(0);
            }
        } else {
            INVOICES_FILE.createNewFile();
        }

        if (INVOICES_IDS_FILE.exists()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_IDS_FILE, "rw")) {
                randomAccessFile.setLength(0);
            }
        } else {
            INVOICES_IDS_FILE.createNewFile();
        }
    }

    @After
    public void clearAfterEachMethod() throws IOException {
        if (INVOICES_FILE.exists()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_FILE, "rw")) {
                randomAccessFile.setLength(0);
            }
        }
        if (INVOICES_IDS_FILE.exists()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_IDS_FILE, "rw")) {
                randomAccessFile.setLength(0);
            }
        }
    }

    @Test
    public void shouldSaveAndCheckContentOfInvoice() throws Exception {
        mockMvc
            .perform(post("/invoices/add")
                .content(invoiceToJson(getInvoices().get(0)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/invoices"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].issue", is("1")))
            .andExpect(jsonPath("$[0].issueDate", is("2019-01-01")))
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldUpdateInvoice() throws Exception {
        Invoice invoice = getInvoices().get(0);

        mockMvc
            .perform(post("/invoices/add")
                .content(invoiceToJson(invoice))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        Invoice invoice1 = new Invoice(getInvoices().get(1), 1L);

        mockMvc
            .perform(post("/invoices/add")
                .content(invoiceToJson(invoice1))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/invoices"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].issue", is("2")))
            .andExpect(jsonPath("$[0].issueDate", is("2017-08-02")))
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldDeleteInvoice() throws Exception {
        mockMvc
            .perform(post("/invoices/add")
                .content(invoiceToJson(getInvoices().get(0)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(delete("/invoices/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/invoices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnInvoice() throws Exception {
        mockMvc
            .perform(post("/invoices/add")
                .content(invoiceToJson(getInvoices().get(1)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/invoices/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnInvoicesByDate() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc
                .perform(post("/invoices/add")
                    .content(invoiceToJson(getInvoices().get(i)))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        }

        LocalDate start = LocalDate.of(2016, 2, 1);
        LocalDate end = LocalDate.of(2016, 12, 30);

        mockMvc
            .perform(get("/invoices/byDates?fromDate={fromDate}&toDate={toDate}", start, end)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    private String invoiceToJson(Invoice invoice) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

        return mapper.writeValueAsString(invoice);
    }

    private ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        invoices.add(new Invoice(null, "1", LocalDate.of(2019, 1, 1),
            company().get(0), company().get(1), Collections.singletonList(invoiceEntry().get(0))));
        invoices.add(new Invoice(null, "2", LocalDate.of(2017, 8, 2),
            company().get(1), company().get(2), Collections.singletonList(invoiceEntry().get(1))));
        invoices.add(new Invoice(2L, "3", LocalDate.of(2016, 3, 3),
            company().get(2), company().get(0), Collections.singletonList(invoiceEntry().get(2))));

        return invoices;
    }

    private ArrayList<Company> company() {
        ArrayList<Company> companies = new ArrayList<>();

        companies.add(new Company(1L, "It's Auto", "1-2-3"));
        companies.add(new Company(2L, "Auto World", "2-3-4"));
        companies.add(new Company(3L, "Black Red White", "3-4-5"));

        return companies;
    }

    private ArrayList<InvoiceEntry> invoiceEntry() {
        ArrayList<InvoiceEntry> invoiceEntries = new ArrayList<>();

        invoiceEntries.add(new InvoiceEntry(11L, "1", "Brakes", "1",
            BigDecimal.valueOf(350), VAT.VAT_8));
        invoiceEntries.add(new InvoiceEntry(22L, "2", "Table", "2",
            BigDecimal.valueOf(1500), VAT.VAT_5));
        invoiceEntries.add(new InvoiceEntry(33L, "3", "Audi Q5", "3",
            BigDecimal.valueOf(250), VAT.VAT_8));

        return invoiceEntries;
    }
}
