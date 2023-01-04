package org.geotools.gpx.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

public class TrksegTypeBinding extends AbstractComplexBinding {
    private GeometryFactory gFactory;

    public TrksegTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GpxXSD.TrksegType;
    }

    @Override
    public Class getType() {
        return MultiPoint.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /** Creates a MultiPoint of the Trkpt of the child elements */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List<Point> geoms = new ArrayList<Point>();
        for (Node child : node.getChildren(GpxXSD.Trkpt)) {
            geoms.add((Point) child.getValue());
        }

        return this.gFactory.createMultiPoint(geoms.toArray(new Point[geoms.size()]));
    }
}
