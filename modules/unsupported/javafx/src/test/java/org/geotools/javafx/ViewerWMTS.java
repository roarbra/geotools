package org.geotools.javafx;

import java.net.URL;

import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSLayer;

public class ViewerWMTS {
	
	public static void main(String[] args) throws Exception {
		new ViewerWMTS().showWMTSTest();
	}
	
	public void showWMTSTest() throws Exception {
		String url = "https://opencache.statkart.no/gatekeeper/gk/gk.open_wmts";
		String name = "norgeskart_bakgrunn";

		// Set BoundingBox and CRS of WMTS
		WebMapTileServer server = new WebMapTileServer(new URL(url));
		WMTSLayer layer = server.getCapabilities().getLayer(name);
		layer.setBoundingBoxes(new CRSEnvelope("EPSG:32632", 599837.0, 6646573.5, 607683.6, 6660078.5));
		
		MapViewer viewer = MapViewer.getInstance();
		viewer.getMap().addLayer(RasterLayer.fromWMTS(server, layer));
		viewer.join();
	}
}
