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
package org.geotools.feature.type;

import java.util.Collection;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AssociationDescriptor;
import org.geotools.api.feature.type.AssociationType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.Schema;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;

/**
 * This implementation is capable of creating a good default implementation of the Types used in the feature model.
 *
 * <p>The implementation focus here is on corretness rather then efficiency or even strict error messages. The code
 * serves as a good example, but is not optimized for any particular use.
 *
 * @author Jody Garnett
 */
public class FeatureTypeFactoryImpl implements FeatureTypeFactory {
    /** Used for spatial content */
    CRSFactory crsFactory;

    /** Used for type restrictions */
    FilterFactory filterFactory;

    /** Rely on setter injection */
    public FeatureTypeFactoryImpl() {
        this.crsFactory = null;
        this.filterFactory = null;
    }
    /** Constructor injection */
    public FeatureTypeFactoryImpl(CRSFactory crsFactory, FilterFactory filterFactory) {
        this.crsFactory = crsFactory;
        this.filterFactory = filterFactory;
    }

    @Override
    public Schema createSchema(String uri) {
        return new SchemaImpl(uri);
    }

    public CRSFactory getCRSFactory() {
        return crsFactory;
    }

    public void setCRSFactory(CRSFactory crsFactory) {
        this.crsFactory = crsFactory;
    }

    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    @Override
    public AssociationDescriptor createAssociationDescriptor(
            AssociationType type, Name name, int minOccurs, int maxOccurs, boolean isNillable) {

        return new AssociationDescriptorImpl(type, name, minOccurs, maxOccurs, isNillable);
    }

    @Override
    public AttributeDescriptor createAttributeDescriptor(
            AttributeType type, Name name, int minOccurs, int maxOccurs, boolean isNillable, Object defaultValue) {

        return new AttributeDescriptorImpl(type, name, minOccurs, maxOccurs, isNillable, defaultValue);
    }

    @Override
    public GeometryDescriptor createGeometryDescriptor(
            GeometryType type, Name name, int minOccurs, int maxOccurs, boolean isNillable, Object defaultValue) {
        return new GeometryDescriptorImpl(type, name, minOccurs, maxOccurs, isNillable, defaultValue);
    }

    @Override
    public AssociationType createAssociationType(
            Name name,
            AttributeType relatedType,
            boolean isAbstract,
            List<Filter> restrictions,
            AssociationType superType,
            InternationalString description) {

        return new AssociationTypeImpl(name, relatedType, isAbstract, restrictions, superType, description);
    }

    @Override
    public AttributeType createAttributeType(
            Name name,
            Class<?> binding,
            boolean isIdentifiable,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {

        return new AttributeTypeImpl(name, binding, isIdentifiable, isAbstract, restrictions, superType, description);
    }

    @Override
    public ComplexType createComplexType(
            Name name,
            Collection<PropertyDescriptor> schema,
            boolean isIdentifiable,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {
        return new ComplexTypeImpl(name, schema, isIdentifiable, isAbstract, restrictions, superType, description);
    }

    @Override
    public GeometryType createGeometryType(
            Name name,
            Class<?> binding,
            CoordinateReferenceSystem crs,
            boolean isIdentifiable,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {

        return new GeometryTypeImpl(
                name, binding, crs, isIdentifiable, isAbstract, restrictions, superType, description);
    }

    @Override
    public FeatureType createFeatureType(
            Name name,
            Collection<PropertyDescriptor> schema,
            GeometryDescriptor defaultGeometry,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {

        return new FeatureTypeImpl(name, schema, defaultGeometry, isAbstract, restrictions, superType, description);
    }

    @Override
    public SimpleFeatureType createSimpleFeatureType(
            Name name,
            List<AttributeDescriptor> schema,
            GeometryDescriptor defaultGeometry,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {

        return new SimpleFeatureTypeImpl(
                name, schema, defaultGeometry, isAbstract, restrictions, superType, description);
    }
}
