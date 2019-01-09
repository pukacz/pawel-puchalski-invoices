package pl.coderstrust.invoices.data.file;

import java.io.File;

public class Configuration {

    private String invoicesFile = "invoices.json";
    private String companiesFile = "companies.json";

    public String getCompaniesFile(String fileName) {
        return getFilePath(companiesFile);
    }

    public String getInvoicesFile(String fileName) {
        return getFilePath(invoicesFile);
    }

    private String getFilePath(String fileName) {
        File localDataDirectory = new File("localData");
        return localDataDirectory.getAbsolutePath() + "\\" + fileName;
    }
}
