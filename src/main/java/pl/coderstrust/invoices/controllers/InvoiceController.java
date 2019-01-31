package pl.coderstrust.invoices.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import javax.validation.Valid;
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
import pl.coderstrust.invoices.services.InvoiceService;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RequestMapping("")
    @ApiOperation(value = "show all invoices", notes = "nothing to retreving", response = Invoice[].class)
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return invoiceService.getAllInvoices();
    }

    @RequestMapping("/byDates")
    @ApiOperation(value = "find invoices from the time range", notes = "retreving time range (from date - to date)", response = Invoice[].class)
    public Collection<Invoice> getAllofRange(@ApiParam @RequestParam(value = "fromDate") String fromDate,
        @ApiParam @RequestParam(value = "toDate") String toDate) throws DatabaseOperationException {
        LocalDate startDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(fromDate));
        LocalDate endDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(toDate));
        return invoiceService.getAllofRange(startDate, endDate);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "find invoice by its unique ID", notes = "retreving the invoice of ID (Long number)", response = Invoice.class)
    public Invoice getInvoiceById(@ApiParam @Valid @PathVariable Long id) throws DatabaseOperationException {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "add or update invoice", notes = "retreving JSON body of new invoice (ID = 0), or invoice to update")
    public void addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        invoiceService.saveInvoice(invoice);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "find invoice by its unique ID and delete it", notes = "retreving the invoice of ID (Long number)")
    public void deleteInvoice(@ApiParam @Valid @PathVariable Long id) throws DatabaseOperationException {
        invoiceService.deleteInvoice(id);
    }

}
