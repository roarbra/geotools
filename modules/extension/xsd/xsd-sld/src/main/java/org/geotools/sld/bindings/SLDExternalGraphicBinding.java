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
package org.geotools.sld.bindings;

import java.net.URI;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.ResourceLocator;
import org.geotools.api.style.StyleFactory;
import org.geotools.util.Converters;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:ExternalGraphic.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="ExternalGraphic"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         An &quot;ExternalGraphic&quot; gives
 *              a reference to an external raster or         vector
 *              graphical object.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:OnlineResource"/&gt;
 *              &lt;xsd:element ref="sld:Format"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDExternalGraphicBinding extends AbstractComplexBinding {
    protected StyleFactory styleFactory;

    protected ResourceLocator resourceLocator;

    public SLDExternalGraphicBinding(StyleFactory styleFactory, ResourceLocator resourceLocator) {
        this.styleFactory = styleFactory;
        this.resourceLocator = resourceLocator;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.EXTERNALGRAPHIC;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
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
        return ExternalGraphic.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // the Converters wrapper here is a workaround for http://jira.codehaus.org/browse/GEOT-2457
        // for some reason on the IBM JDK returns a string, we should really find out why instead
        // of applying this bandaid
        URI uri = Converters.convert(node.getChildValue("OnlineResource"), URI.class);
        String format = (String) node.getChildValue("Format");

        URL url = resourceLocator.locateResource(uri.toString());
        if (url == null) {
            return styleFactory.createExternalGraphic(uri.toString(), format);
        } else {
            return styleFactory.createExternalGraphic(url, format);
        }
    }
}
