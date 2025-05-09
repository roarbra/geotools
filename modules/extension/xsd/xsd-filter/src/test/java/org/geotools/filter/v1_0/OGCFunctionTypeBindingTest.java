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

import org.geotools.api.filter.expression.Function;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;

public class OGCFunctionTypeBindingTest extends FilterTestSupport {
    @Test
    public void testType() {
        assertEquals(Function.class, binding(OGC.FunctionType).getType());
    }

    @Test
    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.FunctionType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.function(document, document);

        Function function = (Function) parse();

        assertEquals("min", function.getName());
        assertEquals(2, function.getParameters().size());
    }

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.function(), OGC.Function);

        assertEquals("min", doc.getDocumentElement().getAttribute("name"));
        assertEquals(
                1,
                doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart())
                        .getLength());
    }
}
