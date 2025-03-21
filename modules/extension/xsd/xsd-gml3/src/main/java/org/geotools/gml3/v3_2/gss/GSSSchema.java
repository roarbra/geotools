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

package org.geotools.gml3.v3_2.gss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AbstractLazyAttributeTypeImpl;
import org.geotools.feature.type.AbstractLazyComplexTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.gml3.v3_2.GMLSchema;
import org.geotools.xlink.XLINKSchema;
import org.geotools.xs.XSSchema;

public class GSSSchema extends SchemaImpl {

    /**
     *
     *
     * <pre>
     *   <code>
     *  &lt;xs:complexType name="GM_Object_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractGeometry"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     *
     * @generated
     */
    public static final ComplexType GM_OBJECT_PROPERTYTYPE_TYPE = build_GM_OBJECT_PROPERTYTYPE_TYPE();

    private static ComplexType build_GM_OBJECT_PROPERTYTYPE_TYPE() {
        ComplexType builtType =
                new AbstractLazyComplexTypeImpl(
                        new NameImpl("http://www.isotc211.org/2005/gss", "GM_Object_PropertyType"),
                        false,
                        false,
                        null,
                        null) {
                    @Override
                    public AttributeType buildSuper() {
                        return XSSchema.ANYTYPE_TYPE;
                    }

                    @Override
                    public Collection<PropertyDescriptor> buildDescriptors() {
                        List<PropertyDescriptor> descriptors = new ArrayList<>();
                        descriptors.add(new AttributeDescriptorImpl(
                                GMLSchema.ABSTRACTGEOMETRYTYPE_TYPE,
                                new NameImpl("http://www.opengis.net/gml/3.2", "AbstractGeometry"),
                                1,
                                1,
                                false,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XLINKSchema._ACTUATE_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "actuate"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "arcrole"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "href"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "role"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XLINKSchema._SHOW_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "show"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "title"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "type"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE, new NameImpl("uuidref"), 0, 1, true, null));
                        descriptors.add(new AttributeDescriptorImpl(
                                GMLSchema.NILREASONTYPE_TYPE,
                                new NameImpl("http://www.isotc211.org/2005/gco", "nilReason"),
                                0,
                                1,
                                true,
                                null));
                        return descriptors;
                    }
                };
        return builtType;
    }

    /**
     *
     *
     * <pre>
     *   <code>
     *  &lt;xs:complexType name="GM_Point_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:Point"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     *
     * @generated
     */
    public static final ComplexType GM_POINT_PROPERTYTYPE_TYPE = build_GM_POINT_PROPERTYTYPE_TYPE();

    private static ComplexType build_GM_POINT_PROPERTYTYPE_TYPE() {
        ComplexType builtType =
                new AbstractLazyComplexTypeImpl(
                        new NameImpl("http://www.isotc211.org/2005/gss", "GM_Point_PropertyType"),
                        false,
                        false,
                        null,
                        null) {
                    @Override
                    public AttributeType buildSuper() {
                        return XSSchema.ANYTYPE_TYPE;
                    }

                    @Override
                    public Collection<PropertyDescriptor> buildDescriptors() {
                        List<PropertyDescriptor> descriptors = new ArrayList<>();
                        descriptors.add(new AttributeDescriptorImpl(
                                GMLSchema.POINTTYPE_TYPE,
                                new NameImpl("http://www.opengis.net/gml/3.2", "Point"),
                                1,
                                1,
                                false,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XLINKSchema._ACTUATE_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "actuate"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "arcrole"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "href"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.ANYURI_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "role"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XLINKSchema._SHOW_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "show"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "title"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE,
                                new NameImpl("http://www.w3.org/1999/xlink", "type"),
                                0,
                                1,
                                true,
                                null));
                        descriptors.add(new AttributeDescriptorImpl(
                                XSSchema.STRING_TYPE, new NameImpl("uuidref"), 0, 1, true, null));
                        descriptors.add(new AttributeDescriptorImpl(
                                GMLSchema.NILREASONTYPE_TYPE,
                                new NameImpl("http://www.isotc211.org/2005/gco", "nilReason"),
                                0,
                                1,
                                true,
                                null));
                        return descriptors;
                    }
                };
        return builtType;
    }

    public GSSSchema() {
        super("http://www.isotc211.org/2005/gss");
        put(GM_OBJECT_PROPERTYTYPE_TYPE);
        put(GM_POINT_PROPERTYTYPE_TYPE);
    }

    /**
     * Complete the definition of a type and store it in the schema.
     *
     * <p>This method calls {@link AttributeType#getSuper()} (and {@link ComplexType#getDescriptors()} where applicable)
     * to ensure the construction of the type (a concrete {@link AbstractLazyAttributeTypeImpl} or
     * {@link AbstractLazyComplexTypeImpl} sublass) is complete. This should be sufficient to avoid any nasty
     * thread-safety surprises in code using this schema.
     *
     * @param type the type to complete and store
     */
    private void put(AttributeType type) {
        type.getSuper();
        if (type instanceof ComplexType) {
            ((ComplexType) type).getDescriptors();
        }
        put(type.getName(), type);
    }
}
