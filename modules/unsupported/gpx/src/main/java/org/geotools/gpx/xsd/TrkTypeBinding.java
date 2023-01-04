package org.geotools.gpx.xsd;

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

public class TrkTypeBinding extends AbstractComplexBinding {

    GeometryFactory gFactory;

    public TrkTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GpxXSD.TrkType;
    }

    @Override
    public Class getType() {
        return MultiPoint.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        MultiPoint points = null;
        List<Node> trksegs = node.getChildren(GpxXSD.Trkseg);
        if (trksegs.isEmpty()) {
            points = this.gFactory.createMultiPoint(new Point[] {});
        } else {
            for (Node next : trksegs) {
                Object v = next.getValue();
                if (v instanceof MultiPoint) {
                    points =
                            points == null
                                    ? (MultiPoint) v
                                    : (MultiPoint) points.union((MultiPoint) v);
                }
            }
        }
        points.setUserData(value);
        return points;
    }
}
