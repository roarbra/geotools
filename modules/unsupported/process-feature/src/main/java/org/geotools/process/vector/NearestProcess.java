/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

@DescribeProcess(
        title = "Nearest Feature",
        description =
                "Returns the feature in a given feature collection that has the smallest distance to a given point.")
public class NearestProcess implements VectorProcess {
    private static final Logger LOGGER = Logging.getLogger(NearestProcess.class);

    /**
     * Process the input data set.
     *
     * @param featureCollection the data set
     * @param crs the CRS
     * @param point the given point
     * @return the snapped to feature
     * @throws ProcessException error
     */
    @DescribeResult(name = "result", description = "Nearest feature")
    public FeatureCollection<?, ?> execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    FeatureCollection<?, ?> featureCollection,
            @DescribeParameter(name = "point", description = "Point from which to compute distance")
                    Point point,
            @DescribeParameter(
                            name = "crs",
                            min = 0,
                            description =
                                    "Coordinate reference system of the collection and point (default is the input collection CRS)")
                    CoordinateReferenceSystem crs,
            @DescribeParameter(
                            name = "numFeatures",
                            min = 1,
                            description = "Number of nearest features to return.")
                    int numFeatures)
            throws ProcessException {
        try {
            MathTransform crsTransform = null;
            GeometryDescriptor gd = featureCollection.getSchema().getGeometryDescriptor();
            if (crs != null) {
                if (gd != null) {
                    crsTransform = CRS.findMathTransform(gd.getCoordinateReferenceSystem(), crs);
                }
            } else {
                crs = gd.getCoordinateReferenceSystem();
            }
            if (crs == null) {
                throw new ProcessException(
                        "The CRS parameter was not provided and the feature collection does not have a default one either");
            }

            SimpleFeatureType targetFeatureType =
                    createTargetFeatureType(featureCollection.getSchema(), crs);
            NearestFeatureCollection results =
                    new NearestFeatureCollection(
                            targetFeatureType, point, crsTransform, numFeatures);
            FeatureIterator<?> featureIterator = featureCollection.features();
            try {
                results.checkAppend(featureIterator);
                return results;
            } finally {
                featureIterator.close();
            }
        } catch (ProcessException e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.warning("Error executing method: " + e);
            throw new ProcessException("Error executing method: " + e, e);
        }
    }

    private static class NearestFeatureCollection extends AbstractFeatureCollection {

        private int numFeatures;
        private ArrayList<SimpleFeature> features;
        private MathTransform crsTransform;
        private Geometry point;

        protected NearestFeatureCollection(
                SimpleFeatureType schema,
                Geometry point,
                MathTransform crsTransform,
                int numFeatures) {
            super(schema);
            this.point = point;
            this.numFeatures = numFeatures;
            this.crsTransform = crsTransform;
            this.features = new ArrayList<SimpleFeature>(numFeatures);
        }

        void checkAppend(FeatureIterator<?> originalIterator)
                throws MismatchedDimensionException, TransformException {

            while (originalIterator.hasNext()) {
                SimpleFeature f = (SimpleFeature) originalIterator.next();
                if (f.getDefaultGeometry() == null) continue;

                Geometry geom =
                        (crsTransform != null
                                ? JTS.transform((Geometry) f.getDefaultGeometry(), crsTransform)
                                : (Geometry) f.getDefaultGeometry());

                double nearestDistance = DistanceOp.distance(point, geom);
                Consumer<SimpleFeature> featureConsumer = null;

                if (features.size() == 0) {
                    featureConsumer = (t) -> features.add(t);
                } else {
                    int i = features.size();
                    while (i > 0
                            && (double) features.get(i - 1).getAttribute("nearest_distance")
                                    > nearestDistance) {
                        i--;
                    }
                    if (features.size() < numFeatures) {
                        final int j = i;
                        featureConsumer = (t) -> features.add(j, t);
                    } else if (i < numFeatures) {
                        final int j = i;
                        featureConsumer = (t) -> features.set(j, t);
                    }
                }

                if (featureConsumer != null) {
                    featureConsumer.accept(
                            createTargetFeature(f, this.schema, geom, nearestDistance));
                }
            }
        }

        @Override
        protected Iterator<SimpleFeature> openIterator() {

            return features.iterator();
        }

        @Override
        public int size() {
            return features.size();
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return null;
        }
    }

    /**
     * Create the modified feature type.
     *
     * @param sourceFeatureType the source feature type
     * @param crs Coordinate reference system of the returned features
     * @return the modified feature type
     * @throws ProcessException error
     */
    private static SimpleFeatureType createTargetFeatureType(
            FeatureType sourceFeatureType, CoordinateReferenceSystem crs) throws ProcessException {
        try {
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.setName(sourceFeatureType.getName().getLocalPart());
            typeBuilder.setNamespaceURI(sourceFeatureType.getName().getNamespaceURI());

            Name geomProperty = sourceFeatureType.getGeometryDescriptor().getName();

            for (PropertyDescriptor attbType : sourceFeatureType.getDescriptors()) {
                if (attbType.getName().equals(geomProperty)) {
                    typeBuilder.crs(crs);
                }
                typeBuilder.add((AttributeDescriptor) attbType);
            }
            typeBuilder
                    .minOccurs(1)
                    .maxOccurs(1)
                    .nillable(false)
                    .add("nearest_distance", Double.class);
            typeBuilder.setDefaultGeometry(
                    sourceFeatureType.getGeometryDescriptor().getLocalName());

            return typeBuilder.buildFeatureType();
        } catch (Exception e) {
            LOGGER.warning("Error creating type: " + e);
            throw new ProcessException("Error creating type: " + e, e);
        }
    }

    /**
     * Create the modified feature.
     *
     * @param feature the source feature
     * @param targetFeatureType the modified feature type
     * @param nearestDistance the snap distance
     * @return the modified feature
     * @throws ProcessException error
     */
    private static SimpleFeature createTargetFeature(
            Feature feature,
            SimpleFeatureType targetFeatureType,
            Geometry geom,
            Double nearestDistance)
            throws ProcessException {
        try {
            AttributeDescriptor distanceAttbType =
                    targetFeatureType.getDescriptor("nearest_distance");
            AttributeDescriptor geomAttbType = targetFeatureType.getGeometryDescriptor();

            Object[] attributes = new Object[targetFeatureType.getAttributeCount()];
            for (int i = 0; i < attributes.length; i++) {
                AttributeDescriptor attbType = targetFeatureType.getAttributeDescriptors().get(i);
                if (attbType.equals(distanceAttbType)) {
                    attributes[i] = nearestDistance;
                } else if (attbType.equals(geomAttbType)) {
                    attributes[i] = geom;
                } else {
                    attributes[i] = feature.getProperty(attbType.getName()).getValue();
                }
            }
            return SimpleFeatureBuilder.build(
                    targetFeatureType, attributes, feature.getIdentifier().getID());
        } catch (Exception e) {
            LOGGER.warning("Error creating feature: " + e);
            throw new ProcessException("Error creating feature: " + e, e);
        }
    }
}
