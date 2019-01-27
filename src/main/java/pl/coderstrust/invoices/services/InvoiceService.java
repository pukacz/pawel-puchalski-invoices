package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

@Service
public class InvoiceService {

  public List<Invoice> getAllInvoices() {
    return new ArrayList<>();
  }

  public List<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate) {
    return new ArrayList<>();
  }

  public Invoice getInvoiceByID(Long id) {
    return null;
  }

  public void addInvoice(Long id, String issue, LocalDate issueDate, Company seller, Company buyer, List<InvoiceEntry> entries) {

  }

  public Invoice updateInvoice(Long id) {
    return null;
  }

  public void deleteInvoice(Long id) {

  }

}
