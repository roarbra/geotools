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
package org.geotools.data.store;

import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;

/**
 * FeatureIterator wrapper which re-types features on the fly based on a target feature type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ReTypingFeatureIterator implements SimpleFeatureIterator {

    /** The delegate iterator */
    SimpleFeatureIterator delegate;

    /** The target feature type */
    SimpleFeatureType target;

    /** The matching types from target */
    AttributeDescriptor[] types;

    SimpleFeatureBuilder builder;

    public ReTypingFeatureIterator(SimpleFeatureIterator delegate, SimpleFeatureType source, SimpleFeatureType target) {
        this.delegate = delegate;
        this.target = target;
        types = typeAttributes(source, target);
        this.builder = new SimpleFeatureBuilder(target);
    }

    public SimpleFeatureIterator getDelegate() {
        return delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() {
        SimpleFeature next = delegate.next();
        String id = next.getID();

        try {
            for (AttributeDescriptor type : types) {
                final String xpath = type.getLocalName();
                builder.add(next.getAttribute(xpath));
            }
            builder.featureUserData(next);

            return builder.buildFeature(id);
        } catch (IllegalAttributeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supplies mapping from origional to target FeatureType.
     *
     * <p>Will also ensure that origional can cover target
     *
     * @param target Desired FeatureType
     * @param original Origional FeatureType
     * @return Mapping from originoal to target FeatureType
     * @throws IllegalArgumentException if unable to provide a mapping
     */
    protected AttributeDescriptor[] typeAttributes(SimpleFeatureType original, SimpleFeatureType target) {
        if (target.equals(original)) {
            throw new IllegalArgumentException("FeatureReader allready produces contents with the correct schema");
        }

        if (target.getAttributeCount() > original.getAttributeCount()) {
            throw new IllegalArgumentException(
                    "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover requested type)");
        }

        String xpath;
        AttributeDescriptor[] types = new AttributeDescriptor[target.getAttributeCount()];

        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attrib = target.getDescriptor(i);
            xpath = attrib.getLocalName();
            types[i] = attrib;

            if (!attrib.equals(original.getDescriptor(xpath))) {
                throw new IllegalArgumentException(
                        "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover "
                                + xpath
                                + ")");
            }
        }

        return types;
    }

    @Override
    public void close() {
        delegate.close();
    }
}
