package pl.coderstrust.invoices.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.StandardInvoice;
import pl.coderstrust.invoices.service.InvoiceService;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    @ApiOperation(value = "Show all invoices", notes = "Retrieving all invoices", response = StandardInvoice[].class)
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/byDates")
    @ApiOperation(value = "Find invoices from the time range", notes = "Retrieving time range (from date - to date)",
        response = StandardInvoice[].class)
    public Collection<Invoice> getAllOfRange(
        @ApiParam @RequestParam(value = "fromDate") String fromDate,
        @ApiParam @RequestParam(value = "toDate") String toDate) throws DatabaseOperationException {
        LocalDate startDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(fromDate));
        LocalDate endDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(toDate));
        return invoiceService.getAllOfRange(startDate, endDate);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID", notes = "Retrieving the invoice of ID (Long number)", response = StandardInvoice.class)
    public Invoice getInvoiceById(@PathVariable Object id) throws DatabaseOperationException {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add or update invoice",
        notes = "Retrieving JSON body of new invoice (ID = 0), or invoice to update", response = StandardInvoice.class)
    public Invoice addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        return invoiceService.saveInvoice(invoice);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Find invoice by its unique ID and delete it", notes = "Retrieving the invoice of ID (Long number)")
    public void deleteInvoice(@PathVariable Object id) throws DatabaseOperationException {
        invoiceService.deleteInvoice(id);
    }
}
