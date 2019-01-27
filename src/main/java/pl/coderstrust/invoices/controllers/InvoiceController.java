package pl.coderstrust.invoices.controllers;

import java.time.LocalDate;
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
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return invoiceService.getAllInvoices();
    }

    @RequestMapping("/byDates")
    public Collection<Invoice> getAllofRange(@RequestParam(value = "fromDate") LocalDate fromDate,
        @RequestParam(value = "toDate") LocalDate toDate) throws DatabaseOperationException {
        return invoiceService.getAllofRange(fromDate, toDate);
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) throws DatabaseOperationException {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping("/add")
    public void addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        invoiceService.saveInvoice(invoice);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) throws DatabaseOperationException {
        invoiceService.deleteInvoice(id);
    }

}
