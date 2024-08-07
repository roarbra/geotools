/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.util.NoSuchElementException;
import org.geotools.api.feature.Feature;
import org.geotools.feature.FeatureIterator;

/**
 * A feature iterator that completely delegates to another FeatureIterator.
 *
 * @author Jody Garnett
 */
public class DecoratingFeatureIterator<F extends Feature> implements FeatureIterator<F> {
    protected FeatureIterator<F> delegate;

    /**
     * Wrap the provided FeatureIterator.
     *
     * @param iterator Iterator to be used as a delegate.
     */
    public DecoratingFeatureIterator(FeatureIterator<F> iterator) {
        delegate = iterator;
    }

    @Override
    public boolean hasNext() {
        return delegate != null && delegate.hasNext();
    }

    @Override
    public F next() throws NoSuchElementException {
        if (delegate == null) throw new NoSuchElementException();
        return delegate.next();
    }

    @Override
    public void close() {
        if (delegate != null) {
            delegate.close();
        }
        delegate = null;
    }
}
