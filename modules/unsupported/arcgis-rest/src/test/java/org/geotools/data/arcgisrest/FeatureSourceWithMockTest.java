package org.geotools.data.arcgisrest;

import org.junit.Test;

public class FeatureSourceWithMockTest {

    private static final String LAYER_NAME = "dummy_1";

    @Test
    public void testGetFeatures() throws Exception {
        ArcGISRestDataStore dataStore = new MockArcGISRestDataStore();
        dataStore.getFeatureSource(LAYER_NAME);
    }
}
