/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.arcgisrest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class ArcGISRestFeatureReaderTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.arcgisrest");

    ArcGISRestFeatureReader reader;
    SimpleFeatureType fType;
    String json;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("jsonfeature");
        builder.add("vint", Integer.class);
        builder.add("vfloat", Float.class);
        builder.add("vstring", String.class);
        builder.add("vdate", Date.class);
        builder.add("geometry", Geometry.class);

        this.fType = builder.buildFeatureType();
    }

    @Test(expected = IOException.class)
    public void emptyInputStreamHasNext() throws Exception {

        this.reader = new ArcGISRestFeatureReader(this.fType, new ByteArrayInputStream("".getBytes()), this.LOGGER);
        assertFalse(this.reader.hasNext());
    }

    @Test
    public void noFeaturesHasNext() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.geo.json");
        this.reader = new ArcGISRestFeatureReader(this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        assertFalse(this.reader.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void noFeaturesNext() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.geo.json");
        this.reader = this.reader =
                new ArcGISRestFeatureReader(this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        this.reader.next();
    }

    @Test
    public void noProperties() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noProperties.geo.json");
        this.reader = new ArcGISRestFeatureReader(this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat = this.reader.next();
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertFalse(this.reader.hasNext());
        assertEquals("geometry", feat.getDefaultGeometryProperty().getName().getLocalPart());
    }

    @Test
    public void properties() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/properties.geo.json");
        this.reader = new ArcGISRestFeatureReader(this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat = this.reader.next();
        assertEquals(2, feat.getAttribute("vint"));
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertEquals(3, feat.getAttribute("vint"));
        assertEquals("geometry", feat.getDefaultGeometryProperty().getName().getLocalPart());
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertEquals(1, feat.getAttribute("vint"));
        // FIXME: this fail with AbstractMethod in GeoJSONParser
        /*
        assertEquals(
                (new SimpleDateFormat((GeoJSONParser.DATETIME_FORMAT))
                        .format(new Date(1381968000000L))),
                feat.getAttribute("vdate"));
         */
        assertFalse(this.reader.hasNext());
    }
}
