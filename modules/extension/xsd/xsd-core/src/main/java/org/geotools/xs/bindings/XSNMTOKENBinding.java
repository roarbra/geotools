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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import org.geotools.xs.XS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:NMTOKEN.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xs:simpleType name="NMTOKEN" id="NMTOKEN"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#NMTOKEN"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:token"&gt;
 *          &lt;xs:pattern value="\c+" id="NMTOKEN.pattern"&gt;
 *              &lt;xs:annotation&gt;
 *                  &lt;xs:documentation
 *                      source="http://www.w3.org/TR/REC-xml#NT-Nmtoken"&gt;
 *                      pattern matches production 7 from the XML spec           &lt;/xs:documentation&gt;
 *              &lt;/xs:annotation&gt;
 *          &lt;/xs:pattern&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class XSNMTOKENBinding extends AbstractSimpleBinding {
    /** @generated */
    @Override
    public QName getTarget() {
        return XS.NMTOKEN;
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
        return String.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        // just return the value passed in
        return value;
    }
}
