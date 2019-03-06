package pl.coderstrust.invoices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Invoice", description = "invoice model")
@Entity
@Table(name = "invoices")
public final class Invoice {

    @ApiModelProperty(value = "Unique ID of invoice", readOnly = true)
    @NotNull(message = "NotNull.Invoice.description")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "Place of invoice issue", readOnly = true)
    @Column(name = "issue")
    private String issue;

    @ApiModelProperty(value = "Date of invoice issue", readOnly = true)
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @ApiModelProperty(value = "Model Company of seller", readOnly = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "seller_id")
    private Company seller;

    @ApiModelProperty(value = "Model Company of buyer", readOnly = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "buyer_id")
    private Company buyer;

    @ApiModelProperty(value = "Model InvoiceEntry - selling items", readOnly = true)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "entries",
            joinColumns = @JoinColumn(name = "entries_id"),
            inverseJoinColumns = @JoinColumn(name = "invoice_id"))
    private List<InvoiceEntry> entries;

    public Invoice() {
        this.id = null;
        this.issue = null;
        this.issueDate = null;
        this.seller = null;
        this.buyer = null;
        this.entries = null;
    }

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
