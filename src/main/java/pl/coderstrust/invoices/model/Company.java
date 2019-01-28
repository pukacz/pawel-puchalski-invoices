package pl.coderstrust.invoices.model;

import java.util.Objects;

public final class Company {

    private Long id;
    private String name;
    private String taxIdentificationNumber;

    public Company() {
    }

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
