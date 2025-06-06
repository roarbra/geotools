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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

/** @author Cory Horner, Refractions Research Inc. */
public class JenksFunctionTest extends FunctionTestSupport {
    @Test
    public void testInstance() {
        Function equInt = ff.function("Jenks", ff.literal(new DefaultFeatureCollection()));
        assertNotNull(equInt);
    }

    @Test
    public void testGetName() {
        Function qInt = ff.function("Jenks", ff.literal(new DefaultFeatureCollection()));
        assertEquals("Jenks", qInt.getName());
    }

    @Test
    public void testSetParameters() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName expr = ff.property("foo");
        JenksNaturalBreaksFunction func = (JenksNaturalBreaksFunction) ff.function("Jenks", expr, classes);
        assertEquals(3, func.getClasses());
        classes = ff.literal(12);
        func = (JenksNaturalBreaksFunction) ff.function("Jenks", expr, classes);
        assertEquals(12, func.getClasses());
        // deprecated still works?
        classes = ff.literal(5);
        func = (JenksNaturalBreaksFunction) ff.function("Jenks", expr, classes);
        assertEquals(5, func.getClasses());
    }

    // rework to test with Jenks71 data
    // answer (from R) is [15.57,41.2] (41.2,60.66] (60.66,77.29] (77.29,100.1] (100.1,155.3]
    @Test
    public void testEvaluateRealData() throws Exception {

        Literal classes = ff.literal(5);
        PropertyName exp = ff.property("jenks71");
        Function func = ff.function("Jenks", exp, classes);

        Object value = func.evaluate(jenksCollection);
        assertNotNull(value);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;
        assertEquals(5, ranged.getSize());
        assertEquals("15.57..41.2", ranged.getTitle(0));
        assertEquals("41.2..60.66", ranged.getTitle(1));
        assertEquals("60.66..77.29", ranged.getTitle(2));
        assertEquals("77.29..100.1", ranged.getTitle(3));
        assertEquals("100.1..155.3", ranged.getTitle(4));
    }

    @Test
    public void testEvaluateWithExpressions() throws Exception {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        Function func = ff.function("Jenks", exp, classes);

        Object value = func.evaluate(featureCollection);
        assertNotNull(value);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;

        // the values being quantiled are
        // {4,90,20,43,29,61,8,12};
        // so there should be two groups:
        // {4, 8, 12, 20} 4 <= x < 29
        // {29, 43, 61, 90} 29 <= x <= 90
        assertEquals(2, ranged.getSize());
        assertEquals("4..29", ranged.getTitle(0));
        assertEquals("29..90", ranged.getTitle(1));
    }

    /**
     * Test a feature collection where each feature will be in it's own bin.
     *
     * <p>Creates a feature collection with five features 1-5. Then uses the quantile function to put these features in
     * 5 bins. Each bin should have a single feature.
     */
    @Test
    public void testSingleBin() throws Exception {

        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType = DataUtilities.createType("classification.test1", "id:0,value:int");
        int[] iVal = {1, 2, 3, 4, 5};
        SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
        for (int i = 0; i < iVal.length; i++) {
            myfeatures[i] = SimpleFeatureBuilder.build(
                    dataType,
                    new Object[] {Integer.valueOf(i + 1), Integer.valueOf(iVal[i])},
                    "classification.test1" + (i + 1));
        }
        SimpleFeatureSource source = DataUtilities.source(myfeatures);
        SimpleFeatureCollection myFeatureCollection = source.getFeatures();

        // run the quantile function
        org.geotools.api.filter.expression.Expression function =
                ff.function("Jenks", ff.property("value"), ff.literal(5));
        Classifier classifier = (Classifier) function.evaluate(myFeatureCollection);

        // verify the results
        assertNotNull(classifier);
        assertEquals(classifier.getClass(), RangedClassifier.class);
        RangedClassifier range = (RangedClassifier) classifier;
        assertEquals(5, range.getSize());

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1d, ((Number) range.getMin(i)).doubleValue(), 0d);
            if (i != 4) {
                assertEquals("wrong value for max", i + 2, ((Number) range.getMax(i)).intValue());
                assertEquals("bad title", (i + 1) + ".." + (i + 2), range.getTitle(i));
            } else {
                assertEquals("wrong value for max", i + 1, ((Number) range.getMax(i)).intValue());

                assertEquals("bad title", (i + 1) + ".." + (i + 1), range.getTitle(i));
            }
        }
    }

    @Test
    public void test2() throws Exception {
        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType = DataUtilities.createType("classification.test1", "id:0,value:int");
        int[] iVal = {1, 2, 3, 4, 5, 6};
        SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
        for (int i = 0; i < iVal.length; i++) {
            myfeatures[i] = SimpleFeatureBuilder.build(
                    dataType,
                    new Object[] {Integer.valueOf(i + 1), Integer.valueOf(iVal[i])},
                    "classification.t" + (i + 1));
        }
        SimpleFeatureSource source = DataUtilities.source(myfeatures);
        SimpleFeatureCollection myFeatureCollection = source.getFeatures();

        // run the quantile function
        org.geotools.api.filter.expression.Expression function =
                ff.function("Jenks", ff.property("value"), ff.literal(5));
        Classifier classifier = (Classifier) function.evaluate(myFeatureCollection);
        assertThat(classifier, CoreMatchers.instanceOf(RangedClassifier.class));
    }

    @Test
    public void testNullNaNHandling() throws Exception {
        // create a feature collection
        SimpleFeatureType ft = DataUtilities.createType("classification.nullnan", "id:0,foo:int,bar:double");
        Integer[] iVal = {
            Integer.valueOf(0),
            Integer.valueOf(0),
            Integer.valueOf(0),
            Integer.valueOf(13),
            Integer.valueOf(13),
            Integer.valueOf(13),
            null,
            null,
            null
        };
        Double[] dVal = {
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null,
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null,
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null
        };

        SimpleFeature[] testFeatures = new SimpleFeature[iVal.length];

        for (int i = 0; i < iVal.length; i++) {
            testFeatures[i] = SimpleFeatureBuilder.build(
                    ft,
                    new Object[] {
                        Integer.valueOf(i + 1), iVal[i], dVal[i],
                    },
                    "nantest.t" + (i + 1));
        }
        SimpleFeatureSource source = DataUtilities.source(testFeatures);
        SimpleFeatureCollection thisFC = source.getFeatures();

        // create the expression
        Divide divide = ff.divide(ff.property("foo"), ff.property("bar"));
        JenksNaturalBreaksFunction qf = (JenksNaturalBreaksFunction) ff.function("Jenks", divide, ff.literal(3));

        RangedClassifier range = (RangedClassifier) qf.evaluate(thisFC);
        assertEquals(3, range.getSize()); // 2 or 3?
        assertEquals("0..0", range.getTitle(0));
        assertEquals("0..0", range.getTitle(1));
    }

    @Test
    public void testConstantValuesNumeric() {
        Function function = ff.function("jenks", ff.property("v"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(123.123, (Double) classifier.getMin(0), 0d);
        assertEquals(123.123, (Double) classifier.getMax(0), 0d);
    }

    @Test
    public void testEvaluateNumericalWithPercentages() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName exp = ff.property("foo");
        Function func = ff.function("Jenks", exp, classes, ff.literal(true));
        Object value = func.evaluate(featureCollection);
        assertNotNull(value);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;
        double[] percentages = ranged.getPercentages();
        assertEquals(percentages.length, 3);
        assertEquals(percentages[0], 37.5, 0d);
        assertEquals(percentages[1], 25.0, 0d);
        assertEquals(percentages[2], 37.5, 0d);
    }
}
