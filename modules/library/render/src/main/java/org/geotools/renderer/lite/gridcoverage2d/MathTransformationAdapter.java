/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.piecewise.MathTransformation;
import it.geosolutions.jaiext.piecewise.Position;
import java.text.MessageFormat;
import org.geotools.api.referencing.operation.MathTransform1D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.renderer.i18n.ErrorKeys;

/**
 * Adapter class for {@link MathTransform1D}.
 *
 * <p>Simple adapter for {@link MathTransform1D} it provides some convenience methods for implementors.
 *
 * <p>Note that it throw an {@link UnsupportedOperationException} for the operations that must be implemented by
 * implementors, namely:
 *
 * <ol>
 *   <li>transform methods
 *   <li>inverse methods
 *   <li>derivative methods
 * </ol>
 *
 * @author Simone Giannecchini, GeoSolutions.
 */
public class MathTransformationAdapter implements MathTransformation {

    /**
     * Makes sure that an argument is non-null.
     *
     * @param name Argument name.
     * @param object User argument.
     * @throws IllegalArgumentException if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform1D#derivative(double)
     */
    @Override
    public double derivative(double value) throws TransformException {

        throw new UnsupportedOperationException(MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform1D#transform(double)
     */
    @Override
    public double transform(double value) {

        throw new UnsupportedOperationException(MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform#getSourceDimensions()
     */
    @Override
    public int getSourceDimensions() {

        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform#getTargetDimensions()
     */
    @Override
    public int getTargetDimensions() {

        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform#inverse()
     */
    @Override
    public MathTransformation inverseTransform() {
        throw new UnsupportedOperationException(MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.referencing.operation.MathTransform#isIdentity()
     */
    @Override
    public boolean isIdentity() {

        throw new UnsupportedOperationException(MessageFormat.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "isIdentity"));
    }

    @Override
    public Position transform(Position ptSrc, Position ptDst) {
        ensureNonNull("ptSrc", ptSrc);
        if (ptDst == null) {
            ptDst = new Position();
        }
        ptDst.setOrdinatePosition(transform(ptSrc.getOrdinatePosition()));
        return ptDst;
    }
}
