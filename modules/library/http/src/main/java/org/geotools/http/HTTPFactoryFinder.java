/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;

/**
 * Factory finder for getting instances of HTTPClientFactory. All implementations should be
 * registered as a file with name org.geotools.http.HTTPClientFactory in META_INF/services of their
 * respective jar.
 *
 * <p>To pick a particular implementation the hint HTTP_CLIENT_FACTORY could be set to the full
 * class name, either in the function call, or by Java property.
 *
 * @author Roar Brænden
 */
public class HTTPFactoryFinder extends FactoryFinder {

    /** Cache of last factory found */
    static HTTPClientFactory lastFactory;

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    /** Do not allow any instantiation of this class. */
    private HTTPFactoryFinder() {
        // singleton
    }

    /**
     * Dynamically register a new factory from SPI.
     * @param factory client factory
     */
    public static void addFactory(HTTPClientFactory factory) {
        getServiceRegistry().registerFactory(factory);
    }

    /**
     * Dynamically removes a factory from SPI.
     * Normally the factory has been added before via {@link #addFactory(HTTPClientFactory)}
     * @param factory client factory
     */
    public static void removeProcessFactory(HTTPClientFactory factory) {
        if (lastFactory == factory) {
            lastFactory = null;
        }
        getServiceRegistry().deregisterFactory(factory);
    }

    /**
     * Set of available HTTPClientFactory; each of which is responsible for one or client implementations.
     *
     * @return Set of ProcessFactory
     */
    public static Set<HTTPClientFactory> getFactories() {
        Stream<HTTPClientFactory> serviceProviders =
                getServiceRegistry().getFactories(HTTPClientFactory.class, null, null);
        return new LazySet<>(serviceProviders);
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(HTTPFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {HTTPClientFactory.class}));
        }
        return registry;
    }

    /**
     * Get default Http Client factory.
     *
     * @return
     */
    public static synchronized HTTPClientFactory getHttpClientFactory() {
        return getServiceRegistry()
                .getFactory(
                        HTTPClientFactory.class,
                        null,
                        mergeSystemHints(null),
                        Hints.HTTP_CLIENT_FACTORY);
    }

    /**
     * Get a special Http Client Factory by specifying hint HTTP_CLIENT_FACTORY
     *
     * @param hints
     * @return
     */
    public static synchronized HTTPClientFactory getHttpClientFactory(Hints hints) {
        return getServiceRegistry()
                .getFactory(
                        HTTPClientFactory.class,
                        null,
                        mergeSystemHints(hints),
                        Hints.HTTP_CLIENT_FACTORY);
    }

    /** Makes sure a call for getServiceRegistry will do a clean scan */
    public static synchronized void reset() {
        final FactoryRegistry copy = registry;
        registry = null;
        if (copy != null) {
            copy.deregisterAll();
        }
    }
}
