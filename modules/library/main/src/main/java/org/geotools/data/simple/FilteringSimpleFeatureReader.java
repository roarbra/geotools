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
 */
package org.geotools.data.simple;

import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.FilteringFeatureReader;

/** Filter simple reader content as it is being read. */
public class FilteringSimpleFeatureReader extends FilteringFeatureReader<SimpleFeatureType, SimpleFeature>
        implements SimpleFeatureReader {

    public FilteringSimpleFeatureReader(SimpleFeatureReader featureReader, Filter filter) {
        super(featureReader, filter);
    }
}
