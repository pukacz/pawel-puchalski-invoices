package pl.coderstrust.invoices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel(value = "InvoiceEntry", description = "invoice entry model")
@Entity
@Table(name = "invoiceentry")
public final class InvoiceEntry {

    @ApiModelProperty(value = "Unique ID of invoice entry", readOnly = true)
    @NotNull(message = "NotNull.InvoiceEntry.description")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "Unit of product", readOnly = true)
    @Column(name = "unit")
    private String unit;

    @ApiModelProperty(value = "Product name", readOnly = true)
    @Column(name = "product_name")
    private String productName;

    @ApiModelProperty(value = "Amount of product", readOnly = true)
    @Column(name = "amount")
    private String amount;

    @ApiModelProperty(value = "Price of product", readOnly = true)
    @Column(name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "Model of VAT", readOnly = true)
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "vat")
    private VAT vat;

    public InvoiceEntry() {
        this.id = null;
        this.unit = null;
        this.productName = null;
        this.amount = null;
        this.price = null;
        this.vat = null;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InvoiceEntry(@JsonProperty("id") Long id, @JsonProperty("unit") String unit,
        @JsonProperty("productName") String productName, @JsonProperty("amount") String amount,
        @JsonProperty("price") BigDecimal price, @JsonProperty("vat") VAT vat) {
        this.id = id;
        this.unit = unit;
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.vat = vat;
    }

    public Long getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public String getProductName() {
        return productName;
    }

    public String getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public VAT getVat() {
        return vat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceEntry that = (InvoiceEntry) o;
        return Objects.equals(id, that.id)
            && Objects.equals(unit, that.unit)
            && Objects.equals(productName, that.productName)
            && Objects.equals(amount, that.amount)
            && Objects.equals(price, that.price)
            && vat == that.vat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unit, productName, amount, price, vat);
    }

    @Override
    public String toString() {
        return "InvoiceEntry{"
            + "id=" + id
            + ", unit='" + unit + '\''
            + ", productName='" + productName + '\''
            + ", amount='" + amount + '\''
            + ", price=" + price
            + ", vat=" + vat
            + '}';
    }
}
