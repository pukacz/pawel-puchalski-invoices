package pl.coderstrust.invoices.data.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import junitparams.JUnitParamsRunner;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FileHelperTest {

    @BeforeClass
    public static void createTempFile() throws IOException {
        File tempFile = new File("localTestData" + "\\" + "invoicesTestTemp.dat");
        tempFile.createNewFile();
    }

    @AfterClass
    public static void deleteCopiedInvoicesTestFile() throws IOException {
        File tempFile = new File("localTestData" + "\\" + "invoicesTestTemp.dat");
        Files.delete(tempFile.toPath());
    }

    @Test
    public void should_Add_Update_Delete_And_Return_Invoices_Or_Spaces_For_Superseded_Invoices()
        throws IOException {
        //given
        String tempFile = "localTestData" + "\\" + "invoicesTestTemp.dat";

        try (RandomAccessFile file = new RandomAccessFile(tempFile, "rw")) {
            FileHelper fileHelper = new FileHelper(file);
            file.setLength(0);

            //when
            fileHelper.saveInvoice(1L, "1st invoice");
            fileHelper.saveInvoice(1L, "1st invoice updated");
            fileHelper.saveInvoice(2L, "2nd invoice");
            fileHelper.saveInvoice(1L, "1st invoice 2nd update");
            fileHelper.saveInvoice(3L, "3rd invoice");
            fileHelper.saveInvoice(44L, "44th invoice");
            fileHelper.deleteInvoice(3L);
            fileHelper.saveInvoice(15L, "15th invoice");

            ArrayList<String> actual = fileHelper.getInvoices();
            ArrayList<String> expected = new ArrayList<>(
                Arrays.asList("              ", "                      ",
                    "2: 2nd invoice", "1: 1st invoice 2nd update",
                    "              ", "44: 44th invoice",
                    "15: 15th invoice"));

            //then
            Assert.assertEquals(actual, expected);
        }
    }
}
