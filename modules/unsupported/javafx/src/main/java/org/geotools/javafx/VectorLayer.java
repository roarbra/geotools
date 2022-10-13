package org.geotools.javafx;

import java.io.File;
import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;

/**
 * 
 * Create a layer representing a vector. 
 * Using SLD.createSimpleStyle to determine kind of features.
 * 
 * @author Roar Brænden
 *
 */
public class VectorLayer{

	public static Layer fromShapefile(File shpFile) throws IOException {
		FileDataStore store = FileDataStoreFinder.getDataStore(shpFile);
		return fromFeatureSource(store.getFeatureSource());
	}
	
	public static Layer fromFeatureSource(SimpleFeatureSource source) throws IOException {
		Style style = SLD.createSimpleStyle(source.getSchema());
		FeatureLayer layer = new FeatureLayer(source, style);
		return layer;	
	}
}
