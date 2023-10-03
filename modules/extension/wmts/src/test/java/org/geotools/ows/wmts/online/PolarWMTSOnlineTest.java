package org.geotools.ows.wmts.online;

import java.net.URL;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.test.OnlineTestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * WMTS that covers the North pole.
 *
 * @author Roar Brænden
 */
public class PolarWMTSOnlineTest extends OnlineTestCase {

    private static final String STATKART_WMTS_URL =
            "https://opencache.statkart.no/gatekeeper/gk/gk.open_wmts?Version=1.0.0&service=wmts&request=getcapabilities";

    private static final String SIRKUMPOLAR_LAYER = "sirkumpolar_grunnkart";

    private static final String GEBCO_LAYER = "gebco";

    @Override
    protected String getFixtureId() {
        return "wmts-polar";
    }

    /** Defined without an extent */
    @Test
    public void testGetSirkumpolarLayer() throws Exception {
        WebMapTileServer server = new WebMapTileServer(new URL(STATKART_WMTS_URL));
        WMTSLayer layer = server.getCapabilities().getLayer(SIRKUMPOLAR_LAYER);
        Assert.assertNotNull(layer);
    }

    /** Defined with full world extent */
    @Test
    public void testGetGebcoLayer() throws Exception {
        WebMapTileServer server = new WebMapTileServer(new URL(STATKART_WMTS_URL));
        WMTSLayer layer = server.getCapabilities().getLayer(GEBCO_LAYER);
        Assert.assertNotNull(layer);
    }
}
