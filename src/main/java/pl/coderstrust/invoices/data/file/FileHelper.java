package pl.coderstrust.invoices.data.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    private RandomAccessFile file;

    FileHelper(RandomAccessFile file) {
        this.file = file;
    }

    public List<String> getInvoices() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;

        while ((line = file.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    public void saveInvoice(Long invoiceId, String invoice) throws IOException {

        String line = "" + invoiceId + ": " + invoice + "\n";
        Long cursor = getPositionOfInvoice(invoiceId);

        file.seek(cursor);
        file.writeBytes(line);
    }

    public void deleteInvoice(Long invoiceId) throws IOException {

        Long cursor = getPositionOfInvoice(invoiceId);

        if (!cursor.equals(file.length())) {
            file.seek(cursor);
            String line = file.readLine();

            file.seek(file.getFilePointer() - line.getBytes().length - 1);
            for (int i = 0; i < line.length(); i++) {
                file.writeBytes(" ");
            }
            file.writeBytes("\n");
        }
    }

    private Long getPositionOfInvoice(Long invoiceId) throws IOException {

        Long cursor;
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
        return cursor;
    }
}
