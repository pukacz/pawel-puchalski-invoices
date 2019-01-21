package pl.coderstrust.invoices.data.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    public static void main(String[] args) throws IOException {

//        File file = new File(new Configuration().getInvoicesFile());
//        file.createNewFile();
//
        FileHelper fileHelper = new FileHelper();
//
//        Company company1 = new Company(1L, "Krzak1", "111");
//        Company company2 = new Company(2L, "Krzak2", "222");
//
//        Company company3 = new Company(3L, "Krzak3", "333");
//        Company company4 = new Company(4L, "Krzak4", "444");
//
//        Invoice invoice1 = new Invoice(1111L, "issue1",
//            LocalDate.of(2019, 1, 1), company1, company2,
//            null);
//
//        Invoice invoice2 = new Invoice(3333L, "issue2",
//            LocalDate.of(2019, 1, 2), company3, company4,
//            null);
//
//        fileHelper.saveInvoice(invoice1.getId(), invoice1.toString());
//        fileHelper.saveInvoice(invoice2.getId(), invoice1.toString());

//        System.out.println(fileHelper.getPositionOfInvoice(2222L));

        String tekst =
            "1111: Invoice{id=1111, issue='issue1', issueDate=2019-01-01, seller=Company{id=1, name='Krzak1', taxIdentificationNumber='111'}, buyer=Company{id=2, name='Krzak2', taxIdentificationNumber='222'}, entries=null}\n"
                + "2222: Invoice{id=1111, issue='issue1', issueDate=2019-01-01, seller=Company{id=1, name='Krzak1', taxIdentificationNumber='111'}, buyer=Company{id=2, name='Krzak2', taxIdentificationNumber='222'}, entries=null}\n"
                + "1111: Invoice{id=1111, issue='issue1', issueDate=2019-01-01, seller=Company{id=1, name='Krzak1', taxIdentificationNumber='111'}, buyer=Company{id=2, name='Krzak2', taxIdentificationNumber='222'}, entries=null}\n";

        int colonPosition = tekst.indexOf("2222: ");
        int endOfLine = tekst.substring(colonPosition).indexOf("\n");
//        System.out.println(endOfLine);
        System.out.println(tekst.substring(endOfLine));
    }

    private String readOnly = "r";
    private String readWrite = "rw";
    private Configuration configuration;

    FileHelper() {
        this.configuration = new Configuration();
    }

    public List<String> getInvoices() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;

        try (RandomAccessFile reader = new RandomAccessFile(configuration.getInvoicesFile(),
            readOnly)) {
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public void saveInvoice(Long invoiceId, String invoice) throws IOException {
        String line = "" + invoiceId + ": " + invoice + "\n";
        Long position = getPositionOfInvoice(invoiceId);

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            readWrite)) {
            file.seek(position);
            file.writeBytes(line);
        }
    }

    public void deleteInvoice(Long invoiceId) throws IOException {
        Long invoicePosition = getPositionOfInvoice(invoiceId);
        if (invoicePosition.equals(0L)) {

        } else {
            try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
                readWrite)) {
                file.seek(invoicePosition);
                file.readUnsignedByte();
            }

        }
    }

    private Long getPositionOfInvoice(Long invoiceId) throws IOException {
        long pointer = 0;

        if (configuration.getInvoicesFile().equals(null)) {
            File file = new File(configuration.getInvoicesFile());
            file.createNewFile();
        }

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            readOnly)) {
            if (file.length() == 0) {
                return 0L;
            }
            String line;
            int colonPosition;
            Long invoiceInLineID;

            while ((line = file.readLine()) != null) {
                colonPosition = line.indexOf(": ");

                if (colonPosition > 0) {
                    invoiceInLineID = Long.parseLong(line.substring(1, colonPosition));
                    if (invoiceInLineID.equals(invoiceId)) {
                        return pointer + colonPosition + 2;
                    }
                }
                pointer = file.getFilePointer();
            }
        }
        return pointer;
    }
}
