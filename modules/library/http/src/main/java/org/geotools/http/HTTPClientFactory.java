/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.http;

import org.geotools.util.factory.Hints;
import org.geotools.util.factory.OptionalFactory;

/**
 * Factory class to create a {@link HTTPClient}. Default implementation is within library gt-http.
 *
 * @author Roar Brænden
 */
public interface HTTPClientFactory extends OptionalFactory {

    /**
     * Check if factory can create requested implementation.
     *
     * @param implementation HTTP Client implementation
     * @return true if factory can create requested implementation
     */
    boolean canCreate(Class<? extends HTTPClient> implementation);

    /**
     * Create a http client.
     *
     * @return created http client instance
     */
    HTTPClient createClient();

    /**
     * Create a http client configured with the provided hints.
     *
     * To request a specific implementation use {@link Hints#HTTP_CLIENT}.
     * Provided hints supersede any given by {@code GeoTools.getDefaultHints()}.
     *
     * @param hints Used to select implementation created
     * @return created http client instance
     */
    HTTPClient createClient(Hints hints);
}
