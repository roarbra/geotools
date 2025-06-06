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
package org.geotools.xml.ogc;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.TestData;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.XSISAXHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.xml.sax.Attributes;

/** @author Jesse */
public class GeometryEncoderTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testEncodeChoiceGeometryType() throws Exception {
        File f = TestData.copy(this, "xml/feature-type-choice.xsd");
        URI u = f.toURI();
        XSISAXHandler contentHandler = new XSISAXHandler(u);
        XSISAXHandler.setLogLevel(Level.WARNING);

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        SAXParser parser = spf.newSAXParser();
        parser.parse(f, contentHandler);

        Schema schema = contentHandler.getSchema();
        Element geomElement = schema.getElements()[0].findChildElement("GEOM");
        GeometryFactory factory = new GeometryFactory();
        LinearRing ring = factory.createLinearRing(new Coordinate[] {
            new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(0, 10), new Coordinate(0, 0)
        });
        Polygon polygon = factory.createPolygon(ring, new LinearRing[0]);
        polygon.setUserData("EPSG:4326");
        MultiPolygon geom = factory.createMultiPolygon(new Polygon[] {polygon});
        geom.setUserData("EPSG:4326");
        final StringWriter writer = new StringWriter();
        //        DocumentWriter.writeDocument(geom, schema, writer, new HashMap());

        PrintHandler output = new PrintHandler() {

            @Override
            public void characters(char[] arg0, int arg1, int arg2) throws IOException {
                writer.write(arg0, arg1, arg2);
            }

            @Override
            public void characters(String s) throws IOException {
                writer.write(s);
            }

            @Override
            public void element(URI namespaceURI, String localName, Attributes attributes) throws IOException {}

            @Override
            public void endDocument() throws IOException {}

            @Override
            public void endElement(URI namespaceURI, String localName) throws IOException {
                writer.write("</" + localName + ">");
            }

            @Override
            public Element findElement(Object value) {
                return null;
            }

            @Override
            public Element findElement(String name) {
                return null;
            }

            @Override
            public Schema getDocumentSchema() {
                return null;
            }

            @Override
            public Object getHint(Object key) {
                return null;
            }

            @Override
            public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws IOException {}

            @Override
            public void startDocument() throws IOException {}

            @Override
            public void startElement(URI namespaceURI, String localName, Attributes attributes) throws IOException {
                writer.write("<" + localName);
                if (attributes != null) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        writer.write(" " + attributes.getLocalName(i) + "=" + attributes.getValue(i));
                    }
                }
                writer.write(">");
            }
        };
        geomElement.getType().encode(geomElement, geom, output, new HashMap<>());
        String expected =
                "<GEOM><MultiPolygon srsName=EPSG:4326><polygonMember><Polygon srsName=EPSG:4326><outerBoundaryIs><LinearRing><coordinates decimal=. cs=, ts= >0.0,0.0 10.0,0.0 0.0,10.0 0.0,0.0</coordinates></LinearRing></outerBoundaryIs></Polygon></polygonMember></MultiPolygon></GEOM>";
        Assert.assertEquals(expected, writer.toString());
    }
}
