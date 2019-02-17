package pl.coderstrust.invoices.database.file;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;
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

@RunWith(MockitoJUnitRunner.class)
public class InvoiceIdCoordinatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Configuration configuration;


    @BeforeClass
    public static void createTempFile() throws IOException {
        invoicesIdsFile.createNewFile();
    }

    @AfterClass
    public static void deleteTempFile() {
        invoicesIdsFile.delete();
    }

    @Test
    public void shouldReturnInvoicesIds() throws IOException {
        //given
        when(configuration.getInvoicesIdsFile()).thenReturn(invoicesIdsFile);
        InvoiceIdCoordinator idCoordinator = new InvoiceIdCoordinator(configuration);

        //when
        idCoordinator.coordinateIds(2L);
        idCoordinator.coordinateIds(3L);
        idCoordinator.coordinateIds(2L);
        Collection<Long> actual = idCoordinator.getIds();
        TreeSet<Long> expected = new TreeSet<>(asList(2L, 3L));

        //then
        Assert.assertEquals(expected, actual);
    }

    private static File invoicesIdsFile = new File(folder() + "invoicesIdsTestFile.cor");

    private static String separator = File.separator;

    private static String folder() {
        return "src" + separator + "test" + separator + "resources" + separator + "inFileTestData"
            + separator;
    }
}
