package pl.coderstrust.invoices.database.file;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.model.VAT;

@RunWith(MockitoJUnitRunner.class)
public class InFileDatabaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    InvoiceFileAccessor fileAccessor;

    @Mock
    Configuration config;

    @Mock
    InvoiceConverter invoiceConverter;

    @InjectMocks
    InFileDatabase inFileDatabase;

    private File testFile = new File("src/test/resources/inFileTestData/invoicesTest.dat");

    @Test
    public void shouldSave_3_Delete_1_andReturn_2_Invoices() throws IOException {
        //given
        ArrayList<String> actual = new ArrayList<>();
        when(config.getFile()).thenReturn(testFile);
        when(fileAccessor.getInvoiceFileLines()).thenReturn(actual);

        when(fileAccessor.saveLine
            (1L, invoiceConverter.getJsonFromInvoice(getInvoices().get(0))))
            .thenReturn(actual.add(getIdsAndJsonInvoices(getInvoices()).get(0)));
        when(fileAccessor
            .saveLine(2L, invoiceConverter.getJsonFromInvoice(getInvoices().get(1))))
            .thenReturn(actual.add(getIdsAndJsonInvoices(getInvoices()).get(1)));
        when(fileAccessor
            .saveLine(3L, invoiceConverter.getJsonFromInvoice(getInvoices().get(2))))
            .thenReturn(actual.add(getIdsAndJsonInvoices(getInvoices()).get(2)));

        when(fileAccessor
            .invalidateLine(2L))
            .thenReturn(actual.remove(1).isEmpty());

        //when
        List<String> expected = new ArrayList<>(
            asList(getIdsAndJsonInvoices(getInvoices()).get(0),
                getIdsAndJsonInvoices(getInvoices()).get(2)));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldReturnInvoicesBetween_01_December_2016_and_31_January_2018()
        throws IOException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);

        when(config.getFile()).thenReturn(testFile);
        when(fileAccessor.getInvoiceFileLines()).thenReturn(getIdsAndJsonInvoices(getInvoices()));

        //when
        ArrayList actual = new ArrayList<>(inFileDatabase.getInvoicesByDate(start, end));
        ArrayList expected = new ArrayList<>(asList(getInvoices().get(1), getInvoices().get(4)));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldReturnListOfInvoices() throws IOException {
        //given
        when(config.getFile()).thenReturn(testFile);
        when(fileAccessor.getInvoiceFileLines()).thenReturn(getIdsAndJsonInvoices(getInvoices()));

        System.out.println(inFileDatabase.getInvoices());

        //when
        ArrayList<Invoice> expected = new ArrayList<>(getInvoices());
        ArrayList<Invoice> actual = new ArrayList<>(inFileDatabase.getInvoices());

        //then
        Assert.assertEquals(expected, actual);
    }

    private File getTestFile() {
        return new File(
            "src" + File.separator + "test" + File.separator
                + "resources" + File.separator + "inFileTestData"
                + File.separator + "invoicesTest.dat");
    }

    private ArrayList<String> getIdsAndJsonInvoices(ArrayList<Invoice> invoices)
        throws JsonProcessingException {
        ArrayList<String> invoicesInJson = new ArrayList<>();
        String line;

        for (Invoice invoice : invoices) {
            line = "" + invoice.getId() + ": " + new ObjectMapper().writeValueAsString(invoice)
                + "\n";
            invoicesInJson.add(line);
        }
        return invoicesInJson;
    }

    private ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        invoices.add(new Invoice(1L, "1", LocalDate.of(2019, 1, 1),
            company().get(0), company().get(1), Collections.singletonList(invoiceEntry().get(0))));
        invoices.add(new Invoice(2L, "2", LocalDate.of(2017, 8, 2),
            company().get(1), company().get(2), Collections.singletonList(invoiceEntry().get(1))));
        invoices.add(new Invoice(3L, "3", LocalDate.of(2016, 3, 3),
            company().get(2), company().get(3), Collections.singletonList(invoiceEntry().get(2))));
        invoices.add(new Invoice(4L, "4", LocalDate.of(2015, 2, 2),
            company().get(3), company().get(4), Collections.singletonList(invoiceEntry().get(1))));
        invoices.add(new Invoice(5L, "5", LocalDate.of(2018, 1, 13),
            company().get(4), company().get(0), Collections.singletonList(invoiceEntry().get(2))));

        return invoices;
    }

    private ArrayList<Company> company() {
        ArrayList<Company> companies = new ArrayList<>();

        companies.add(new Company(1L, "It's Auto", "1-2-3"));
        companies.add(new Company(2L, "Auto World", "2-3-4"));
        companies.add(new Company(3L, "Black Red White", "3-4-5"));
        companies.add(new Company(4L, "Friendly Pharmacy", "4-5-6"));
        companies.add(new Company(5L, "Grocery", "5-6-7"));

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
        invoiceEntries.add(new InvoiceEntry(33L, "3", "Cleaning of Audi Q5", "3",
            BigDecimal.valueOf(250), VAT.VAT_8));

        return invoiceEntries;
    }
}
