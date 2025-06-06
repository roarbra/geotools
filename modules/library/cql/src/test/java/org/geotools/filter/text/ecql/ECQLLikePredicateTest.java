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
 */

package org.geotools.filter.text.ecql;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLLikePredicateTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for like predicate
 *
 * <p>
 *
 * <pre>
 *  &lt;text predicate &gt; ::=
 *      &lt;expression &gt; [ NOT ] <b>LIKE</b>  &lt;character pattern &gt;
 *
 * </pre>
 *
 * <p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class ECQLLikePredicateTest extends CQLLikePredicateTest {

    public ECQLLikePredicateTest() {
        super(Language.ECQL);
    }

    /**
     * Test Text Predicate
     *
     * <p>Sample: strConcat('aa', 'bbcc') like '%bb%'
     */
    @Test
    public void functionlikePredicate() throws Exception {

        // Like strConcat('aa', 'bbcc') like '%bb%'
        Filter resultFilter = parseFilter(FilterECQLSample.FUNCTION_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike expected =
                (PropertyIsLike) FilterECQLSample.getSample(FilterECQLSample.FUNCTION_LIKE_ECQL_PATTERN);

        Assert.assertEquals("like filter was expected", expected, resultFilter);

        // test for strToUpperCase function
        resultFilter = parseFilter("strToUpperCase(anAttribute) like '%BB%'");

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike resultLike = (PropertyIsLike) resultFilter;

        Expression resultExpression = resultLike.getExpression();
        Assert.assertTrue(resultExpression instanceof Function);

        Function resultFunction = (Function) resultExpression;
        Assert.assertEquals("strToUpperCase", resultFunction.getName());

        Assert.assertEquals(resultLike.getLiteral(), "%BB%");
    }

    /** Test like using a pattern with spanish caracters */
    @Test
    public void functionAndPatternWithSpanishCharacter() throws CQLException {

        Filter resultFilter = parseFilter("strToUpperCase(anAttribute) like '%año%'");

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike resultLike = (PropertyIsLike) resultFilter;

        Expression resultExpression = resultLike.getExpression();
        Assert.assertTrue(resultExpression instanceof Function);

        Function resultFunction = (Function) resultExpression;
        Assert.assertEquals("strToUpperCase", resultFunction.getName());

        Assert.assertEquals(resultLike.getLiteral(), "%año%");
    }

    @Test
    public void likePredicateCaseSensitive() throws Exception {

        // iLike
        Filter resultFilter = parseFilter("ATTR1 LIKE 'abc%'");

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof LikeFilterImpl);
        LikeFilterImpl likeFilter = (LikeFilterImpl) resultFilter;

        Assert.assertTrue(likeFilter.isMatchingCase());
    }

    @Test
    public void ilikePredicate() throws Exception {
        // iLike
        Filter resultFilter = parseFilter("ATTR1 ILIKE 'abc%'");

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof LikeFilterImpl);
        LikeFilterImpl likeFilter = (LikeFilterImpl) resultFilter;

        Assert.assertFalse(likeFilter.isMatchingCase());
    }

    @Test
    public void notilikePredicate() throws Exception {

        // not iLike
        Filter resultFilter = parseFilter("not ATTR1 ILIKE 'abc%'");

        Not notFilter = (Not) resultFilter;

        LikeFilterImpl likeFilter = (LikeFilterImpl) notFilter.getFilter();

        Assert.assertFalse(likeFilter.isMatchingCase());
    }

    /**
     * Test Text Predicate
     *
     * <p>Sample: 'aabbcc' like '%bb%'
     */
    @Test
    public void literallikePredicate() throws Exception {

        Filter resultFilter = parseFilter(FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike expected =
                (PropertyIsLike) FilterECQLSample.getSample(FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN);

        Assert.assertEquals("like filter was expected", expected, resultFilter);
    }

    @Test
    public void literalNotlikePredicate() throws Exception {

        Filter resultFilter = parseFilter(FilterECQLSample.LITERAL_NOT_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof Not);

        Not expected = (Not) FilterECQLSample.getSample(FilterECQLSample.LITERAL_NOT_LIKE_ECQL_PATTERN);

        Assert.assertTrue(expected.getFilter() instanceof PropertyIsLike);

        Assert.assertEquals("like filter was expected", expected, resultFilter);
    }
}
