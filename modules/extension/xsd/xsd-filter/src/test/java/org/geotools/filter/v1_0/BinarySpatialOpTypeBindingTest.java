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
package org.geotools.filter.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BinarySpatialOpTypeBindingTest extends FilterTestSupport {
    @Test
    public void testDistanceBufferType() {
        assertEquals(
                DistanceBufferOperator.class, binding(OGC.DistanceBufferType).getType());
    }

    @Test
    public void testBeyondType() {
        assertEquals(Beyond.class, binding(OGC.Beyond).getType());
    }

    @Test
    public void testBeyondParse() throws Exception {
        FilterMockData.beyond(document, document);

        Beyond beyond = (Beyond) parse();

        assertNotNull(beyond.getExpression1());
        assertNotNull(beyond.getExpression2());
        assertEquals(1.0, beyond.getDistance(), 0.1);
        assertEquals("m", beyond.getDistanceUnits());
    }

    @Test
    public void testBeyondEncode() throws Exception {
        Document dom = encode(FilterMockData.beyond(), OGC.Beyond);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
        assertEquals(1, dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").getLength());
        assertEquals(
                "1.0",
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Distance.getLocalPart())
                        .item(0)
                        .getFirstChild()
                        .getNodeValue());
        assertEquals(
                "m",
                ((Element) dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Distance.getLocalPart())
                                .item(0))
                        .getAttribute("units"));
    }

    @Test
    public void testDWithinType() {
        assertEquals(DWithin.class, binding(OGC.DWithin).getType());
    }

    @Test
    public void testDWithinParse() throws Exception {
        FilterMockData.dwithin(document, document);

        DWithin dwithin = (DWithin) parse();

        assertNotNull(dwithin.getExpression1());
        assertNotNull(dwithin.getExpression2());
        assertEquals(1.0, dwithin.getDistance(), 0.1);
        assertEquals("m", dwithin.getDistanceUnits());
    }

    @Test
    public void testDWithinEncode() throws Exception {
        Document dom = encode(FilterMockData.beyond(), OGC.DWithin);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
        assertEquals(1, dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").getLength());
        assertEquals(
                "1.0",
                dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance")
                        .item(0)
                        .getFirstChild()
                        .getNodeValue());
    }

    @Test
    public void testBinarySpatialOpType() {
        assertEquals(
                BinarySpatialOperator.class, binding(OGC.BinarySpatialOpType).getType());
    }

    @Test
    public void testContainsType() {
        assertEquals(Contains.class, binding(OGC.Contains).getType());
    }

    @Test
    public void testContainsParse() throws Exception {
        FilterMockData.contains(document, document);

        Contains contains = (Contains) parse();

        assertNotNull(contains.getExpression1());
        assertNotNull(contains.getExpression2());
    }

    @Test
    public void testContainsEncode() throws Exception {
        Document dom = encode(FilterMockData.contains(), OGC.Contains);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testCrossesType() {
        assertEquals(Crosses.class, binding(OGC.Crosses).getType());
    }

    @Test
    public void testCrossesParse() throws Exception {
        FilterMockData.crosses(document, document);

        Crosses crosses = (Crosses) parse();

        assertNotNull(crosses.getExpression1());
        assertNotNull(crosses.getExpression2());
    }

    @Test
    public void testCrossesEncode() throws Exception {
        Document dom = encode(FilterMockData.crosses(), OGC.Crosses);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testDisjointType() {
        assertEquals(Disjoint.class, binding(OGC.Disjoint).getType());
    }

    @Test
    public void testDisjointParse() throws Exception {
        FilterMockData.disjoint(document, document);

        Disjoint disjoint = (Disjoint) parse();

        assertNotNull(disjoint.getExpression1());
        assertNotNull(disjoint.getExpression2());
    }

    @Test
    public void testDisjointEncode() throws Exception {
        Document dom = encode(FilterMockData.disjoint(), OGC.Disjoint);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testEqualsType() {
        assertEquals(Equals.class, binding(OGC.Equals).getType());
    }

    @Test
    public void testEqualsParse() throws Exception {
        FilterMockData.equals(document, document);

        Equals equals = (Equals) parse();

        assertNotNull(equals.getExpression1());
        assertNotNull(equals.getExpression2());
    }

    @Test
    public void testEqualsEncode() throws Exception {
        Document dom = encode(FilterMockData.equals(), OGC.Equals);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testIntersectsType() {
        assertEquals(Intersects.class, binding(OGC.Intersects).getType());
    }

    @Test
    public void testIntersectsParse() throws Exception {
        FilterMockData.intersects(document, document);

        Intersects intersects = (Intersects) parse();

        assertNotNull(intersects.getExpression1());
        assertNotNull(intersects.getExpression2());
    }

    @Test
    public void testIntersectsEncode() throws Exception {
        Document dom = encode(FilterMockData.intersects(), OGC.Intersects);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testOverlapsType() {
        assertEquals(Overlaps.class, binding(OGC.Overlaps).getType());
    }

    @Test
    public void testOverlapsParse() throws Exception {
        FilterMockData.overlaps(document, document);

        Overlaps overlaps = (Overlaps) parse();

        assertNotNull(overlaps.getExpression1());
        assertNotNull(overlaps.getExpression2());
    }

    @Test
    public void testOverlapsEncode() throws Exception {
        Document dom = encode(FilterMockData.overlaps(), OGC.Overlaps);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testTouchesType() {
        assertEquals(Touches.class, binding(OGC.Touches).getType());
    }

    @Test
    public void testTouchesParse() throws Exception {
        FilterMockData.touches(document, document);

        Touches touches = (Touches) parse();

        assertNotNull(touches.getExpression1());
        assertNotNull(touches.getExpression2());
    }

    @Test
    public void testTouchesEncode() throws Exception {
        Document dom = encode(FilterMockData.touches(), OGC.Touches);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testWithinType() {
        assertEquals(Within.class, binding(OGC.Within).getType());
    }

    @Test
    public void testWithinParse() throws Exception {
        FilterMockData.within(document, document);

        Within within = (Within) parse();

        assertNotNull(within.getExpression1());
        assertNotNull(within.getExpression2());
    }

    @Test
    public void testWithinEncode() throws Exception {
        Document dom = encode(FilterMockData.within(), OGC.Within);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart())
                        .getLength());
    }

    @Test
    public void testWithFunctionParse() throws Exception {
        FilterMockData.withinWithFunction(document, document);

        Within within = (Within) parse();

        assertNotNull(within.getExpression1());
        assertNotNull(within.getExpression2());

        assertTrue(within.getExpression2() instanceof Function);
    }

    @Test
    public void testWithFunctionEncode() throws Exception {
        Document dom = encode(FilterMockData.withinWithFunction(), OGC.Within);

        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Function.getLocalPart())
                        .getLength());
    }
}
