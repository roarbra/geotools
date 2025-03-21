/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.gml3.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class GeometryPropertyTypeBindingBase extends AbstractComplexBinding {

    private XSDIdRegistry idSet;

    private boolean makeEmpty = false;

    private GML3EncodingUtils encodingUtils;

    public GeometryPropertyTypeBindingBase(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry) {
        this.idSet = idRegistry;
        this.encodingUtils = encodingUtils;
    }

    @Override
    public Class getType() {
        return getGeometryType();
    }

    public Class<? extends Geometry> getGeometryType() {
        return Geometry.class;
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
        return node.getChildValue(getGeometryType());
    }

    /** @see AbstractComplexBinding#encode(java.lang.Object, org.w3c.dom.Document, org.w3c.dom.Element) */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        checkExistingId((Geometry) object);
        return value;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {

        return encodingUtils.GeometryPropertyType_GetProperty((Geometry) object, name, makeEmpty);
    }

    @Override
    public List<Object[]> getProperties(Object object, XSDElementDeclaration element) throws Exception {
        return encodingUtils.GeometryPropertyType_GetProperties((Geometry) object);
    }

    /**
     * Check if the geometry contains a feature which id is pre-existing in the document. If it's true, make the
     * geometry empty and add xlink:href property
     *
     * @param geom The geometry to be checked
     */
    private void checkExistingId(Geometry geom) {
        if (geom != null) {
            String id = GML2EncodingUtils.getID(geom);

            if (id != null && idSet.idExists(id)) {
                // make geometry empty, href will added by getproperty
                makeEmpty = true;

            } else if (id != null) {

                idSet.add(id);
            }
        }
    }
}
