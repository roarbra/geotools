package org.geotools.javafx;

import java.io.File;

import org.geotools.map.Layer;

public class ViewerShapefile {

	public static void main(String[] args) throws Exception {
		String path = "/Users/roar/Satellittbilder/Lillomarka_innsjo_32_2.shp";
		Layer layer = VectorLayer.fromShapefile(new File(path));
		MapViewer viewer = MapViewer.getInstance();
		viewer.getMap().addLayer(layer);
		viewer.join();
	}

}
