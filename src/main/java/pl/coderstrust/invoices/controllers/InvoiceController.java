package pl.coderstrust.invoices.controllers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
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
    public Invoice getInvoiceByID(@PathVariable Long id) throws DatabaseOperationException {
        return invoiceService.getInvoiceByID(id);
    }

    @PostMapping("/add")
    public void addInvoice(@RequestParam(defaultValue = "0", required = false) Long id,
        @RequestParam String issue,
        @RequestParam LocalDate issueDate,
        @RequestParam Company seller,
        @RequestParam Company buyer,
        @RequestParam List<InvoiceEntry> entries) throws DatabaseOperationException {
        invoiceService.saveInvoice(id, issue, issueDate, seller, buyer, entries);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) throws DatabaseOperationException {
        invoiceService.deleteInvoice(id);
    }

}
