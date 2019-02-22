package pl.coderstrust.invoices.service;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceImplementationTest {

    @Mock
    private Database database;

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
        //given
        LocalDate issueDate = LocalDate.of(2016, 12, 1);
        Company seller = new Company(22L, "BestSeller", "007");
        Company buyer = new Company(24L, "BestBuyer", "0,7");
        List<InvoiceEntry> entries = new ArrayList<>(Collections.emptyList());
        Invoice invoice = new Invoice(33L, "01", issueDate, seller, buyer, entries);

        //when
        invoiceService.saveInvoice(invoice);
        String actualSellerName = invoice.getSeller().getName();
        String actualTaxingId = invoice.getBuyer().getTaxIdentificationNumber();

        //then
        verify(database).saveInvoice(invoice);
        Assert.assertEquals("BestSeller", actualSellerName);
        Assert.assertEquals("0,7", actualTaxingId);
    }

    @Test
    public void testDeleteInvoice() throws DatabaseOperationException {
        //when
        invoiceService.deleteInvoice(30L);

        //then
        verify(database).deleteInvoice(30L);
    }
}
