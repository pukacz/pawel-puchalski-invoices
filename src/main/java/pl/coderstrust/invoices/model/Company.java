package pl.coderstrust.invoices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "Company", description = "company model")
public final class Company {

    @ApiModelProperty(value = "unique ID of company", readOnly = true)
    @NotNull(message = "NotNull.Company.description")
    @Size(min = 1, message = "Long value")
    private final Long id;

    @ApiModelProperty(value = "name of company", readOnly = true)
    @Size(min = 1, max = 64)
    private final String name;

    @ApiModelProperty(value = "tax identification number, ???", readOnly = true)
    @Size(min = 1, max = 8)
    private final String taxIdentificationNumber;

    public Company(Long id, String name, String taxIdentificationNumber) {
        this.id = id;
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        return Objects.equals(id, company.id)
                && Objects.equals(name, company.name)
                && Objects.equals(taxIdentificationNumber, company.taxIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taxIdentificationNumber);
    }

    @Override
    public String toString() {
        return "Company{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", taxIdentificationNumber='" + taxIdentificationNumber + '\''
                + '}';
    }
}
