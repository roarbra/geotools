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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.filter.v1_0.OGCDisjointBinding;
import org.geotools.filter.v2_0.FES;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Disjoint.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:element name="Disjoint" substitutionGroup="fes:spatialOps" type="fes:BinarySpatialOpType"/&gt;
 *
 *        </code>
 *       </pre>
 *
 * @generated
 */
public class DisjointBinding extends OGCDisjointBinding {

    public DisjointBinding(FilterFactory filterFactory, GeometryFactory geometryFactory) {
        super(filterFactory, geometryFactory);
    }

    @Override
    public QName getTarget() {
        return FES.Disjoint;
    }
}
