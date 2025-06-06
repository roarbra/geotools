/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.CENTRAL_MERIDIAN;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.FALSE_EASTING;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.FALSE_NORTHING;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.LATITUDE_OF_ORIGIN;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.SCALE_FACTOR;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.SEMI_MAJOR;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.SEMI_MINOR;
import static org.junit.Assert.assertEquals;

import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Test;

/** Unit tests of {@link TransverseMercator}. */
public class TransverseMercatorTest {

    /**
     * Tests the example provided by the EPSG guidance. See "OGP Surveying and Positioning Guidance Note number 7, part
     * 2 – April 2018", pages 53-57
     */
    @Test
    public void testEpsgExample() throws Exception {
        MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup parameters = mtFactory.getDefaultParameters("Transverse_Mercator");

        // build the transformation using the guidance provided values
        parameter(SEMI_MAJOR, parameters).setValue(6377563.396);
        parameter(SEMI_MINOR, parameters).setValue(6356256.910);
        parameter(LATITUDE_OF_ORIGIN, parameters).setValue(dmsToDegree(49, 0, 0));
        parameter(CENTRAL_MERIDIAN, parameters).setValue(-dmsToDegree(2, 0, 0));
        parameter(SCALE_FACTOR, parameters).setValue(0.9996012717);
        parameter(FALSE_EASTING, parameters).setValue(400000.00);
        parameter(FALSE_NORTHING, parameters).setValue(-100000.00);
        MathTransform transform = mtFactory.createParameterizedTransform(parameters);

        // results as provided by the EPSG guidance
        final double[] point = {dmsToDegree(0, 30, 0), dmsToDegree(50, 30, 0)};
        final double[] expected = {577274.99, 69740.50};

        // check forward transform
        final double[] forward = new double[2];
        transform.transform(point, 0, forward, 0, 1);
        assertEquals(expected[0], forward[0], 1e-1);
        assertEquals(expected[1], forward[1], 1e-1);

        // check inverse transform
        final double[] inverse = new double[2];
        transform.inverse().transform(expected, 0, inverse, 0, 1);
        assertEquals(point[0], inverse[0], 1e-4);
        assertEquals(inverse[1], inverse[1], 1e-4);
    }

    /** Extracts the {@link ParameterValue} for a certain {@link ParameterDescriptor} */
    ParameterValue<?> parameter(ParameterDescriptor<?> param, ParameterValueGroup group) {
        return group.parameter(param.getName().getCode());
    }

    /** Converts a DMS value into degrees */
    double dmsToDegree(double degrees, double minutes, double seconds) {
        return degrees + (minutes + seconds / 60) / 60;
    }
}
