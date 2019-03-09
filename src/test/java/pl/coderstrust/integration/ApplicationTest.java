package pl.coderstrust.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.coderstrust.invoices.controller.InvoiceController;
import pl.coderstrust.invoices.database.InvoiceJsonSerializer;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.model.VAT;

@TestPropertySource(properties = "spring.config.name=filedatabase")
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "file")
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

    private ArrayList<InvoiceEntry> invoiceEntry = new ArrayList<>(Arrays.asList(
        new InvoiceEntry(11L, "1", "Brakes", "1",
            BigDecimal.valueOf(350), VAT.VAT_8),
        new InvoiceEntry(22L, "2", "Table", "2",
            BigDecimal.valueOf(1500), VAT.VAT_5),
        new InvoiceEntry(33L, "3", "Audi Q5", "3",
            BigDecimal.valueOf(250), VAT.VAT_8)));

    private ArrayList<Company> company = new ArrayList<>(Arrays.asList(
        new Company(1L, "It's Auto", "1-2-3"),
        new Company(2L, "Auto World", "2-3-4"),
        new Company(3L, "Black Red White", "3-4-5")));

    private ArrayList<Invoice> invoices = new ArrayList<>(Arrays.asList(
        new Invoice(null, "1", LocalDate.of(2019, 1, 1),
            company.get(0), company.get(1), Collections.singletonList(invoiceEntry.get(0))),
        new Invoice(null, "2", LocalDate.of(2017, 8, 2),
            company.get(1), company.get(2), Collections.singletonList(invoiceEntry.get(1))),
        new Invoice(2L, "3", LocalDate.of(2016, 3, 3),
            company.get(2), company.get(0), Collections.singletonList(invoiceEntry.get(2)))));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceController invoiceController;

    @Autowired
    private InvoiceJsonSerializer jsonConverter;

    @Test
    public void contextLoads()  {
        assertThat(mockMvc).isNotNull();
        assertThat(invoiceController).isNotNull();
        assertThat(jsonConverter).isNotNull();
    }

    @BeforeClass
    public static void clearFilesBeforeAllTests() throws IOException {
        if (INVOICES_FILE.getParentFile().mkdirs()) {
            INVOICES_FILE.createNewFile();
            INVOICES_IDS_FILE.createNewFile();
        } else {
            if (INVOICES_IDS_FILE.exists()) {
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_IDS_FILE, "rw")) {
                    randomAccessFile.setLength(0);
                }
            } else {
                INVOICES_IDS_FILE.createNewFile();
            }

            if (INVOICES_IDS_FILE.exists()) {
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(INVOICES_IDS_FILE, "rw")) {
                    randomAccessFile.setLength(0);
                }
            } else {
                INVOICES_IDS_FILE.createNewFile();
            }
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
                .content(jsonConverter.getJsonFromInvoice(invoices.get(0)))
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
        MvcResult result = mockMvc
            .perform(post("/invoices/add")
                .content(jsonConverter.getJsonFromInvoice(invoices.get(0)))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn();

        String returnedContent = result.getResponse().getContentAsString();
        Long id = (Long)jsonConverter.getInvoiceFromJson(returnedContent).getId();
        Invoice invoiceForUpdate = new Invoice(invoices.get(2), id);

        mockMvc
            .perform(post("/invoices/add")
                .content(jsonConverter.getJsonFromInvoice(invoiceForUpdate))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/invoices"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(id.intValue())))
            .andExpect(jsonPath("$[0].issue", is("3")))
            .andExpect(jsonPath("$[0].issueDate", is("2016-03-03")))
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldDeleteInvoice() throws Exception {
        mockMvc
            .perform(post("/invoices/add")
                .content(jsonConverter.getJsonFromInvoice(invoices.get(0)))
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
                .content(jsonConverter.getJsonFromInvoice(invoices.get(1)))
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
                    .content(jsonConverter.getJsonFromInvoice(invoices.get(i)))
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
}
