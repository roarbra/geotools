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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.geometry.Position;
import org.geotools.geometry.Position2D;
import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="PointType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A Point is defined by a single coordinate tuple.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricPrimitiveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;GML supports two different ways to specify the direct poisiton of a point. 1. The "pos" element is of type
 *                                                          DirectPositionType.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;element ref="gml:pos"/&gt;
 *                      &lt;element ref="gml:coordinates"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.1.0 for coordinates with ordinate values that are numbers. Use "pos"
 *                                                                  instead. The "coordinates" element shall only be used for coordinates with ordinates that require a string
 *                                                                  representation, e.g. DMS representations.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                      &lt;element ref="gml:coord"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.0. Use "pos" instead. The "coord" element is included for
 *                                                                  backwards compatibility with GML 2.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                  &lt;/choice&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class PointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public PointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return GML.PointType;
    }

    @Override
    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return Point.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        if (node.hasChild(Position.class)) {
            Position dp = node.getChildValue(Position.class);

            if (dp instanceof Position2D) {
                return gFactory.createPoint(new Coordinate(dp.getOrdinate(0), dp.getOrdinate(1)));
            } else {
                return gFactory.createPoint(new Coordinate(dp.getOrdinate(0), dp.getOrdinate(1), dp.getOrdinate(2)));
            }
        }

        if (node.hasChild(Coordinate.class)) {
            return gFactory.createPoint(node.getChildValue(Coordinate.class));
        }

        if (node.hasChild(CoordinateSequence.class)) {
            return gFactory.createPoint(node.getChildValue(CoordinateSequence.class));
        }

        return null;
    }

    @Override
    public Object getProperty(Object object, QName name) {
        // hack for xlink stuff
        Geometry geometry = (Geometry) object;
        if (geometry.isEmpty()) {
            return null;
        }

        if ("pos".equals(name.getLocalPart())) {
            Point point = (Point) object;
            return point.getCoordinateSequence();
        }

        return null;
    }
}
