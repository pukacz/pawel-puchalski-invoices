package pl.coderstrust.invoices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final Long id;

    @ApiModelProperty(value = "Place of invoice issue", readOnly = true)
    private final String issue;

    @ApiModelProperty(value = "Date of invoice issue", readOnly = true)
    private final LocalDate issueDate;

    @ApiModelProperty(value = "Model Company of seller", readOnly = true)
    private final Company seller;

    @ApiModelProperty(value = "Model Company of buyer", readOnly = true)
    private final Company buyer;

    @ApiModelProperty(value = "Model InvoiceEntry - selling items", readOnly = true)
    private final List<InvoiceEntry> entries;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Invoice(@JsonProperty("id") Long id, @JsonProperty("issue") String issue,
        @JsonProperty("issueDate") LocalDate issueDate, @JsonProperty("seller") Company seller,
        @JsonProperty("buyer") Company buyer, @JsonProperty("entries") List<InvoiceEntry> entries) {
        this.id = id;
        this.issue = issue;
        this.issueDate = issueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public Invoice(Invoice invoice, Long id) {
        this.id = id;
        this.issue = invoice.getIssue();
        this.issueDate = invoice.getIssueDate();
        this.seller = invoice.getSeller();
        this.buyer = invoice.getBuyer();
        this.entries = invoice.getEntries();
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
