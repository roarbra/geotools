/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.filter;

import org.geotools.api.feature.Attribute;
import org.geotools.filter.FunctionExpressionImpl;

public class IDFunctionExpression extends FunctionExpressionImpl {
    public IDFunctionExpression() {
        super("getID");
    }

    @Override
    public Object evaluate(Object obj) {
        if (obj instanceof Attribute) {
            Attribute att = (Attribute) obj;
            return att.getIdentifier();
        }
        return null;
    }
}
