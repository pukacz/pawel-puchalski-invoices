package pl.coderstrust.invoices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Company", description = "company model")
@Entity
@Table(name = "company")
public final class Company {

    @ApiModelProperty(value = "Unique ID of company", readOnly = true)
    @NotNull(message = "NotNull.Company.description")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "Name of company", readOnly = true)
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "Tax identification number", readOnly = true)
    @Column(name = "tax_identification_number")
    private String taxIdentificationNumber;

    public Company() {
        this.id = null;
        this.name = null;
        this.taxIdentificationNumber = null;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Company(@JsonProperty("id") Long id, @JsonProperty("name") String name,
        @JsonProperty("taxIdentificationNumber") String taxIdentificationNumber) {
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
