package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@ApiModel(value = "MongoInvoice", description = "mongo invoice model")
public final class MongoInvoice {

    @Id
    private String id;

    private String issue;

    private LocalDate issueDate;

    private Company seller;

    private Company buyer;

    private List<InvoiceEntry> entries;

    public String getId() {
        return id;
    }

    String getIssue() {
        return issue;
    }

    LocalDate getIssueDate() {
        return issueDate;
    }

    Company getSeller() {
        return seller;
    }

    Company getBuyer() {
        return buyer;
    }

    List<InvoiceEntry> getEntries() {
        return entries;
    }

    public MongoInvoice(String issue, LocalDate issueDate, Company seller, Company buyer,
        List<InvoiceEntry> entries) {
        this.issue = issue;
        this.issueDate = issueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries;
    }

    public MongoInvoice(Invoice invoice) {
        this(invoice.getIssue(), invoice.getIssueDate(), invoice.getSeller(), invoice.getBuyer(),
            invoice.getEntries());
        id = getIdFromObject(invoice.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MongoInvoice that = (MongoInvoice) o;
        return Objects.equals(id, that.id)
            && Objects.equals(issue, that.issue)
            && Objects.equals(issueDate, that.issueDate)
            && Objects.equals(seller, that.seller)
            && Objects.equals(buyer, that.buyer)
            && Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issue, issueDate, seller, buyer, entries);
    }

    @Override
    public String toString() {
        return "MongoInvoice{"
            + "id='" + id + '\''
            + ", issue='" + issue + '\''
            + ", issueDate=" + issueDate
            + ", seller=" + seller
            + ", buyer=" + buyer
            + ", entries=" + entries
            + '}';
    }

    private String getIdFromObject(Object id) {
        if (id == null) {
            return null;
        }

        if (!(id instanceof String) && !(id instanceof Integer) && !(id instanceof Long)) {
            throw new IllegalArgumentException("Argument Id must be String type.");
        }

        if ((id instanceof Integer) || (id instanceof Long)) {
            return String.valueOf(id);
        }
        return (String) id;
    }
}
