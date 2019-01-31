package pl.coderstrust.invoices.data.file;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @InjectMocks
    InFileDatabase inFileDatabase;

    @Test
    public void shouldSave3Delete1andReturn2Invoice() throws IOException {
        //given
        ArrayList<String> list = new ArrayList<>();
        String filePath = new File("localTestData").getAbsolutePath() + "\\" + "invoicesTest.dat";

        when(configuration.getInvoicesFilePath()).thenReturn(filePath);
        when(fileHelper.getInvoices()).thenReturn(list);

        when(fileHelper.saveInvoice(1L, invoicesInJson().get(0)))
            .thenReturn(list.add("" + 1L + invoicesInJson().get(0)));
        when(fileHelper.saveInvoice(2L, invoicesInJson().get(1)))
            .thenReturn(list.add("" + 2L + invoicesInJson().get(1)));
        when(fileHelper.saveInvoice(3L, invoicesInJson().get(2)))
            .thenReturn(list.add("" + 3L + invoicesInJson().get(2)));

        when(fileHelper.deleteInvoice(2L)).thenReturn(Boolean.valueOf(list.remove(1)));

        //when
        inFileDatabase.saveInvoice(getInvoices().get(0));
        inFileDatabase.saveInvoice(getInvoices().get(1));
        inFileDatabase.saveInvoice(getInvoices().get(2));
        inFileDatabase.deleteInvoice(2L);

        List<String> actual = list;
        List<String> expected = new ArrayList<>(
            Arrays.asList("" + 1L + invoicesInJson().get(0), "" + 3L + invoicesInJson().get(2)));

        //then
        Assert.assertEquals(list.size(), 2);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldReturnListOfInvoicesBetween_01_December_2016_and_31_January_2018()
        throws IOException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);

        String filePath = new File("localTestData").getAbsolutePath() + "\\" + "invoicesTest.dat";

        when(configuration.getInvoicesFilePath()).thenReturn(filePath);
        when(fileHelper.getInvoices()).thenReturn(invoicesInJson());

        //when
        ArrayList actual = new ArrayList<>(inFileDatabase.getInvoicesByDate(start, end));
        ArrayList expected = new ArrayList<>(Arrays.asList(
            getInvoices().get(1), getInvoices().get(4)));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldReturnListOfInvoices() throws IOException {
        //given
        String filePath = new File("localTestData").getAbsolutePath() + "\\" + "invoicesTest.dat";

        when(configuration.getInvoicesFilePath()).thenReturn(filePath);
        when(fileHelper.getInvoices()).thenReturn(invoicesInJson());

        //when
        ArrayList actual = new ArrayList<>(inFileDatabase.getInvoices());
        ArrayList expected = new ArrayList<>(getInvoices());

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

        invoices.add(new Invoice(1L, "1", LocalDate.of(2019, 1, 1),
            company().get(0), company().get(1), Arrays.asList(invoiceEntry().get(0))));
        invoices.add(new Invoice(2L, "2", LocalDate.of(2017, 8, 2),
            company().get(1), company().get(2), Arrays.asList(invoiceEntry().get(1))));
        invoices.add(new Invoice(3L, "3", LocalDate.of(2016, 3, 3),
            company().get(2), company().get(3), Arrays.asList(invoiceEntry().get(2))));
        invoices.add(new Invoice(4L, "4", LocalDate.of(2015, 2, 2),
            company().get(3), company().get(4), Arrays.asList(invoiceEntry().get(1))));
        invoices.add(new Invoice(5L, "5", LocalDate.of(2018, 1, 13),
            company().get(4), company().get(0), Arrays.asList(invoiceEntry().get(2))));

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
