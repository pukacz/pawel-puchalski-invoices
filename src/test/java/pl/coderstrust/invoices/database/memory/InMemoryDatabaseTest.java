package pl.coderstrust.invoices.database.memory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.database.IdGenerator;
import pl.coderstrust.invoices.model.Invoice;

public class InMemoryDatabaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IdGenerator idGenerator = new IdGenerator();

    @Test
    public void shouldSaveTwoInvoices() throws DatabaseOperationException {
        //given
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        Invoice invoice1 = new Invoice(null, null, null, null, null, null);
        Invoice invoice2 = new Invoice(13L, null, null, null, null, null);
        //when
        inMemoryDatabase.saveInvoice(invoice1);
        inMemoryDatabase.saveInvoice(invoice2);
        //then
        Assert.assertEquals(2, inMemoryDatabase.getInvoices().size());
    }

    @Test
    public void shouldThrowExceptionWhenSavingNull() throws DatabaseOperationException {
        //given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invoice must not be null.");
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        //then
        inMemoryDatabase.saveInvoice(null);
    }

    @Test
    public void shouldDeleteInvoice() throws DatabaseOperationException {
        //given
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        Invoice invoice1 = new Invoice(null, null, null, null, null, null);
        Invoice invoice2 = new Invoice(13L, null, null, null, null, null);
        //when
        Long invoice1Id = (Long)inMemoryDatabase.saveInvoice(invoice1).getId();
        inMemoryDatabase.saveInvoice(invoice2);
        inMemoryDatabase.deleteInvoice(invoice1Id);
        //then
        Assert.assertEquals(1, inMemoryDatabase.getInvoices().size());
    }

    @Test
    public void shouldThrowExceptionWhenDeletingInvoiceWithWrongId() throws DatabaseOperationException {
        //given
        expectedException.expect(DatabaseOperationException.class);
        expectedException.expectMessage("Invoice id=[13] doesn't exist.");
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        //when
        inMemoryDatabase.deleteInvoice(13L);
    }

    @Test
    public void shouldReturnInvoice() throws DatabaseOperationException {
        //given
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        Invoice invoice1 = new Invoice(3L, null, null, null, null, null);
        Invoice invoice2 = new Invoice(13L, null, null, null, null, null);
        //when
        inMemoryDatabase.saveInvoice(invoice1);
        inMemoryDatabase.saveInvoice(invoice2);
        Invoice expected = inMemoryDatabase.getInvoice(3L);
        //then
        Assert.assertEquals(expected, invoice1);
    }

    @Test
    public void shouldReturnInvoiceByDate() throws DatabaseOperationException {
        //given
        LocalDate start = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2018, 1, 31);
        LocalDate invoice1Date = LocalDate.of(2017, 3, 1);
        LocalDate invoice2Date = LocalDate.of(2019, 1, 31);
        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase(idGenerator);
        Invoice invoice1 = new Invoice(3L, null, invoice1Date, null, null, null);
        Invoice invoice2 = new Invoice(13L, null, invoice2Date, null, null, null);
        //when
        inMemoryDatabase.saveInvoice(invoice1);
        inMemoryDatabase.saveInvoice(invoice2);
        ArrayList<Invoice> expected = new ArrayList<>(Collections.singletonList(invoice1));
        ArrayList<Invoice> actual = new ArrayList<>(inMemoryDatabase.getInvoicesByDate(start, end));
        //then
        Assert.assertEquals(expected, actual);
    }
}
