package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

@ApiModel(value = "InvoiceEntry", description = "invoice entry model")
public final class InvoiceEntry {

    @ApiModelProperty(value = "Unique ID of invoice entry", readOnly = true)
    @NotNull(message = "NotNull.InvoiceEntry.description")
    private  Long id;

    @ApiModelProperty(value = "Unit of product", readOnly = true)
    private  String unit;

    @ApiModelProperty(value = "Product name", readOnly = true)
    private  String productName;

    @ApiModelProperty(value = "Amount of product", readOnly = true)
    private  String amount;

    @ApiModelProperty(value = "Price of product", readOnly = true)
    private  BigDecimal price;

    @ApiModelProperty(value = "Model of VAT", readOnly = true)
    private  VAT vat;

    public InvoiceEntry() {
    }

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
