package org.geotools.gpx.xsd;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;

/**
 * Result after parsing a gpx-file with the GpxConfiguration
 *
 * @author Roar Brænden
 */
public class GpxDataSet {

    private final GeometryCollection waypoints;
    private final GeometryCollection routes;
    private final GeometryCollection tracks;

    GpxDataSet(GeometryCollection waypoints, GeometryCollection routes, GeometryCollection tracks) {
        this.waypoints = waypoints;
        this.routes = routes;
        this.tracks = tracks;
    }

    public GeometryCollection getWaypoints() {
        return this.waypoints;
    }

    /**
     * The waypoints are returned as a FeatureCollection. Attributes in the schema are mapped to
     * elements in the wpt-elements of the gpx-file.
     *
     * @param schema
     * @return
     */
    public SimpleFeatureCollection extractWaypoints(SimpleFeatureType schema) {
        return extractFeatures(schema, this.waypoints);
    }

    /**
     * The routes are returnes as a FeatureCollection. Attributes in the schema are mapped to
     * elements in the rte-elements of the gpx-file.
     *
     * @param schema
     * @return
     */
    public SimpleFeatureCollection extractRoutes(SimpleFeatureType schema) {
        return extractFeatures(schema, this.routes);
    }

    public GeometryCollection getRoutes() {
        return this.routes;
    }

    public GeometryCollection getTracks() {
        return this.tracks;
    }

    /**
     * The tracks are returned as a FeatureCollection. Attributes in the schema are mapped to
     * elements in the trk-elements of the gpx-file.
     *
     * @param schema
     * @return
     */
    public SimpleFeatureCollection extractTracks(SimpleFeatureType schema) {
        return extractFeatures(schema, this.tracks);
    }

    @SuppressWarnings("unchecked")
    private SimpleFeatureCollection extractFeatures(
            SimpleFeatureType schema, GeometryCollection geoms) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        DefaultFeatureCollection coll =
                new DefaultFeatureCollection(schema.getName().getLocalPart(), schema);
        GeometryType destGeom = schema.getGeometryDescriptor().getType();
        int k = 0;

        for (int i = 0; i < geoms.getNumGeometries(); i++) {
            Geometry geom = geoms.getGeometryN(i);
            HashMap<String, Object> userData = (HashMap<String, Object>) geom.getUserData();
            geom.setUserData(null);

            if (geom instanceof Point) {
                createFeature(builder, geom, userData);
                coll.add(builder.buildFeature(Integer.toString(k++)));
            } else if (geom instanceof MultiPoint) {
                MultiPoint points = (MultiPoint) geom;

                if (destGeom.getBinding() == Point.class) {

                    for (int j = 0; j < points.getNumPoints(); j++) {
                        Point pnt = (Point) points.getGeometryN(j);
                        HashMap<String, Object> userData2 =
                                (HashMap<String, Object>) pnt.getUserData();
                        pnt.setUserData(null);

                        for (Entry<String, Object> ent : userData.entrySet()) {
                            if (!userData2.containsKey(ent.getKey())) {
                                userData2.put(ent.getKey(), ent.getValue());
                            }
                        }
                        createFeature(builder, pnt, userData2);
                        coll.add(builder.buildFeature(Integer.toString(k++)));
                    }
                } else if (destGeom.getBinding() == LineString.class) {
                    GeometryFactory factory = points.getFactory();
                    Coordinate[] coordinates = points.getCoordinates();
                    if (coordinates.length > 1) {
                        LineString newGeom = factory.createLineString(coordinates);

                        createFeature(builder, newGeom, userData);
                        coll.add(builder.buildFeature(Integer.toString(k++)));
                    }
                } else {
                    throw new IllegalArgumentException("How should I do this?");
                }
            } else {
                throw new IllegalArgumentException("Unknown geometry");
            }
        }

        return coll;
    }

    private void createFeature(
            SimpleFeatureBuilder builder, Geometry geom, Map<String, Object> userData) {
        SimpleFeatureType schema = builder.getFeatureType();
        for (int j = 0; j < schema.getAttributeCount(); j++) {
            AttributeDescriptor desc = schema.getDescriptor(j);
            Object value =
                    desc.getType() instanceof GeometryType
                            ? geom
                            : userData.get(desc.getLocalName());

            if (value != null) {
                builder.set(j, value);
            }
        }
    }
}
