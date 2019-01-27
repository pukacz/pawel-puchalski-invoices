package pl.coderstrust.invoices.controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;
import pl.coderstrust.invoices.services.InvoiceService;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @RequestMapping("")
  public List<Invoice> getAllInvoices() {
    return invoiceService.getAllInvoices();
  }

  @RequestMapping("/getAllofRange")
  public List<Invoice> getAllofRange(@RequestParam(value = "fromDate") LocalDate fromDate, @RequestParam(value = "toDate") LocalDate toDate) {
    return invoiceService.getAllofRange(fromDate, toDate);
  }

  @GetMapping("/(id)")
  public Invoice getInvoiceByID(@PathVariable Long id) {
    return invoiceService.getInvoiceByID(id);
  }

  @PostMapping("/add")
  public void addInvoice(@RequestParam Long id, @RequestParam String issue, @RequestParam LocalDate issueDate, @RequestParam Company seller, @RequestParam Company buyer, @RequestParam List<InvoiceEntry> entries) {
    invoiceService.addInvoice(id, issue, issueDate, seller, buyer, entries);
  }

  @RequestMapping("/update/(id)")
  public void updateInvoice(@PathVariable Long id) {
    invoiceService.updateInvoice(id);
  }

  @DeleteMapping("/(id)")
  public void deleteInvoice(@PathVariable Long id) {
    invoiceService.deleteInvoice(id);
  }

}
