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
package org.geotools.filter.v1_1.capabilities;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;
import org.geotools.api.filter.capability.SpatialOperator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Binding test case for http://www.opengis.net/ogc:SpatialOperatorType.
 *
 * <p>
 *
 * <pre>
 *   <code>
 *  &lt;xsd:complexType name="SpatialOperatorType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" name="GeometryOperands" type="ogc:GeometryOperandsType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="name" type="ogc:SpatialOperatorNameType"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 *
 * @generated
 */
public class SpatialOperatorTypeBindingTest extends OGCTestSupport {
    @Test
    public void testType() {
        assertEquals(SpatialOperator.class, binding(OGC.SpatialOperatorType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.SpatialOperatorType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.spatialOperator(document, document, "Intersect");

        SpatialOperator sop = (SpatialOperator) parse(OGC.SpatialOperatorType);

        assertEquals("Intersect", sop.getName());
    }

    @Test
    public void testEncode() throws Exception {
        Document dom = encode(
                FilterMockData.spatialOperator("Intersect"),
                new QName(OGC.NAMESPACE, "SpatialOperator"),
                OGC.SpatialOperatorType);
        assertEquals("Intersect", dom.getDocumentElement().getAttribute("name"));
    }
}
