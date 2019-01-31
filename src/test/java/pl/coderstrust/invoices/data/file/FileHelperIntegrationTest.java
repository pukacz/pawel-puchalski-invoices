package pl.coderstrust.invoices.data.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import junitparams.JUnitParamsRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FileHelperIntegrationTest {

    String copiedFileForTestsPath = "localTestData" + "\\" + "invoicesTestCopy.dat";

    @BeforeClass
    public static void copyInvoicesTestFile() throws IOException {
        File originalFile = new File("localTestData" + "\\" + "invoicesTest.dat");
        File copiedFile = new File("localTestData" + "\\" + "invoicesTestCopy.dat");
        Files.copy(originalFile.toPath(), copiedFile.toPath());
    }

//    @AfterClass
//    public static void deleteCopiedInvoicesTestFile() throws IOException {
//        File copiedFile = new File("localTestData" + "\\" + "invoicesTestCopy.dat");
//        Files.delete(copiedFile.toPath());
//    }

    @Test
    public void shouldUpdateInvoice() throws IOException {

        try (RandomAccessFile file = new RandomAccessFile(copiedFileForTestsPath, "rw")) {
            FileHelper fileHelper = new FileHelper(file);

            fileHelper.saveInvoice(22L, "baseline information in 22 invoice");
            fileHelper.saveInvoice(11L, "baseline information in 11 invoice");
            fileHelper.saveInvoice(33L, "baseline information in 33 invoice");
            fileHelper.saveInvoice(22L, "1st update in 22 invoice");
            fileHelper.saveInvoice(22L, "2nd update in 22 invoice");
            fileHelper.saveInvoice(1L, "2nd update in 22 invoice");

            System.out.println(fileHelper.getInvoices());
        }
    }

//     new Object[]{
//        new Object[]{1L, "Invoice1"},
//            new Object[]{55L, "Invoice55"},
//            new Object[]{2L, "Invoice2"},
//            new Object[]{55L, "Invoice55update"},
//            new Object[]{2L, "Invoice2update"}
//    },
//        new Object[]{
//        new Object[]{1L, "Invoice1"},
//            new Object[]{55L, "Invoice55updated"},
//            new Object[]{2L, "Invoice2updated"},
//    }
}
