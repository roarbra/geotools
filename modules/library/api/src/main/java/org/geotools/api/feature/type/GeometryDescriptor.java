/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.feature.type;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

/**
 * Describes an instance of a geometry attribute.
 *
 * <p>This interface adds no additional methods, the point of it is convenience to type narrow {@link #getType()} to
 * {@link GeometryType}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface GeometryDescriptor extends AttributeDescriptor {
    /** Override of {@link AttributeDescriptor#getType()} which type narrows to {@link GeometryType}. */
    @Override
    GeometryType getType();

    /**
     * The coordinate reference system in which these geometries are defined.
     *
     * <p>This method may return <code>null</code>, but this should only occur in cases where the actual crs is not
     * known. A common case is when a shapefile does not have an accompanied .prj file.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();
}
