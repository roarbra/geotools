/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.VolatileFunction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;

/**
 * Extracts a property from a feature, taking the property name as a parameter
 *
 * @author Andrea Aime - GeoSolutions
 */
public class FilterFunction_property extends FunctionExpressionImpl implements VolatileFunction {

    FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    public static FunctionName NAME = new FunctionNameImpl(
            "property", parameter("propertyValue", Object.class), parameter("propertyName", String.class));

    /** Cache the last PropertyName used in a thead safe way */
    volatile PropertyName lastPropertyName;

    public FilterFunction_property() {
        super(NAME);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        Object result = evaluate(object);
        if (result == null) {
            return null;
        } else {
            return Converters.convert(result, context);
        }
    }

    @Override
    public Object evaluate(Object feature) {
        String name = getExpression(0).evaluate(feature, String.class);

        if (name == null) {
            return null;
        }

        PropertyName pn = lastPropertyName;
        if (pn != null && pn.getPropertyName().equals(name)) {
            return pn.evaluate(feature);
        } else {
            pn = FF.property(name);
            Object result = pn.evaluate(feature);
            lastPropertyName = pn;
            return result;
        }
    }
}
