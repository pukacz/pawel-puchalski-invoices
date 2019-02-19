package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Invoice", description = "invoice model")
public final class Invoice {

    @ApiModelProperty(value = "Unique ID of invoice", readOnly = true)
    @NotNull(message = "NotNull.Invoice.description")
    private Long id;

    @ApiModelProperty(value = "Place of invoice issue", readOnly = true)
    private String issue;

    @ApiModelProperty(value = "Date of invoice issue", readOnly = true)
    private LocalDate issueDate;

    @ApiModelProperty(value = "Model Company of seller", readOnly = true)
    private Company seller;

    @ApiModelProperty(value = "Model Company of buyer", readOnly = true)
    private Company buyer;

    @ApiModelProperty(value = "Model InvoiceEntry - selling items", readOnly = true)
    private List<InvoiceEntry> entries;

    public Invoice(String issue, LocalDate issueDate,
        Company seller, Company buyer, List<InvoiceEntry> entries) {
        this(0L, issue, issueDate, seller, buyer, entries);
    }

    public Invoice(Long id, String issue, LocalDate issueDate,
        Company seller, Company buyer, List<InvoiceEntry> entries) {
        this.id = id;
        this.issue = issue;
        this.issueDate = issueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public Invoice(Invoice invoice, Long invoiceId) {
        this.id = invoiceId;
        this.issue = invoice.getIssue();
        this.issueDate = invoice.getIssueDate();
        this.seller = invoice.getSeller();
        this.buyer = invoice.getBuyer();
        this.entries = invoice.getEntries();
    }

    public Invoice() {
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
