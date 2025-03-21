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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.Filter;
import org.geotools.feature.FeatureIterator;

/**
 * Provides an implementation of Iterator that will filter contents using the provided filter.
 *
 * <p>This is a *Generic* iterator not limited to Feature, this will become more interesting as Filter is able to
 * evaulate itself with more things then just Features.
 *
 * <p>This also explains the use of Collection (where you may have expected a FeatureCollection). However <code>
 * FeatureCollectoin.close( iterator )</code> will be called on the internal delgate.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 */
public class FilteredIterator<F extends Feature> implements Iterator<F>, FeatureIterator<F> {
    private Iterator<F> delegate;
    private Filter filter;

    private F next;

    public FilteredIterator(Iterator<F> iterator, Filter filter) {
        this.delegate = iterator;
        this.filter = filter;
    }

    public FilteredIterator(Collection<F> collection, Filter filter) {
        this.delegate = collection.iterator();
        this.filter = filter;
        next = getNext();
    }

    /** Package protected, please use SubFeatureCollection.close( iterator ) */
    @Override
    public void close() {
        if (delegate instanceof FeatureIterator) {
            ((FeatureIterator<?>) delegate).close();
        }
        delegate = null;
        filter = null;
        next = null;
    }

    private F getNext() {
        F item = null;
        while (delegate.hasNext()) {
            item = delegate.next();
            if (filter.evaluate(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public F next() {
        if (next == null) {
            throw new NoSuchElementException();
        }
        F current = next;
        next = getNext();
        return current;
    }

    @Override
    public void remove() {
        if (delegate == null) throw new IllegalStateException();

        delegate.remove();
    }
}
