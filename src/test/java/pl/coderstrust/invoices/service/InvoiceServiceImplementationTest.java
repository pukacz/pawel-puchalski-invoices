package pl.coderstrust.invoices.service;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceImplementationTest {

    @Mock
    private Database database;
    private Invoice invoice;
    @InjectMocks
    private InvoiceServiceImplementation invoiceService;

    @Test
    public void testSaveInvoice() throws DatabaseOperationException {
        //when
        invoiceService.saveInvoice(invoice);

        //then
        verify(database).saveInvoice(invoice);
    }

}
