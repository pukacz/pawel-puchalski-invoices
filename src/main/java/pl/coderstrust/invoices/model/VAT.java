package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import java.math.BigDecimal;

@SuppressWarnings("checkstyle:abbreviationaswordinname")
@ApiModel(value = "VAT", description = "VAT model")
public enum VAT {

    VAT_23(BigDecimal.valueOf(0.23)),
    VAT_8(BigDecimal.valueOf(0.08)),
    VAT_5(BigDecimal.valueOf(0.05)),
    VAT_0(BigDecimal.ZERO);

    private BigDecimal value;

    VAT(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
