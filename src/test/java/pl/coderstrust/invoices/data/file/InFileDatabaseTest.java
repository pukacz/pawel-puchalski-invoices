package pl.coderstrust.invoices.data.file;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.model.VAT;

@RunWith(MockitoJUnitRunner.class)
public class InFileDatabaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    FileHelper fileHelper;

    @Mock
    Configuration configuration;

    @Mock
    RandomAccessFile file;

    @InjectMocks
    InFileDatabase inFileDatabase;

    public static void main(String[] args) throws IOException {
        InFileDatabase inFileDatabase = new InFileDatabase();

        InFileDatabaseTest inFileDatabaseTest = new InFileDatabaseTest();

//        for (Invoice i : inFileDatabaseTest.getInvoices()) {
//            inFileDatabase.saveInvoice(i);
//        }
//
//        ArrayList<Invoice> list = new ArrayList<>(inFileDatabase.getInvoices());
//        for (Invoice i : list) {
//            System.out.println(i);
//        }
    }

    @Test
    public void shouldReturnListOfInvoices() throws IOException {
        //given
        String filePath = new File("localData").getAbsolutePath() + "\\" + "invoices.dat";
        when(configuration.getInvoicesFile()).thenReturn(filePath);
        when(fileHelper.getInvoices()).thenReturn(invoicesInJson());

        //when
        Invoice actual = getInvoices().get(0);
        Invoice expected = new ArrayList<>(inFileDatabase.getInvoices()).get(0);

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    private ArrayList<String> invoicesInJson() throws JsonProcessingException {
        ArrayList<Invoice> invoices = getInvoices();
        ArrayList<String> invoicesInJson = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (Invoice invoice : invoices) {
            String line = "" + invoice.getId() + ": " + invoice + "\n";
            invoicesInJson.add(mapper.writeValueAsString(line));
        }
        return invoicesInJson;
    }

    private ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        invoices.add(new Invoice(1L, "1", LocalDate.of(2019, 01, 01),
            company().get(0), company().get(1), Arrays.asList(invoiceEntry().get(0))));
        invoices.add(new Invoice(2L, "2", LocalDate.of(2019, 02, 02),
            company().get(1), company().get(2), Arrays.asList(invoiceEntry().get(1))));
        invoices.add(new Invoice(3L, "3", LocalDate.of(2019, 03, 03),
            company().get(2), company().get(3), Arrays.asList(invoiceEntry().get(2))));

        return invoices;
    }

    private ArrayList<Company> company() {
        ArrayList<Company> companies = new ArrayList<>();

        companies.add(new Company(1L, "It's Auto", "1-2-3"));
        companies.add(new Company(2L, "Auto World", "2-3-4"));
        companies.add(new Company(3L, "Black Red White", "3-4-5"));
        companies.add(new Company(4L, "Friendly Pharmacy", "3-4-5"));

        return companies;
    }

    private ArrayList<InvoiceEntry> invoiceEntry() {
        ArrayList<InvoiceEntry> invoiceEntries = new ArrayList<>();

        invoiceEntries.add(new InvoiceEntry(11L, "1", "Brakes", "1",
            BigDecimal.valueOf(350), VAT.VAT_8));
        invoiceEntries.add(new InvoiceEntry(22L, "2", "Table", "2",
            BigDecimal.valueOf(1500), VAT.VAT_5));
        invoiceEntries.add(new InvoiceEntry(33L, "3", "Cleaning of Audi Q5", "3",
            BigDecimal.valueOf(250), VAT.VAT_8));

        return invoiceEntries;
    }
}
