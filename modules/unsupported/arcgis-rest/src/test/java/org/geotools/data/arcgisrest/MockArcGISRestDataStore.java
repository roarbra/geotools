package org.geotools.data.arcgisrest;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.util.logging.Logging;

public class MockArcGISRestDataStore extends ArcGISRestDataStore {

    private static String NAMESPACE = "http://dummy.net/";
    private static String ENDPOINT = "https://dummy.net/arcgis/rest/services/layer_1/FeatureServer";
    private static Boolean OPENDATA = Boolean.FALSE;

    private static Logger LOGGER = Logging.getLogger(MockArcGISRestDataStore.class);

    public MockArcGISRestDataStore()
            throws MalformedURLException, JsonSyntaxException, IOException {
        super(NAMESPACE, ENDPOINT, OPENDATA, null, null);
    }

    @Override
    public InputStream retrieveJSON(String methType, URL url, Map<String, Object> params) {
        LOGGER.fine(() -> String.format("%s %s {%s}", methType, url, formatParams(params)));
        return this.getClass().getResourceAsStream("test-data/bicycleDataset.json");
    }

    private String formatParams(Map<String, Object> params) {
        return params.entrySet().stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));
    }
}
