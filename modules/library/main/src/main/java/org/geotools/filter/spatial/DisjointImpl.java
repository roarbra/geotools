/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.spatial;

import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.Disjoint;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

public class DisjointImpl extends AbstractPreparedGeometryFilter implements Disjoint {

    public DisjointImpl(Expression e1, Expression e2) {
        super(e1, e2);
    }

    public DisjointImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
    }

    @Override
    public boolean evaluateInternal(Geometry left, Geometry right) {

        switch (literals) {
            case BOTH:
                return cacheValue;
            case RIGHT: {
                return rightPreppedGeom.disjoint(left);
            }
            case LEFT: {
                return leftPreppedGeom.disjoint(right);
            }
            default: {
                return basicEvaluate(left, right);
            }
        }
    }

    @Override
    protected boolean basicEvaluate(Geometry left, Geometry right) {
        Envelope envLeft = left.getEnvelopeInternal();
        Envelope envRight = right.getEnvelopeInternal();

        if (envRight.intersects(envLeft)) return left.disjoint(right);

        return true;
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
