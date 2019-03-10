package pl.coderstrust.invoices.database.mongo;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.MongoInvoice;

@RunWith(MockitoJUnitRunner.class)
public class MongoDatabaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    MongoInvoiceRepository invoiceRepository;

    @InjectMocks
    MongoDatabase mongoDatabase;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldSaveNewInvoice() throws DatabaseOperationException {
        //given
        Invoice oldInvoice = new Invoice(13, "OldIssue", null, null, null, null);
        Invoice newInvoice = new Invoice(13, "NewIssue", null, null, null, null);
        MongoInvoice mongoInvoice = new MongoInvoice(newInvoice);

        when(invoiceRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(new MongoInvoice(oldInvoice)));
        when(invoiceRepository.save(mongoInvoice)).thenReturn(mongoInvoice);

        //when
        mongoDatabase.saveInvoice(newInvoice);

        //then
        verify(invoiceRepository).save(mongoInvoice);
    }

    @Test
    public void shouldThrowExceptionWhenSavingNull() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice must not be null.");

        //when
        mongoDatabase.saveInvoice(null);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingInvoiceWhichDoesNotExist() throws DatabaseOperationException {
        //given
        expectedException.expect(DatabaseOperationException.class);
        expectedException.expectMessage("Invoice Id=[15] doesn't exist.");
        when(invoiceRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Invoice invoice = new Invoice(15, "AnyIssue", null, null, null, null);

        //when
        mongoDatabase.saveInvoice(invoice);
    }

    @Test
    public void shouldDeleteInvoice() throws DatabaseOperationException {
        //given
        when(invoiceRepository.findById("15")).thenReturn(Optional.of(new MongoInvoice("15", null, null, null,null)));

        //when
        mongoDatabase.deleteInvoice("15");

        //then
        verify(invoiceRepository).deleteById("15");
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNullId() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice Id must not be null.");

        //when
        mongoDatabase.deleteInvoice(null);
    }

    @Test
    public void shouldGetInvoice() throws DatabaseOperationException {
        //given
        Invoice invoice = new Invoice(15, "AnyIssue", null, null, null, null);
        MongoInvoice mongoInvoice = new MongoInvoice(invoice);
        when(invoiceRepository.findById("15")).thenReturn(Optional.of(mongoInvoice));

        //when
        Invoice expected = new Invoice(mongoInvoice);
        Invoice actual = mongoDatabase.getInvoice("15");

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvoiceByDate() throws DatabaseOperationException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);
        LocalDate invoice1Date = LocalDate.of(2017, 3, 1);
        LocalDate invoice2Date = LocalDate.of(2019, 1, 31);
        Invoice invoice1 = new Invoice("3", null, invoice1Date, null, null, null);
        Invoice invoice2 = new Invoice("13", null, invoice2Date, null, null, null);
        when(invoiceRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new MongoInvoice(invoice1), new MongoInvoice(invoice2))));

        //when
        ArrayList<Invoice> expected = new ArrayList<>(Collections.singletonList(invoice1));
        ArrayList<Invoice> actual = new ArrayList<>(mongoDatabase.getInvoicesByDate(start, end));
        //then
        Assert.assertEquals(expected, actual);
    }
}
