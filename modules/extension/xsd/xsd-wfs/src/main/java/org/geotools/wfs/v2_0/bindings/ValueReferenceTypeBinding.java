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
package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.util.Converters;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xs.bindings.XSQNameBinding;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ValueReferenceTypeBinding extends AbstractComplexEMFBinding {

    NamespaceContext namespaceContext;

    public ValueReferenceTypeBinding(NamespaceContext namespaceContext) {
        super(Wfs20Factory.eINSTANCE, ValueReferenceType.class);
        this.namespaceContext = namespaceContext;
    }

    @Override
    public QName getTarget() {
        return WFS.PropertyType_ValueReference;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("value".equals(property)) {
            try {
                Object qname = new XSQNameBinding(namespaceContext).parse(null, value);
                super.setProperty(eObject, property, qname, lax);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {
            super.setProperty(eObject, property, value, lax);
        }
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        value.setTextContent(Converters.convert(((ValueReferenceType) object).getValue(), String.class));
        return value;
    }
}
