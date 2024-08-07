/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.feature.type;

import java.util.Map;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.4
 */
public class GeometryTypeProxy extends AttributeTypeProxy implements GeometryType {

    public GeometryTypeProxy(final Name typeName, final Map registry) {
        super(typeName, registry);
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return ((GeometryType) getSubject()).getCoordinateReferenceSystem();
    }
}
