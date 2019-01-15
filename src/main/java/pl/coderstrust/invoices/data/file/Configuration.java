package pl.coderstrust.invoices.data.file;

import java.io.File;

public class Configuration {

    private String invoices = "invoicesData.json";
    private String IDsOfExistingInvoices = "invoicesID.json";
    private String folder = "localData";

    public String getInvoicesFile() {
        return getFilePath(invoices);
    }

    public String getInvoicesIDFile() {
        return getFilePath(IDsOfExistingInvoices);
    }

    private String getFilePath(String fileName) {
        File localDataDirectory = new File(folder);
        return localDataDirectory.getAbsolutePath() + "\\" + fileName;
    }
}
