package pl.coderstrust.invoices.model;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceEntry {

    private String productName;
    private String amount;
    private BigDecimal price;
    private VAT vat;

    public InvoiceEntry(String productName, String amount, BigDecimal price, VAT vat) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.vat = vat;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public VAT getVat() {
        return vat;
    }

    public void setVat(VAT vat) {
        this.vat = vat;
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
