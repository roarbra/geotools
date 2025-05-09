/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.Geometry;

/**
 * This class shows how to "join" two feature sources.
 *
 * @author Jody
 */
@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
public class JoinExample {

    /** @param args shapefile to use, if not provided the user will be prompted */
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to GeoTools:" + GeoTools.getVersion());

        File file, file2;
        if (args.length == 0) {
            file = JFileDataStoreChooser.showOpenFile("shp", null);
        } else {
            file = new File(args[0]);
        }
        if (args.length <= 1) {
            file2 = JFileDataStoreChooser.showOpenFile("shp", null);
        } else {
            file2 = new File(args[1]);
        }
        if (file == null || !file.exists() || file2 == null || !file2.exists()) {
            System.exit(1);
        }
        Map<String, Object> connect = new HashMap<>();
        connect.put("url", file.toURI().toURL());
        DataStore shapefile = DataStoreFinder.getDataStore(connect);
        String typeName = shapefile.getTypeNames()[0];
        SimpleFeatureSource shapes = shapefile.getFeatureSource(typeName);

        Map<String, Object> connect2 = new HashMap<>();
        connect.put("url", file2.toURI().toURL());
        DataStore shapefile2 = DataStoreFinder.getDataStore(connect);
        String typeName2 = shapefile2.getTypeNames()[0];
        SimpleFeatureSource shapes2 = shapefile2.getFeatureSource(typeName2);

        joinExample(shapes, shapes2);
        System.exit(0);
    }

    // joinExample start
    private static void joinExample(SimpleFeatureSource shapes, SimpleFeatureSource shapes2) throws Exception {
        SimpleFeatureType schema = shapes.getSchema();
        String typeName = schema.getTypeName();
        String geomName = schema.getGeometryDescriptor().getLocalName();

        SimpleFeatureType schema2 = shapes2.getSchema();
        String typeName2 = schema2.getTypeName();
        String geomName2 = schema2.getGeometryDescriptor().getLocalName();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Query outerGeometry = new Query(typeName, Filter.INCLUDE, new String[] {geomName});
        SimpleFeatureCollection outerFeatures = shapes.getFeatures(outerGeometry);
        SimpleFeatureIterator iterator = outerFeatures.features();
        int max = 0;
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                try {
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    if (!geometry.isValid()) {
                        // skip bad data
                        continue;
                    }
                    Filter innerFilter = ff.intersects(ff.property(geomName2), ff.literal(geometry));
                    Query innerQuery = new Query(typeName2, innerFilter, Query.NO_NAMES);
                    SimpleFeatureCollection join = shapes2.getFeatures(innerQuery);
                    int size = join.size();
                    max = Math.max(max, size);
                } catch (Exception skipBadData) {
                }
            }
        } finally {
            iterator.close();
        }
        System.out.println("At most " + max + " " + typeName2 + " features in a single " + typeName + " feature");
    }
    // joinExample end

    // Filter innerFilter = ff.dwithin(ff.property(geomName2), ff.literal( geometry
    // ),1.0,"km");
    // Filter innerFilter = ff.beyond(ff.property(geomName2), ff.literal( geometry
    // ),1.0,"km");
    // Filter innerFilter = ff.not( ff.disjoint(ff.property(geomName2), ff.literal(
    // geometry )) );

}
