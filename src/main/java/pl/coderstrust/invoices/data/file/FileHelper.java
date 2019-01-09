package pl.coderstrust.invoices.data.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    private List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    private void writeLinesToFile(List<String> list, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i));
                if (i < list.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }
}
