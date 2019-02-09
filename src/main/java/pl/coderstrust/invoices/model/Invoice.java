package pl.coderstrust.invoices.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class Invoice {

    @ApiModelProperty(value = "Unique ID of invoice", readOnly = true)
    @NotNull(message = "NotNull.Invoice.description")
    private  Long id;

    @ApiModelProperty(value = "Place of invoice issue", readOnly = true)
    private  String issue;

    @ApiModelProperty(value = "Date of invoice issue", readOnly = true)
    private  LocalDate issueDate;

    @ApiModelProperty(value = "Model Company of seller", readOnly = true)
    private  Company seller;

    @ApiModelProperty(value = "Model Company of buyer", readOnly = true)
    private  Company buyer;

    @ApiModelProperty(value = "Model InvoiceEntry - selling items", readOnly = true)
    private  List<InvoiceEntry> entries;

    public Invoice() {
    }

    public Invoice(String issue, LocalDate issueDate,
        Company seller, Company buyer, List<InvoiceEntry> entries) {
        this.issue = issue;
        this.issueDate = issueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public Invoice(Long id, String issue, LocalDate issueDate,
        Company seller, Company buyer, List<InvoiceEntry> entries) {
        this(issue, issueDate, seller, buyer, entries);
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id)
            && Objects.equals(issue, invoice.issue)
            && Objects.equals(issueDate, invoice.issueDate)
            && Objects.equals(seller, invoice.seller)
            && Objects.equals(buyer, invoice.buyer)
            && Objects.equals(entries, invoice.entries);
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
