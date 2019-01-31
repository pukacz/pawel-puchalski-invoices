package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "InvoiceEntry", description = "invoice entry model")
public final class InvoiceEntry {

    @ApiModelProperty(value = "unique ID of invoice entry", readOnly = true)
    @NotNull(message = "NotNull.InvoiceEntry.description")
    @Size(min = 1, message = "Long value")
    private final Long id;

    @ApiModelProperty(value = "unit of product", readOnly = true)
    @Size(min = 1, max = 64)
    private final String unit;

    @ApiModelProperty(value = "product name", readOnly = true)
    @Size(min = 1, max = 64)
    private final String productName;

    @ApiModelProperty(value = "amount of product", readOnly = true)
    @Size(min = 1, max = 8)
    private final String amount;

    @ApiModelProperty(value = "price of product", readOnly = true)
    @Size(min = 1)
    private final BigDecimal price;

    @ApiModelProperty(value = "model of VAT", readOnly = true)
    private final VAT vat;

    public InvoiceEntry(Long id, String unit, String productName,
                        String amount, BigDecimal price, VAT vat) {
        this.id = id;
        this.unit = unit;
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.vat = vat;
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
