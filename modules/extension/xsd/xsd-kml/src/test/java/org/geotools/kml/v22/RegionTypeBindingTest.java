/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.kml.v22;

import static org.junit.Assert.assertEquals;

import org.geotools.xsd.Binding;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LinearRing;

public class RegionTypeBindingTest extends KMLTestSupport {

    @Test
    public void testType() {
        assertEquals(LinearRing.class, binding(KML.RegionType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.RegionType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        String xml = "<Region>"
                + "<LatLonAltBox>"
                + "<north>1</north>"
                + "<south>-1</south>"
                + "<east>1</east>"
                + "<west>-1</west>"
                + "</LatLonAltBox>"
                + "</Region>";

        buildDocument(xml);

        LinearRing box = (LinearRing) parse();
        Coordinate[] coordinates = box.getCoordinates();
        assertEquals("Invalid number of coordinates", 5, coordinates.length);
        assertEquals(coordinates[0], new Coordinate(-1, -1));
        assertEquals(coordinates[1], new Coordinate(-1, 1));
        assertEquals(coordinates[2], new Coordinate(1, 1));
        assertEquals(coordinates[3], new Coordinate(1, -1));
        assertEquals("Last and first coordinates should be equal", coordinates[0], coordinates[4]);
    }

    @Test
    public void testParseWithUnparsedElements() throws Exception {
        String xml = "<Region>"
                + "<LatLonAltBox>"
                + "<north>1</north>"
                + "<south>-1</south>"
                + "<east>1</east>"
                + "<west>-1</west>"
                + "<minAltitude>-1.5</minAltitude>"
                + "<maxAltitude>1500</maxAltitude>"
                + "<altitudeMode>clampToGround</altitudeMode>"
                + "</LatLonAltBox>"
                + "</Region>";

        buildDocument(xml);

        LinearRing box = (LinearRing) parse();
        Coordinate[] coordinates = box.getCoordinates();
        assertEquals("Invalid number of coordinates", 5, coordinates.length);
        assertEquals(coordinates[0], new Coordinate(-1, -1));
        assertEquals(coordinates[1], new Coordinate(-1, 1));
        assertEquals(coordinates[2], new Coordinate(1, 1));
        assertEquals(coordinates[3], new Coordinate(1, -1));
        assertEquals("Last and first coordinates should be equal", coordinates[0], coordinates[4]);
    }
}
