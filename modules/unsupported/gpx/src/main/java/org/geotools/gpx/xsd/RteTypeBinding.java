package org.geotools.gpx.xsd;

import java.math.BigDecimal;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class RteTypeBinding extends AbstractComplexBinding {

    private GeometryFactory gFactory;

    public RteTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GpxXSD.RteType;
    }

    @Override
    public Class getType() {
        return LineString.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List<Node> wpt = node.getChildren(GpxXSD.Rtept);
        Coordinate[] coordinates = new Coordinate[wpt.size()];
        int i = 0;
        for (Node child : wpt) {
            double lat, lon;

            lat = ((BigDecimal) child.getAttributeValue("lat")).doubleValue();
            lon = ((BigDecimal) child.getAttributeValue("lon")).doubleValue();
            coordinates[i++] = new Coordinate(lon, lat);
        }

        LineString ret = gFactory.createLineString(coordinates);
        ret.setUserData(value);
        return ret;
    }
}
