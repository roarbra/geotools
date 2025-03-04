/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;

/**
 * A CSBuilder that generates DefaultCoordinateSequence objects, that is, coordinate sequences backed by a Coordinate[]
 * array.
 *
 * @author wolf
 */
public class DefaultCSBuilder implements CSBuilder {

    private Coordinate[] coordinateArray;
    private CoordinateSequenceFactory factory = CoordinateArraySequenceFactory.instance();

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#start(int, int) */
    @Override
    public void start(int size, int dimensions) {
        coordinateArray = new Coordinate[size];
        for (int i = 0; i < size; i++) coordinateArray[i] = new Coordinate();
    }

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#getCoordinateSequence() */
    @Override
    public CoordinateSequence end() {
        CoordinateSequence cs = factory.create(coordinateArray);
        coordinateArray = null;
        return cs;
    }

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(double, int, int) */
    @Override
    public void setOrdinate(double value, int ordinateIndex, int coordinateIndex) {
        Coordinate c = coordinateArray[coordinateIndex];
        switch (ordinateIndex) {
            case 0:
                c.x = value;
                break;
            case 1:
                c.y = value;
                break;
            case 2:
                c.setZ(value);
                break;
        }
    }

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#getOrdinate(int, int) */
    @Override
    public double getOrdinate(int ordinateIndex, int coordinateIndex) {
        Coordinate c = coordinateArray[coordinateIndex];
        switch (ordinateIndex) {
            case 0:
                return c.x;
            case 1:
                return c.y;
            case 2:
                return c.getZ();
            default:
                return 0.0;
        }
    }

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#getSize() */
    @Override
    public int getSize() {
        if (coordinateArray != null) {
            return coordinateArray.length;
        } else {
            return -1;
        }
    }

    /** @see org.geotools.geometry.coordinatesequence.CSBuilder#getDimension() */
    @Override
    public int getDimension() {
        if (coordinateArray != null) {
            return 2;
        } else {
            return -1;
        }
    }

    /**
     * @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(org.locationtech.jts.geom.CoordinateSequence,
     *     double, int, int)
     */
    @Override
    public void setOrdinate(CoordinateSequence sequence, double value, int ordinateIndex, int coordinateIndex) {
        Coordinate c = sequence.getCoordinate(coordinateIndex);
        switch (ordinateIndex) {
            case 0:
                c.x = value;
                break;
            case 1:
                c.y = value;
                break;
            case 2:
                c.setZ(value);
                break;
        }
    }
}
