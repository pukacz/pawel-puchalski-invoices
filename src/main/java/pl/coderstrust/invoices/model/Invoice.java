package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "Invoice", description = "invoice model")
public final class Invoice {

    @ApiModelProperty(value = "unique ID of invoice", readOnly = true)
    @NotNull(message = "NotNull.Invoice.description")
    @Size(min = 1, message = "Long value")
    private final Long id;

    @ApiModelProperty(value = "place of invoice issue", readOnly = true)
    @Size(min = 3, max = 64)
    private final String issue;

    @ApiModelProperty(value = "date of invoice issue", readOnly = true)
    private final LocalDate issueDate;

    @ApiModelProperty(value = "model Company of seller", readOnly = true)
    private final Company seller;

    @ApiModelProperty(value = "model Company of buyer", readOnly = true)
    private final Company buyer;

    @ApiModelProperty(value = "model InvoiceEntry - selling items", readOnly = true)
    private final List<InvoiceEntry> entries;

    public Invoice(Long id, String issue, LocalDate issueDate,
                   Company seller, Company buyer, List<InvoiceEntry> entries) {
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
