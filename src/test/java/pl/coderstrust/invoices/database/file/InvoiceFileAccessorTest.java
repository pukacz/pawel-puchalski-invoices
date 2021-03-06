package pl.coderstrust.invoices.database.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import junitparams.JUnitParamsRunner;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class InvoiceFileAccessorTest {

    private static InvoiceFileAccessor fileAccessor;

    private static String invoicesFilePath = folder() + "invoicesTestTemp.dat";
    private static String invoicesIdsFilePath = folder() + "invoicesIdsTestTemp.cor";

    private static String separator = File.separator;

    private static String folder() {
        return "src" + separator + "test" + separator + "resources" + separator + "inFileTestData"
            + separator;
    }

    private static File invoicesFile() {
        return new File(invoicesFilePath);
    }

    private static File invoicesIdsFile() {
        return new File(invoicesIdsFilePath);
    }

    @Before
    public void createTempFileAndSave3Invoices() throws IOException {
        invoicesFile().createNewFile();
        invoicesIdsFile().createNewFile();
        Configuration configuration = new Configuration(invoicesFilePath, invoicesIdsFilePath);
        fileAccessor = new InvoiceFileAccessor(configuration);

        fileAccessor.saveLine("1: 1st invoice");
        fileAccessor.saveLine("2: 2nd invoice");
        fileAccessor.saveLine("3: 3rd invoice");
    }

    @After
    public void deleteTestFile()  {
        invoicesFile().delete();
        invoicesIdsFile().delete();
    }


    @Test
    public void shouldReturn3invoices() throws IOException {
        //when
        ArrayList<String> actual = fileAccessor.readLines();
        ArrayList<String> expected = new ArrayList<>(
            Arrays.asList("1: 1st invoice", "2: 2nd invoice", "3: 3rd invoice"));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldDelete1invoice() throws IOException {
        //when
        fileAccessor.invalidateLine(2L);

        ArrayList<String> actual = fileAccessor.readLines();
        ArrayList<String> expected = new ArrayList<>(
            Arrays.asList("1: 1st invoice", "              ", "3: 3rd invoice"));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldGetPositionOfInvoicesInFile() throws IOException {
        //when
        ArrayList<Long> expected = new ArrayList<>(Arrays.asList(0L, 15L, 30L));
        ArrayList<Long> actual = new ArrayList<>();
        actual.add(fileAccessor.getPositionOfInvoice(1L));
        actual.add(fileAccessor.getPositionOfInvoice(2L));
        actual.add(fileAccessor.getPositionOfInvoice(3L));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void should_Add_Update_Delete_And_Return_Invoices_Or_Spaces_For_Superseded_Invoices()
        throws IOException {
        //when
        fileAccessor.saveLine("1: 1st invoice 1st update");
        fileAccessor.saveLine("2: 2nd invoice 1st update");
        fileAccessor.saveLine("1: 1st invoice 2nd update");
        fileAccessor.saveLine("3: 3rd invoice 1st update");
        fileAccessor.saveLine("44: 44th invoice");
        fileAccessor.invalidateLine(3L);

        ArrayList<String> actual = fileAccessor.readLines();
        ArrayList<String> expected = new ArrayList<>(
            Arrays.asList("1: 1st invoice", "2: 2nd invoice", "              ",
                "1: 1st invoice 1st update", "2: 2nd invoice 1st update",
                "1: 1st invoice 2nd update", "3: 3rd invoice 1st update", "44: 44th invoice"));
        //then
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldInvalidate2Lines() throws IOException {
        //when
        fileAccessor.invalidateLine(1L);
        fileAccessor.invalidateLine(3L);
        ArrayList<String> actual = fileAccessor.readLines();
        ArrayList<String> expected = new ArrayList<>(
            Arrays.asList("              ", "2: 2nd invoice", "              "));

        //then
        Assert.assertThat(actual, Is.is(expected));
    }
}
