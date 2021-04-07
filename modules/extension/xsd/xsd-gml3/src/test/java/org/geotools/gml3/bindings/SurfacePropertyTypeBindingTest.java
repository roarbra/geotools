/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SurfacePropertyTypeBindingTest extends GML3TestSupport {

    @Test
    public void testEncode() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Polygon polygon =
                gf.createPolygon(
                        gf.createLinearRing(
                                new Coordinate[] {
                                    new Coordinate(0, 0),
                                    new Coordinate(1, 1),
                                    new Coordinate(2, 2),
                                    new Coordinate(0, 0)
                                }),
                        null);

        Document dom = encode(polygon, GML.surfaceProperty);
        Assert.assertEquals(1, dom.getElementsByTagName("gml:Polygon").getLength());
        Assert.assertEquals(1, dom.getElementsByTagName("gml:exterior").getLength());
    }
    
    @Test
    public void parseSurfacePropertyWithRing() throws Exception {
        Element prev = GML3MockData.element(GML.surfaceProperty, document, document);
        prev = GML3MockData.element(GML.Surface, document, prev);
        prev = GML3MockData.element(GML.patches, document, prev);
        prev = GML3MockData.element(GML.PolygonPatch, document, prev);
        prev = GML3MockData.element(GML.exterior, document, prev);
        prev = GML3MockData.element(GML.Ring, document, prev);
        prev = GML3MockData.element(GML.curveMember, document, prev);
        prev = GML3MockData.element(GML.Curve, document, prev);
        prev = GML3MockData.element(GML.segments, document, prev);
        lineStringSegment("1.0 2.0 3.5 3.0", prev);
        lineStringSegment("3.5 3.0 3.0 2.5", prev);
        lineStringSegment("3.0 2.5 1.0 2.0", prev);
        
        Geometry geom = (Geometry) parse();
        Assert.assertNotNull(geom);
    }
    
    private Element lineStringSegment(String posList, Element root) {
        Element prev = GML3MockData.element(GML.LineStringSegment, document, root);
        GML3MockData.element(GML.posList, document, prev)
                    .appendChild(document.createTextNode(posList));
        return prev;
    }
}
