package pl.coderstrust.invoices.service;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
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
    public void testGetInvoices() throws DatabaseOperationException {
        //when
        invoiceService.getAllInvoices();

        //then
        verify(database).getInvoices();
    }

    @Test
    public void testGetAllOfRange() throws DatabaseOperationException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);

        //when
        invoiceService.getAllOfRange(start, end);

        //then
        verify(database).getInvoicesByDate(start, end);
    }

    @Test
    public void testGetInvoiceById() throws DatabaseOperationException {
        //when
        invoiceService.getInvoiceById(33L);

        //then
        verify(database).getInvoice(33L);
    }

    @Test
    public void testSaveInvoice() throws DatabaseOperationException {
        //when
        invoiceService.saveInvoice(invoice);

        //then
        verify(database).saveInvoice(invoice);
    }

    @Test
    public void testDeleteInvoice() throws DatabaseOperationException {
        //when
        invoiceService.deleteInvoice(30L);

        //then
        verify(database).deleteInvoice(30L);
    }
}
