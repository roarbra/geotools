package org.geotools.gpx.xsd;

import javax.xml.namespace.QName;
import org.geotools.xsd.Configuration;
import org.locationtech.jts.geom.GeometryFactory;
import org.picocontainer.MutablePicoContainer;

/**
 * Defining the schema of a gpx xml file. Used in conjunction with org.geotools.xsd.Parser in this
 * way:
 *
 * <pre>
 * try (FileInputStream input = new FileInputStream(gpxFile)) {
 * 	Parser parser = new Parser(new GpxConfiguration());
 * 	GpxDataSet gpxSet = parser.parse(input);
 * }
 * </pre>
 *
 * @author Roar Brænden
 */
public class GpxConfiguration extends Configuration {

    public GpxConfiguration() {
        super(GpxXSD.getInstance());
    }

    @Override
    protected void registerBindings(MutablePicoContainer container) {
        container.registerComponentImplementation(GpxXSD.GpxType, GpxTypeBinding.class);
        container.registerComponentImplementation(GpxXSD.WptType, WptTypeBinding.class);
        container.registerComponentImplementation(GpxXSD.RteType, RteTypeBinding.class);
        container.registerComponentImplementation(GpxXSD.TrkType, TrkTypeBinding.class);
        container.registerComponentImplementation(GpxXSD.TrksegType, TrksegTypeBinding.class);
        container.registerComponentImplementation(
                new QName(GpxXSD.NAMESPACE, GpxXSD.Trkpt), TrkptBinding.class);
    }

    @Override
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(GeometryFactory.class);
    }
}
