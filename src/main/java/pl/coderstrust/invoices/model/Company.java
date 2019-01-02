package pl.coderstrust.invoices.model;

import java.util.Objects;

final public class Company {

    final private Long id;
    final private String name;
    final private String taxIdentificationNumber;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(taxIdentificationNumber, company.taxIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taxIdentificationNumber);
    }

    @Override
    public String toString() {
        return "Company{"
                + "name='" + name
                + '\'' + ", taxIdentificationNumber='"
                + taxIdentificationNumber + '\''
                + '}';
    }
}
