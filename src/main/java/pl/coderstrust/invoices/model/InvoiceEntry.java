package pl.coderstrust.invoices.model;

import java.math.BigDecimal;
import java.util.Objects;

final public class InvoiceEntry {

    final private Long id;
    final private String unit;
    final private String productName;
    final private String amount;
    final private BigDecimal price;
    final private VAT vat;

    public InvoiceEntry(Long id, String unit, String productName, String amount, BigDecimal price, VAT vat) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceEntry that = (InvoiceEntry) o;
        return Objects.equals(productName, that.productName) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(price, that.price) &&
                vat == that.vat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, amount, price, vat);
    }
}
