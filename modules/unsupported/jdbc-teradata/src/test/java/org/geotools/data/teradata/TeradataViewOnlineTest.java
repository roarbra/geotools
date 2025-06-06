/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.teradata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Handler;
import java.util.logging.Level;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.geotools.util.logging.Logging;
import org.junit.Test;

public class TeradataViewOnlineTest extends JDBCViewOnlineTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new TeradataViewTestSetup();
    }

    @Override
    protected boolean isPkNillable() {
        return false;
    }

    @Test
    public void testViewWithTesselationAndIndex() throws Exception {

        Handler handler = Logging.getLogger(TeradataViewOnlineTest.class).getHandlers()[0];
        handler.setLevel(Level.FINEST);

        Logging.getLogger(TeradataViewOnlineTest.class).setLevel(Level.FINEST);

        SimpleFeatureType schema = dataStore.getSchema("lakesview2");
        TessellationInfo tesselation =
                (TessellationInfo) schema.getGeometryDescriptor().getUserData().get(TessellationInfo.KEY);
        assertNotNull("expected tessleation info", tesselation);
        assertEquals("lakesview2_geom_idx", tesselation.getIndexTableName());

        // this will use the index but since the index is empty, it will return 0 (despite actually
        // intersecting)
        BBOX bbox = CommonFactoryFinder.getFilterFactory(null).bbox("geom", -20, -20, 20, 20, null);
        assertEquals(0, query(bbox));

        // the filter will not use the index since this is essentially a table scan
        // due to the size of the bbox versus world bounds
        bbox = CommonFactoryFinder.getFilterFactory(null).bbox("geom", -179, -89, 179, 89, null);
        assertEquals(1, query(bbox));
    }

    int query(Filter f) throws Exception {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource("lakesview2");
        Query q = new Query();
        q.setFilter(f);
        int r = 0;
        try (SimpleFeatureIterator features = featureSource.getFeatures(q).features()) {
            while (features.hasNext()) {
                features.next();
                r++;
            }
        }
        return r;
    }
}
