package pl.coderstrust.invoices.database.file;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.model.VAT;

@RunWith(MockitoJUnitRunner.class)
public class InFileDatabaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    InvoiceFileAccessor fileAccessor;

    @Mock
    InvoiceIdCoordinator idCoordinator;

    @Mock
    IdGenerator idGenerator;

    @InjectMocks
    InFileDatabase inFileDatabase;

    @BeforeClass
    public static void createTestFile() throws IOException {
        fileFor1Invoice().createNewFile();
    }

    @AfterClass
    public static void deleteTestFile() {
        fileFor1Invoice().delete();
    }

    @Test
    public void shouldSave1AndReturn1invoice() throws IOException, DatabaseOperationException {
        //given
        Invoice invoice = getInvoices().get(2);
        String invoiceInJson = new Converter().getJsonFromInvoice(invoice);
        String line = invoice.getId() + ": " + invoiceInJson;

        //when
        when(idCoordinator.getIds()).thenReturn(new TreeSet<>(asList(1L, 2L, 3L, 4L)));
        when(fileAccessor.invalidateLine(3L)).thenReturn(true);
        inFileDatabase.saveInvoice(getInvoices().get(2));

        //then
        verify(idCoordinator, times(1)).getIds();
        verify(fileAccessor, times(1)).invalidateLine(3L);
        verify(fileAccessor, times(1)).saveLine(line);
        verify(idCoordinator, times(1)).coordinateIds(3L);
    }

    @Test
    public void shouldReturnAllInvoicesFromFile() throws IOException, DatabaseOperationException {
        //given
        when(fileAccessor.readLines()).thenReturn(getLinesFromFile(allInvoices()));

        //when
        ArrayList<Invoice> expected = new ArrayList<>(getInvoices());
        ArrayList<Invoice> actual = new ArrayList<>(inFileDatabase.getInvoices());

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvoicesFrom_01_December_2016_to_31_January_2018()
        throws IOException, DatabaseOperationException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);

        when(fileAccessor.readLines()).thenReturn(getLinesFromFile(allInvoices()));

        //when
        ArrayList<Invoice> expected = new ArrayList<>(
            asList(getInvoices().get(1), getInvoices().get(4)));
        ArrayList<Invoice> actual = new ArrayList<>(inFileDatabase.getInvoicesByDate(start, end));

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testForSynchronizeDbFiles() throws DatabaseOperationException, IOException {
        //given
        when(idCoordinator.isDataSynchronized(inFileDatabase.getIdsFromDataFile()))
            .thenReturn(false);

        //when
        inFileDatabase.synchronizeDbFiles();

        //then
        verify(idCoordinator, times(1)).isDataSynchronized(inFileDatabase.getIdsFromDataFile());
        verify(idCoordinator, times(1)).synchronizeData(inFileDatabase.getIdsFromDataFile());
    }

    @Test
    public void shouldGenerateNewIdForInvoice() throws IOException, DatabaseOperationException {
        //given
        ArrayList <Long> list = new ArrayList(asList(1L, 2L));
        Invoice invoice = new Invoice(null, "defaultID", null, null, null, null);
        when(idCoordinator.getIds()).thenReturn(list);

        //when
        inFileDatabase.saveInvoice(invoice);

        //then
        verify(idGenerator,times(1)).generateId(list);
    }

    @Test
    public void shouldReturnIdsFromDataFile() throws IOException, DatabaseOperationException {
        //given
        when(fileAccessor.readLines()).thenReturn(getLinesFromFile(allInvoices()));

        //when
        ArrayList actual = inFileDatabase.getIdsFromDataFile();
        ArrayList expected = new ArrayList(asList(1L, 2L, 3L, 4L, 5L));

        //then
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSavingNull() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice must not be null.");

        //then
        inFileDatabase.saveInvoice(null);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenDeletingInvoiceWithNullId() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice Id must not be null.");

        //then
        inFileDatabase.deleteInvoice(null);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenGettingInvoiceWithNullId() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice Id must not be null.");

        //then
        inFileDatabase.getInvoice(null);
    }

    @Test
    public void shouldThrowDatabaseOperationExceptionWhenIdIsNotRecognizedInCoordinationFile()
        throws IOException, DatabaseOperationException {
        //given
        when(idCoordinator.getIds()).thenReturn(new TreeSet<>(asList(1L, 2L)));
        expectedException.expect(DatabaseOperationException.class);
        expectedException.expectMessage("Invoice id=[5] doesn't exist.");

        //then
        inFileDatabase.getInvoice(5L);
    }

    @Test
    public void shouldThrowDatabaseOperationExceptionWhenInvoiceIsNotExisting()
        throws IOException, DatabaseOperationException {
        //given
        when(idCoordinator.getIds()).thenReturn(new TreeSet<>(asList(1L, 2L, 15L)));
        when(fileAccessor.readLines()).thenReturn(getLinesFromFile(allInvoices()));
        expectedException.expect(DatabaseOperationException.class);
        expectedException.expectMessage("You are trying to read/update invoice which is "
            + "not recognized in coordination file. Please synchronize database files first.");

        //then
        inFileDatabase.getInvoice(15L);
    }

    @Test
    public void shouldThrowDatabaseOperationExceptionWhenSaving()
        throws IOException, DatabaseOperationException {
        //given
        expectedException.expect(DatabaseOperationException.class);
        expectedException.expectMessage("You are trying to read/update invoice which is not "
            + "recognized in coordination file. Please synchronize database files first.");
        Invoice invoice = new Invoice(3L, null, null, null, null, null);
        when(idCoordinator.getIds()).thenReturn(new ArrayList<>(Collections.singletonList(2L)));

        //when
        inFileDatabase.saveInvoice(invoice);
    }

    @Test
    public void shouldThrowDatabaseOperationExceptionWhenDatesAreWrong()
        throws DatabaseOperationException {
        //given
        LocalDate start = LocalDate.of(2018, 1, 31);
        LocalDate end = LocalDate.of(2016, 12, 1);
        expectedException.expect(DatabaseOperationException.class);
        expectedException
            .expectMessage("Start date [" + start + "] is after end date [" + end + "].");

        //when
        inFileDatabase.getInvoicesByDate(start, end);
    }


    private static File fileFor1Invoice() {
        return new File(testFolder() + "invoicesTestSave1.dat");
    }

    private File allInvoices() {
        return new File(testFolder() + "invoicesTestAll.dat");
    }

    private static String testFolder() {
        return "src" + separator + "test" + separator
            + "resources" + separator + "inFileTestData" + separator;
    }

    private static String separator = File.separator;

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
