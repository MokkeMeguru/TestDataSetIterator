// TestDataSetIterator.java
// ver 1.0.0-SNAPSHOT
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.IOException;

public interface TestDataSetIterator {
    static DataSetIterator createDataSetIterator () {
        return null;
    }

    static void main (String... args) {
        DataSetIterator dsi = createDataSetIterator();
        System.out.println(dsi);
    }
}