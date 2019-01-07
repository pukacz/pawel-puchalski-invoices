package pl.coderstrust.invoices.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

final public class Invoice {

    final private Long id;
    final private String issue;
    final private LocalDate issueDate;
    final private Company seller;
    final private Company buyer;
    final private List<InvoiceEntry> entries;

    public Invoice(Long id, String issue, LocalDate issueDate, Company seller, Company buyer, List<InvoiceEntry> entries) {
        this.id = id;
        this.issue = issue;
        this.issueDate = issueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public String getIssue() {
        return issue;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Company getSeller() {
        return seller;
    }

    public Company getBuyer() {
        return buyer;
    }

    public List<InvoiceEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) &&
                Objects.equals(issue, invoice.issue) &&
                Objects.equals(issueDate, invoice.issueDate) &&
                Objects.equals(seller, invoice.seller) &&
                Objects.equals(buyer, invoice.buyer) &&
                Objects.equals(entries, invoice.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issue, issueDate, seller, buyer, entries);
    }

    @Override
    public String toString() {
        return "Invoice{"
                + "id=" + id
                + ", issue='" + issue + '\''
                + ", issueDate=" + issueDate
                + ", seller=" + seller
                + ", buyer=" + buyer
                + ", entries=" + entries
                + '}';
    }
}