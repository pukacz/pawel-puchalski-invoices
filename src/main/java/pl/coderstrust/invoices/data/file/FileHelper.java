package pl.coderstrust.invoices.data.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    public static void main(String[] args) throws IOException {

        FileHelper fileHelper = new FileHelper();

//        String invoice1 = "1line";
//        String invoice2 = "2line-zmienionyxxxx";
//        String invoice3 = "3line";

//        fileHelper.saveInvoice(111L, invoice1);
//        fileHelper.saveInvoice(222L, invoice2);
//        fileHelper.saveInvoice(333L, invoice3);

//        System.out.println(fileHelper.getPositionOfInvoice(111L));
//        System.out.println(fileHelper.getPositionOfInvoice(222L));
//        System.out.println(fileHelper.getPositionOfInvoice(333L));
        fileHelper.deleteInvoice(222L);
        fileHelper.deleteInvoice(111L);
    }

    private String readOnly = "r";
    private String readWrite = "rw";
    private Configuration configuration;
    private RandomAccessFile raf;

    FileHelper() {
//        this.raf = raf;
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
        Long cursor = getPositionOfInvoice(invoiceId);

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            readWrite)) {
            file.seek(cursor);
            file.writeBytes(line);
        }
    }

    public void deleteInvoice(Long invoiceId) throws IOException {

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            readWrite)) {

            Long cursor = getPositionOfInvoice(invoiceId);
            long remainingByteCount;
            byte[] rest;

            if (!cursor.equals(file.length())) {
                file.seek(cursor);
                String line = file.readLine();
                remainingByteCount = file.length() - file.getFilePointer();

                file.seek(file.getFilePointer() - line.getBytes().length-1);
                for(int i=0; i<line.length();i++){
                    file.writeBytes(" ");
                }


//                rest = new byte[(int) remainingByteCount];
//                file.read(rest);

//                file.seek(cursor);
//                file.setLength(remainingByteCount);
                file.writeBytes("\n");
//                file.write(rest);
            }
        }
    }

    private Long getPositionOfInvoice(Long invoiceId) throws IOException {
        Long cursor = -1L;

        if (configuration.getInvoicesFile().equals(null)) {
            File file = new File(configuration.getInvoicesFile());
            file.createNewFile();
            return cursor;
        }

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            readOnly)) {

            String line;
            String idInString;
            int colonIndex;
            cursor = file.getFilePointer();

            while ((line = file.readLine()) != null) {

                if ((colonIndex = line.indexOf(": ")) > 0) {
                    idInString = line.substring(0, colonIndex);

                    if (invoiceId.equals(Long.parseLong(idInString))) {
                        return cursor;
                    }
                    cursor = file.getFilePointer();
                } else {
                    cursor += line.length() + 1;
                }
            }
        }
        return cursor;
    }
}
