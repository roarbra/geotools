/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

import java.io.IOException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.store.ContentEntry;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * VPFFeature Source
 *
 * @author James Gambale (Alysida AI)
 */
public class VPFCovFeatureSource extends VPFFeatureSource {

    private VPFFeatureType featureType;

    public VPFCovFeatureSource(VPFFeatureType featureType, ContentEntry entry, Query query) {
        super(entry, query);
        this.featureType = featureType;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        return new VPFFeatureReader(getState(), featureType);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return -1; // feature by feature scan required to count records
    }

    /** Implementation that generates the total bounds (many file formats record this information in the header) */
    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        CoordinateReferenceSystem crs = this.featureType.getCoordinateReferenceSystem();
        ReferencedEnvelope bounds = null;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> rdr = this.getReader()) {
            while (rdr.hasNext()) {
                SimpleFeature feature = rdr.next();

                if (feature != null) {

                    BoundingBox bb = feature.getBounds();

                    if (bounds == null) bounds = new ReferencedEnvelope(bb);
                    else bounds.expandToInclude(ReferencedEnvelope.reference(bb));
                }
            }
        }

        if (bounds == null) {
            bounds = ReferencedEnvelope.create(crs);
        }

        return bounds;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return this.featureType;
    }
}
