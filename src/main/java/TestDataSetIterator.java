// TestDataSetIterator.java
// ver 1.0.0-SNAPSHOT
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.reader.impl.csv.CSVNLinesSequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public interface TestDataSetIterator {
    static DataSetIterator createDataSetIterator (int featureMax, int labelSize, File featureFile, File labelFile) throws IOException, InterruptedException {

        Configuration conf = new Configuration();

        CSVNLinesSequenceRecordReader featureSequenceRecordReader =
                new CSVNLinesSequenceRecordReader(featureMax, 0,",");
        CSVNLinesSequenceRecordReader labelSequenceRecordReader =
                new CSVNLinesSequenceRecordReader(1, 0, ",");
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
                        1,
                        labelSize,
                        false,
                        SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);

        dataSetIterator.reset();

        List<DataSet> trainData = new ArrayList<>();
        int i = 0;

        while (dataSetIterator.hasNext()) {
            // TODO: Here is the cancer ...
            DataSet ds = dataSetIterator.next();
            INDArray labelArray = Nd4j.zeros(ds.getFeatures().shape()[0], labelSize);
            INDArray labels = ds.getLabels();
            System.out.println(labels);
        }
        return null;
    }

    static void main (String... args) throws IOException, InterruptedException {
        int featureMax = TestTokenizer.saveTokens(
                new File("resources/test_source.csv"),
                new File("resources/test_feature.csv"),
                new File("resources/test_label.csv"));
        DataSetIterator dsi = createDataSetIterator(
                featureMax,
                5,
                new File("resources/test_feature.csv"),
                new File("resources/test_label.csv")
                );
        System.out.println(dsi);
    }
}

// Error
//　Exception in thread "main" java.lang.IndexOutOfBoundsException: 25
//        at org.bytedeco.javacpp.indexer.Indexer.checkIndex(Indexer.java:90)
//        at org.bytedeco.javacpp.indexer.FloatRawIndexer.put(FloatRawIndexer.java:90)
//        at org.nd4j.linalg.api.buffer.BaseDataBuffer.put(BaseDataBuffer.java:1116)
//        at org.nd4j.linalg.jcublas.buffer.BaseCudaDataBuffer.put(BaseCudaDataBuffer.java:684)
//        at org.nd4j.linalg.api.ndarray.BaseNDArray.putScalar(BaseNDArray.java:1414)
//        at org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator.convertWritablesSequence(RecordReaderMultiDataSetIterator.java:661)
//        at org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator.convertFeaturesOrLabels(RecordReaderMultiDataSetIterator.java:367)
//        at org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator.nextMultiDataSet(RecordReaderMultiDataSetIterator.java:325)
//        at org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator.next(RecordReaderMultiDataSetIterator.java:213)
//        at org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator.next(SequenceRecordReaderDataSetIterator.java:345)
//        at org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator.next(SequenceRecordReaderDataSetIterator.java:324)
//        at TestDataSetIterator.createDataSetIterator(TestDataSetIterator.java:61)
//        at TestDataSetIterator.main(TestDataSetIterator.java:74)


// feature & label data
// Features
// [1, 2, 3, 4, 5]
// [1, 2, 3, 4, 5, 6]
// [1, 2, 3, 4, 5, 6, 7]
// [1, 2, 3]
// [1, 2, 3, 4, 5, 6]
// Labels
// [1]
// [2]
// [3]
// [4]
// [5]