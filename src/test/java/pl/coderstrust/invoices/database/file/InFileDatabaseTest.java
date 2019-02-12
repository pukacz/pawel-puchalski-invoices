package pl.coderstrust.invoices.database.file;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    Configuration configuration;

    @Mock
    Converter converter;

    @Mock
    InvoiceFileAccessor fileAccessor;

    @Mock
    InvoiceIdCoordinator idCoordinator;

    @InjectMocks
    InFileDatabase inFileDatabase;

    private String separator = File.separator;

    @Test
    public void shouldSave_3_Delete_1_andReturn_2_Invoices() throws IOException {
        //given
        ArrayList<String> actual = new ArrayList<>();
        when(configuration.getInvoicesFile()).thenReturn(allInvoices());
        when(configuration.getInvoicesIdsFile()).thenReturn(allInvoicesIds());
        when(fileAccessor.getInvoiceFileLines()).thenReturn(actual);

        when(fileAccessor.saveLine
            (1L, converter.getJsonFromInvoice(getInvoices().get(0))))
            .thenReturn(actual.add(getIdsAndJsonInvoices().get(0)));
        when(fileAccessor
            .saveLine(2L, converter.getJsonFromInvoice(getInvoices().get(1))))
            .thenReturn(actual.add(getIdsAndJsonInvoices().get(1)));
        when(fileAccessor
            .saveLine(3L, converter.getJsonFromInvoice(getInvoices().get(2))))
            .thenReturn(actual.add(getIdsAndJsonInvoices().get(2)));

        when(fileAccessor.invalidateLine(2L)).thenReturn(actual.remove(1).isEmpty());

        //when
        inFileDatabase.saveInvoice(getInvoices().get(0));
        inFileDatabase.saveInvoice(getInvoices().get(1));
        inFileDatabase.saveInvoice(getInvoices().get(2));
        inFileDatabase.deleteInvoice(2L);

        List<String> expected = new ArrayList<>(
            asList(getIdsAndJsonInvoices().get(0), getIdsAndJsonInvoices().get(2)));

        //then
        Assert.assertThat(actual, Is.is(expected));
        Mockito.verify(fileAccessor, times(1)).invalidateLine(2L);
    }

    @Test
    public void shouldSave_3_invoices() throws IOException {
        //given
        TreeSet<Long> ids = new TreeSet<>();
        String line1 = getIdsAndJsonInvoices().get(0);
        String line2 = getIdsAndJsonInvoices().get(2);
        String line3 = getIdsAndJsonInvoices().get(4);

        when(configuration.getInvoicesFile()).thenReturn(_3Invoices());
        when(idCoordinator.getIds()).thenReturn(ids);
        when(fileAccessor.saveLine(1L, anyString()))
            .thenReturn(appendLineToFile(line1, _3Invoices()));
        when(fileAccessor.saveLine(3L, anyString()))
            .thenReturn(appendLineToFile(line2, _3Invoices()));
        when(fileAccessor.saveLine(5L, anyString()))
            .thenReturn(appendLineToFile(line3, _3Invoices()));
        when(idCoordinator.coordinateIds(anyLong())).thenReturn(ids.add(anyLong()));

        //when
        inFileDatabase.saveInvoice(getInvoices().get(0));
        inFileDatabase.saveInvoice(getInvoices().get(2));
        inFileDatabase.saveInvoice(getInvoices().get(4));

        ArrayList<Invoice> expected = new ArrayList<>(
            asList(getInvoices().get(0), getInvoices().get(2), getInvoices().get(2)));
        ArrayList<Invoice> actual = new ArrayList<>(inFileDatabase.getInvoices());

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnAllSavedInvoices() throws IOException {
        //given
        when(fileAccessor.getInvoiceFileLines()).thenReturn(getLinesFromFile(allInvoices()));

        //when
        ArrayList<Invoice> expected = new ArrayList<>(getInvoices());
        ArrayList<Invoice> actual = new ArrayList<>(inFileDatabase.getInvoices());

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvoicesBetween_01_December_2016_and_31_January_2018()
        throws IOException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);

        when(fileAccessor.getInvoiceFileLines())
            .thenReturn(getLinesFromFile(invoicesBetween_01_December_2016_and_31_January_2018()));

        //when
        ArrayList<Invoice> expected = new ArrayList<>(
            asList(getInvoices().get(1), getInvoices().get(4)));
        ArrayList<Invoice> actual = new ArrayList<>(
            inFileDatabase.getInvoicesByDate(start, end));

        //then
        Assert.assertEquals(expected, actual);
    }

    private boolean appendLineToFile(String line, File file) throws IOException {
        try (
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
        }
        return true;
    }

    private File _3Invoices() {
        return new File("src" + separator + "test" + separator
            + "resources" + separator + "inFileTestData" + separator
            + "3_invoices" + separator + "invoicesTestSave3.dat");
    }

    private File allInvoices() {
        return new File("src" + separator + "test" + separator
            + "resources" + separator + "inFileTestData" + separator
            + "allSavedInvoices" + separator + "invoicesTestAll.dat");
    }

    private File allInvoicesIds() {
        return new File("src" + separator + "test" + separator
            + "resources" + separator + "inFileTestData" + separator
            + "allSavedInvoices" + separator + "invoiceIdsTestAll.cor");
    }

    private File invoicesBetween_01_December_2016_and_31_January_2018() {
        return new File("src" + separator + "test" + separator
            + "resources" + separator + "inFileTestData" + separator
            + "invoicesBetween_01-12-16_31-01-18" + separator + "invoicesTestByDate.dat");
    }

    private ArrayList<String> getLinesFromFile(File file) throws IOException {
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    private ArrayList<String> getIdsAndJsonInvoices() throws JsonProcessingException {
        ArrayList<String> invoicesInJson = new ArrayList<>();
        String line;

        for (Invoice invoice : getInvoices()) {
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
