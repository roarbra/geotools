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
package org.geotools.filter.visitor;

import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;

/**
 * Check if an expression is static (ie does not contain a PropertyName expression).
 *
 * <p>This visitor will "short-circuit" the moment it finds a PropertyName expression and will not need to visit the
 * entire data structure.
 *
 * <p>Example:
 *
 * <pre><code>
 * if( filter.accepts( IsStaticExpressionVisitor.VISITOR, null ) ){
 *     Color color = expression.evaulate( null, Color.class );
 *     ...
 * }
 * </code></pre>
 *
 * @author Jody
 */
public class IsStaticExpressionVisitor implements ExpressionVisitor {
    public static final IsStaticExpressionVisitor VISITOR = new IsStaticExpressionVisitor();

    /** visit each expression and check that they are static */
    protected IsStaticExpressionVisitor() {}
    /** visit each expression and check that they are static */
    @Override
    public Boolean visit(NilExpression expression, Object data) {
        return true;
    }
    /** visit each expression and check that they are static */
    @Override
    public Boolean visit(Add expression, Object data) {
        boolean isStatic = (Boolean) expression.getExpression1().accept(this, data);
        if (isStatic == false) return false;
        isStatic = (Boolean) expression.getExpression2().accept(this, data);
        return isStatic;
    }
    /** visit each expression and check that they are static */
    @Override
    public Boolean visit(Divide expression, Object data) {
        boolean isStatic = (Boolean) expression.getExpression1().accept(this, data);
        if (isStatic == false) return false;
        isStatic = (Boolean) expression.getExpression2().accept(this, data);
        return isStatic;
    }
    /** Visit each parameter and check if they are static */
    @Override
    public Boolean visit(Function expression, Object data) {
        boolean isStatic = true;
        if (expression.getParameters() != null) {
            for (Expression parameter : expression.getParameters()) {
                isStatic = (Boolean) parameter.accept(this, data);
                if (isStatic == false) break;
            }
        }
        return isStatic;
    }
    /**
     * Literal expressions are always static.
     *
     * @return true
     */
    @Override
    public Boolean visit(Literal expression, Object data) {
        return true;
    }
    /**
     * visit each expression and check that they are static.
     *
     * @return true if getExpression1 and getExpression2 are static
     */
    @Override
    public Boolean visit(Multiply expression, Object data) {
        boolean isStatic = (Boolean) expression.getExpression1().accept(this, data);
        if (isStatic == false) return false;
        isStatic = (Boolean) expression.getExpression2().accept(this, data);
        return isStatic;
    }
    /**
     * If even a single PropertyName is found in the expression the expression is not static.
     *
     * @return false
     */
    @Override
    public Boolean visit(PropertyName expression, Object data) {
        return false;
    }
    /**
     * visit each expression and check that they are static.
     *
     * @return true if getExpression1 and getExpression2 are static
     */
    @Override
    public Boolean visit(Subtract expression, Object data) {
        boolean isStatic = (Boolean) expression.getExpression1().accept(this, data);
        if (isStatic == false) return false;
        isStatic = (Boolean) expression.getExpression2().accept(this, data);
        return isStatic;
    }
}
