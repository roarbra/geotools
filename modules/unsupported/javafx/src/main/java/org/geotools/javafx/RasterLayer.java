package org.geotools.javafx;

import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.styling.Style;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.style.ContrastMethod;
import org.opengis.style.SelectedChannelType;

/**
 * 
 * Creates a Layer representing a Coverage using RasterSymbolizer.
 * 
 * @author Roar Brænden
 *
 */
public class RasterLayer {
	

	public static Layer fromFile(File singleFile) throws IOException{
		AbstractGridFormat format = GridFormatFinder.findFormat( singleFile );
		AbstractGridCoverage2DReader reader = format.getReader( singleFile );
		
		Style rasterStyle = createGreyscaleStyle( 1 );
		GridCoverage2D cov = reader.read(null);
		
		return new GridCoverageLayer(cov, rasterStyle);
	}
	
	public static Layer fromWMTS(WebMapTileServer wmts, WMTSLayer layer) {
		return new WMTSMapLayer(wmts, layer);
	}
	
	
	private static Style createGreyscaleStyle(int band) {
		final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
		final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
		
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal( 1.0 ), ContrastMethod.NORMALIZE);
		SelectedChannelType channel = sf.createSelectedChannelType( String.valueOf( band ), ce);
		
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(channel);
		sym.setChannelSelection(sel);
		
		return SLD.wrapSymbolizers(sym);
		
	}
}
