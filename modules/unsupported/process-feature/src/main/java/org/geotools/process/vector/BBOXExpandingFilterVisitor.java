/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A {@link DuplicatingFilterVisitor} which expands the {@link BBOX} of the filter by given distances for each box edge.
 *
 * @author Martin Davis - OpenGeo
 */
public class BBOXExpandingFilterVisitor extends DuplicatingFilterVisitor {
    private double expandMinX;

    private double expandMaxX;

    private double expandMinY;

    private double expandMaxY;

    /**
     * Creates a new expanding filter.
     *
     * @param expandMinX the distance to expand the box X dimension leftwards
     * @param expandMaxX the distance to expand the box X dimension rightwards
     * @param expandMinY the distance to expand the box Y dimension downwards
     * @param expandMaxY the distance to expand the box Y dimension upwards
     */
    public BBOXExpandingFilterVisitor(double expandMinX, double expandMaxX, double expandMinY, double expandMaxY) {
        this.expandMinX = expandMinX;
        this.expandMaxX = expandMaxX;
        this.expandMinY = expandMinY;
        this.expandMaxY = expandMaxY;
    }

    /** Expands the BBOX in the Filter. */
    @Override
    public Object visit(BBOX filter, Object extraData) {
        // no need to change the property name
        Expression propertyName = filter.getExpression1();

        ReferencedEnvelope envelope = ReferencedEnvelope.reference(filter.getBounds());
        ReferencedEnvelope expanded = new ReferencedEnvelope(
                envelope.getMinX() - expandMinX,
                envelope.getMaxX() + expandMinY,
                envelope.getMinY() - expandMaxX,
                envelope.getMaxY() + expandMaxY,
                envelope.getCoordinateReferenceSystem());

        return getFactory(extraData).bbox(propertyName, ff.literal(expanded));
    }
}
