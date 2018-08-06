// TestDataSetIterator.java
// ver 1.0.0-beta
import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.io.IOException;

public interface TestDataSetIterator {
    static DataSetIterator createDataSetIterator () throws IOException, InterruptedException {
        SequenceRecordReader testFeatures = new CSVSequenceRecordReader(0,",");

        testFeatures.initialize(new FileSplit(new File("resources/test_train.csv")));
        System.out.println(testFeatures.sequenceRecord());
        testFeatures.reset();
        SequenceRecordReader testLabels = new CSVSequenceRecordReader(0,",");
        testLabels.initialize(new FileSplit(new File("resources/test_label.csv")));
        return new SequenceRecordReaderDataSetIterator(
                testFeatures,
                testLabels,
                3,
                6,
                false,
                SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END
        );
    }
    static void main (String... args) throws IOException, InterruptedException {
        DataSetIterator dsi = createDataSetIterator();
        System.out.println(dsi.next());
    }
}
