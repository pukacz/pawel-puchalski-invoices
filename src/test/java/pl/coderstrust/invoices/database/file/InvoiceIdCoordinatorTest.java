package pl.coderstrust.invoices.database.file;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import pl.coderstrust.invoices.database.Converter;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceIdCoordinatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Configuration configuration;

    private Converter converter;

    @BeforeClass
    public static void createTempFile() throws IOException {
        invoicesIdsFile().createNewFile();
    }

    @AfterClass
    public static void deleteTempFile() {
        invoicesIdsFile().delete();
    }

    @Test
    public void testReturnIds() throws IOException {
        //given
        when(configuration.getInvoicesIdsFilePath()).thenReturn(invoicesIdsFilePath);
        InvoiceIdCoordinator idCoordinator = new InvoiceIdCoordinator(configuration);

        //when
        idCoordinator.coordinateIds(2L);
        idCoordinator.coordinateIds(3L);
        idCoordinator.removeId(3L);
        idCoordinator.synchronizeData(asList(2L, 4L));

        //then
        Assert.assertTrue(idCoordinator.isDataSynchronized(asList(2L, 4L)));
        Assert.assertFalse(idCoordinator.isDataSynchronized(asList(2L, 3L)));
    }

    private static File invoicesIdsFile() {
        return new File(invoicesIdsFilePath);
    }

    private static String invoicesIdsFilePath = folder() + "invoicesIdsTestFile.cor";

    private static String separator = File.separator;

    private static String folder() {
        return "src" + separator + "test" + separator + "resources" + separator + "inFileTestData"
            + separator;
    }
}
