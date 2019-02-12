package pl.coderstrust.invoices.database.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import junitparams.JUnitParamsRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class InvoiceFileAccessorTest {

    @BeforeClass
    public static void createTempFile() throws IOException {
        File tempFile = new File("src/test/resources/inFileTestData/invoicesTestTemp.dat");
        tempFile.createNewFile();
    }

    @AfterClass
    public static void deleteCopiedInvoicesTestFile() throws IOException {
        File tempFile = new File("src/test/resources/inFileTestData/invoicesTestTemp.dat");
        Files.delete(tempFile.toPath());
    }

//    @Test
//    public void should_Add_Update_Delete_And_Return_Invoices_Or_Spaces_For_Superseded_Invoices()
//        throws IOException {
//        //given
//        File tempFile = new File("src/test/resources/inFileTestData/invoicesTestTemp.dat");
//
//        InvoiceFileAccessor accessor = new InvoiceFileAccessor(tempFile);
//        try (RandomAccessFile file = new RandomAccessFile(tempFile, "rw")) {
//
//            file.setLength(0);
//
//            //when
//            accessor.saveLine(1L, "1st invoice");
//            accessor.saveLine(1L, "1st invoice updated");
//            accessor.saveLine(2L, "2nd invoice");
//            accessor.saveLine(1L, "1st invoice 2nd update");
//            accessor.saveLine(3L, "3rd invoice");
//            accessor.saveLine(44L, "44th invoice");
//            accessor.invalidateLine(3L);
//            accessor.saveLine(15L, "15th invoice");
//
//            ArrayList<String> actual = accessor.getInvoiceFileLines();
//            ArrayList<String> expected = new ArrayList<>(
//                Arrays.asList("              ", "                      ",
//                    "2: 2nd invoice", "1: 1st invoice 2nd update",
//                    "              ", "44: 44th invoice",
//                    "15: 15th invoice"));
//
//            //then
//            Assert.assertEquals(actual, expected);
//        }
//    }

    //stworzyc rozbite testy na pojedyncze metody
}
