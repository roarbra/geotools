/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.EncoderDelegate;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.AttributesImpl;

public class PropertyTypeBinding extends AbstractComplexEMFBinding {

    public PropertyTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return WFS.PropertyType;
    }

    @Override
    public Class<?> getType() {
        return PropertyType.class;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        return value;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(final Object object, QName name) throws Exception {
        if (WFS.Value.equals(name)) {
            return (EncoderDelegate) output -> {
                Object value = ((PropertyType) object).getValue();

                output.startElement(
                        WFS.NAMESPACE,
                        WFS.Value.getLocalPart(),
                        "wfs:" + WFS.Value.getLocalPart(),
                        new AttributesImpl());
                if (value instanceof Geometry) {
                    Encoder encoder = new Encoder(new GMLConfiguration());
                    encoder.setInline(true);
                    encoder.encode(value, GML._Geometry, output);
                } else {
                    String s = value.toString();
                    output.characters(s.toCharArray(), 0, s.length());
                }
                output.endElement(WFS.NAMESPACE, WFS.Value.getLocalPart(), "wfs:" + WFS.Value.getLocalPart());
            };
        }

        return super.getProperty(object, name);
    }
}
