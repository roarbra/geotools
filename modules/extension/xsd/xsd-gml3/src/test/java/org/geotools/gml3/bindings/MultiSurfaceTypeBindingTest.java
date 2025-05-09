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

import static org.junit.Assert.assertEquals;

import org.geotools.geometry.jts.MultiSurface;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.gml3.GMLSchema;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MultiSurfaceTypeBindingTest extends GML3TestSupport {
    @Test
    public void testEncode() throws Exception {
        Geometry geometry = GML3MockData.multiSurface();
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiSurface);
        // print(dom);
        assertEquals("geometry", getID(dom.getDocumentElement()));
        assertEquals(
                2, dom.getElementsByTagNameNS(GML.NAMESPACE, "surfaceMember").getLength());
        NodeList children = dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Polygon.getLocalPart());
        assertEquals(2, children.getLength());
        assertEquals("geometry.1", getID(children.item(0)));
        assertEquals("geometry.2", getID(children.item(1)));
    }

    @Test
    public void testParseWithSurfaceMember() throws Exception {
        GML3MockData.multiSurface(document, document);
        MultiSurface mpoly = (MultiSurface) parse();

        assertEquals(2, mpoly.getNumGeometries());
    }

    @Test
    public void testParseWithSurfaceMembers() throws Exception {
        GML3MockData.multiSurface(document, document, false);
        MultiSurface mpoly = (MultiSurface) parse();

        assertEquals(2, mpoly.getNumGeometries());
    }

    @Test
    public void testMultiSurfaceTypeAssignable() {
        Assert.assertTrue(GMLSchema.MULTISURFACETYPE_TYPE.getBinding().isAssignableFrom(MultiSurface.class));
    }
}
