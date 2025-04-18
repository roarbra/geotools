/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import java.io.IOException;
import org.geotools.api.feature.Feature;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureIterator;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;

/**
 * GetFeature response for feature's that isn't treated as SimpleFeatureType.
 *
 * <p>Created when calling {@link WFSClient#issueComplexRequest(GetFeatureRequest)}.
 *
 * @author Roar Brænden
 */
public class ComplexGetFeatureResponse extends WFSResponse {

    private final XmlComplexFeatureParser parser;

    public ComplexGetFeatureResponse(
            WFSRequest originatingRequest, HTTPResponse httpResponse, XmlComplexFeatureParser parser)
            throws ServiceException, IOException {
        super(originatingRequest, httpResponse);
        this.parser = parser;
    }

    /**
     * The parser that will be used to extract features from the http response.
     *
     * <p>Should only be called before calling {@link #features()}
     */
    public XmlComplexFeatureParser getParser() {
        return this.parser;
    }

    /** Should only be called once. Call {@link FeatureIterator#close()} after use. */
    public FeatureIterator<Feature> features() {
        return new ComplexFeatureIteratorImpl(parser);
    }
}
