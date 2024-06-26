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
package org.geotools.kml.bindings;

import java.net.URI;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Symbolizer;
import org.geotools.kml.KML;
import org.geotools.kml.StyleMap;
import org.geotools.styling.StyleBuilder;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://earth.google.com/kml/2.1:StyleType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType final="#all" name="StyleType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:StyleSelectorType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="kml:IconStyle"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:LabelStyle"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:LineStyle"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:PolyStyle"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:BalloonStyle"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:ListStyle"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class StyleTypeBinding extends AbstractComplexBinding {
    StyleBuilder sb;
    StyleMap styleMap;

    public StyleTypeBinding(StyleBuilder sb, StyleMap styleMap) {
        this.sb = sb;
        this.styleMap = styleMap;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return KML.StyleType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return FeatureTypeStyle.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List<Symbolizer> l = node.getChildValues(Symbolizer.class);
        Symbolizer[] syms = l.toArray(new Symbolizer[l.size()]);

        FeatureTypeStyle style = sb.createFeatureTypeStyle(syms, 1.0, 1.0);

        // if the style has an id, throw it in to the style cache
        if (node.hasAttribute("id")) {
            String id = (String) node.getAttributeValue("id");

            // create a uri with just a fragment
            URI uri = new URI("#" + id);

            styleMap.put(uri, style);
        }

        return style;
    }
}
