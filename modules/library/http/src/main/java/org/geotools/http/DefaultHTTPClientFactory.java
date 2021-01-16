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
 */
package org.geotools.http;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Default factory for generating HTTP client's.
 *
 * Will deliver a SimpleHttpClient, or a HTTPClient given by the hint HTTP_CLIENT.
 *
 * @author Roar Brænden
 */
public class DefaultHTTPClientFactory extends AbstractHTTPClientFactory {

    private static final Logger LOGGER = Logging.getLogger(DefaultHTTPClientFactory.class);

    public DefaultHTTPClientFactory(){
        super(new Class[]{SimpleHttpClient.class});
    }

    @SuppressWarnings("unchecked")
    @Override
    protected HTTPClient createClient(Hints hints) {
        if (!hints.containsKey(Hints.HTTP_CLIENT)){
            Class requestedFactory = hints.get(Hints.HTTP_CLIENT);




            HTTPClient client = new SimpleHttpClient();
            Hints.HTTP_CLIENT.isCompatibleValue(client);

            Object value = hints.get(Hints.HTTP_CLIENT);
            return new SimpleHttpClient();
        } else {
            try {
                Object hint = hints.get(Hints.HTTP_CLIENT);
                Class<HTTPClient> cls =
                        (Class<HTTPClient>)
                                (hint instanceof String ? Class.forName((String) hint) : hint);

                LOGGER.fine("Using special Http client " + cls.getName());
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Exception when initiating new HTTP client.", ex);
                throw new RuntimeException(
                        String.format(
                                "A class couldn't be initiated: %s", hints.get(Hints.HTTP_CLIENT)));
            }
        }
    }
}
