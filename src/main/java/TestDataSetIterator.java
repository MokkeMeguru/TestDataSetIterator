// TestDataSetIterator.java
// ver 1.0.0-SNAPSHOT
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.reader.impl.csv.CSVNLinesSequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import org.datavec.api.records.reader.impl.csv.CSVLineSequenceRecordReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public interface TestDataSetIterator {
    static DataSetIterator createDataSetIterator (int labelSize, File featureFile, File labelFile) throws IOException, InterruptedException {

        Configuration conf = new Configuration();

        CSVLineSequenceRecordReader featureSequenceRecordReader =
                new CSVLineSequenceRecordReader(0, ',');
        CSVLineSequenceRecordReader labelSequenceRecordReader =
                new CSVLineSequenceRecordReader(0, ',');
        featureSequenceRecordReader.initialize(conf, new FileSplit(featureFile));
        labelSequenceRecordReader.initialize(conf, new FileSplit(labelFile));

        // check coming correct data
        System.out.println("Features");
        while(featureSequenceRecordReader.hasNext()) {
            System.out.println(featureSequenceRecordReader.next());
        }
        System.out.println("Labels");
        while(labelSequenceRecordReader.hasNext()) {
            System.out.println(labelSequenceRecordReader.next());
        }

        featureSequenceRecordReader.reset();
        labelSequenceRecordReader.reset();

        SequenceRecordReaderDataSetIterator dataSetIterator =
                new SequenceRecordReaderDataSetIterator(
                        featureSequenceRecordReader,
                        labelSequenceRecordReader,
                        5,
                        labelSize,
                        false,
                        SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);

        dataSetIterator.reset();

        List<DataSet> trainData = new ArrayList<>();
        int i = 0;

        while (dataSetIterator.hasNext()) {
            // TODO: Here is the cancer ...
            DataSet ds = dataSetIterator.next();
            System.out.println(ds);
//            INDArray labelArray = Nd4j.zeros(ds.getFeatures().shape()[0], labelSize);
//            INDArray labels = ds.getLabels();
//            System.out.println(labels);
        }
        return null;
    }

    static void main (String... args) throws IOException, InterruptedException {
        int featureMax = TestTokenizer.saveTokens(
                new File("resources/test_source.csv"),
                new File("resources/test_feature.csv"),
                new File("resources/test_label.csv"));
        DataSetIterator dsi = createDataSetIterator(
                5,
                new File("resources/test_feature.csv"),
                new File("resources/test_label.csv")
                );
    }
}

// Features
// [[1, 2, 3, 4, 5]
//  [1, 2, 3, 4, 5, 6]
//  [1, 2, 3, 4, 5, 6, 7]
//  [1, 2, 3]
//  [1, 2, 3, 4, 5, 6]
// Labels
//  [0]
//  [1]
//  [2]
//  [3]
//  [4]
//
// ===========INPUT===================
// [[[    1.0000,    2.0000,    3.0000,    4.0000,    5.0000,         0,         0]],
//  [[    1.0000,    2.0000,    3.0000,    4.0000,    5.0000,    6.0000,         0]],
//  [[    1.0000,    2.0000,    3.0000,    4.0000,    5.0000,    6.0000,    7.0000]],
//  [[    1.0000,    2.0000,    3.0000,         0,         0,         0,         0]],
//  [[    1.0000,    2.0000,    3.0000,    4.0000,    5.0000,    6.0000,         0]]]
// =================OUTPUT==================
// [[[         0,         0,         0,         0,    1.0000,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0]],
//  [[         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,    1.0000,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0]],
//  [[         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,    1.0000],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0]],
//  [[         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,    1.0000,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0]],
//  [[         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,         0,         0],
//   [         0,         0,         0,         0,         0,    1.0000,         0]]]
// ===========INPUT MASK===================
// [[    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,         0,         0],
//  [    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,         0],
//  [    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,    1.0000],
//  [    1.0000,    1.0000,    1.0000,         0,         0,         0,         0],
//  [    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,    1.0000,         0]]
// ===========OUTPUT MASK===================
// [[         0,         0,         0,         0,    1.0000,         0,         0],
//  [         0,         0,         0,         0,         0,    1.0000,         0],
//  [         0,         0,         0,         0,         0,         0,    1.0000],
//  [         0,         0,    1.0000,         0,         0,         0,         0],
//  [         0,         0,         0,         0,         0,    1.0000,         0]]
// Process finished with exit code 0