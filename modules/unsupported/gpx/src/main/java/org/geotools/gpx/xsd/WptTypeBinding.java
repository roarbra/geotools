package org.geotools.gpx.xsd;

import java.math.BigDecimal;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/** Way point - contains lat, lon and ele */
public class WptTypeBinding extends AbstractComplexBinding {

    private GeometryFactory gFactory;

    public WptTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GpxXSD.WptType;
    }

    @Override
    public Class getType() {
        return Point.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /** Extract lat, lon and ele. Put rest of node into userData */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        final double lat = ((BigDecimal) node.getAttributeValue("lat")).doubleValue();
        final double lon = ((BigDecimal) node.getAttributeValue("lon")).doubleValue();

        Object elevation = node.getChildValue("ele");

        final Point pnt =
                gFactory.createPoint(
                        elevation == null
                                ? new Coordinate(lon, lat)
                                : new Coordinate(lon, lat, ((BigDecimal) elevation).doubleValue()));
        pnt.setUserData(value);

        return pnt;
    }
}
