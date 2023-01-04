package org.geotools.gpx.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

public class GpxTypeBinding extends AbstractComplexBinding {

    private GeometryFactory gFactory;

    public GpxTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    @Override
    public QName getTarget() {
        return GpxXSD.GpxType;
    }

    @Override
    public Class<GpxDataSet> getType() {
        return GpxDataSet.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        List<Node> wpt = node.getChildren(GpxXSD.Wpt);
        List<Point> geoms = new ArrayList<Point>();
        for (Node child : wpt) {
            if (child.getValue() instanceof Geometry) {
                geoms.add((Point) child.getValue());
            }
        }

        GeometryCollection waypoints =
                gFactory.createGeometryCollection(geoms.toArray(new Geometry[geoms.size()]));

        List<Node> rte = node.getChildren(GpxXSD.Rte);
        List<LineString> lines = new ArrayList<LineString>();
        for (Node child : rte) {
            lines.add((LineString) child.getValue());
        }
        GeometryCollection routes =
                gFactory.createGeometryCollection(lines.toArray(new Geometry[lines.size()]));

        List<Node> trk = node.getChildren(GpxXSD.Trk);
        List<MultiPoint> points = new ArrayList<MultiPoint>();
        for (Node child : trk) {
            Object ls = child.getValue();
            if (ls instanceof MultiPoint) {
                points.add((MultiPoint) ls);
            }
        }
        GeometryCollection tracks =
                gFactory.createGeometryCollection(points.toArray(new Geometry[points.size()]));

        return new GpxDataSet(waypoints, routes, tracks);
    }
}
