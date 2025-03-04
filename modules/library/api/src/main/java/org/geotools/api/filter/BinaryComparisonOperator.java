/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.filter;

// OpenGIS direct dependencies

import org.geotools.api.filter.expression.Expression;

/**
 * Abstract base class for filters that compare exactly two values against each other. The nature of the comparison is
 * dependent on the subclass.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface BinaryComparisonOperator extends MultiValuedFilter {
    /** Returns the first of the two expressions to be compared by this operator. */
    Expression getExpression1();

    /** Returns the second of the two expressions to be compared by this operator. */
    Expression getExpression2();

    /**
     * Flag controlling wither comparisons are case sensitive.
     *
     * @return <code>true</code> if the comparison is case sensetive, otherwise <code>false</code>.
     */
    boolean isMatchingCase();
}
