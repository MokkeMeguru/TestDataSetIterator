import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public interface TestTokenizer {
    static void saveTokens (File sourceFile, File featureFile, File labelFile) throws IOException, InterruptedException {
        PrintWriter pf;
        PrintWriter lf;
        CSVRecordReader csvRecordReader = new CSVRecordReader(0, ",");
        csvRecordReader.initialize(new FileSplit(sourceFile));
        // features file
        pf = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(featureFile, false)));
        // label file
        lf = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(labelFile, false)));

        while (csvRecordReader.hasNext()) {
            List<Writable> writableList = csvRecordReader.next();

            // parse string -> tokenize them -> save as feature_data
            String string = writableList.get(0).toString();
            List<String> stringList = Arrays.asList(string.split("[\\s]+"));
            List<Integer> integerList =
                    stringList.stream()
                            .map(Integer::valueOf)
                            .collect(Collectors.toList());
            Iterator<Integer> iterable = integerList.iterator();
            while (iterable.hasNext()) {
                pf.print(iterable.next());
                if (iterable.hasNext())
                    pf.print(",");
            }
            lf.print(writableList.get(1).toInt());

            if (csvRecordReader.hasNext()) {
                lf.println();
                pf.println();
            }
        }

        lf.close();
        pf.close();

    }

    static void main (String... args) throws IOException, InterruptedException {
        TestTokenizer.saveTokens(new File("resources/test_source.csv"),
                new File("resources/test_feature.csv"),
                new File("resources/test_label.csv"));
    }
}
