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
package org.geotools.factory;

import java.util.Arrays;
import java.util.Set;
import org.geotools.api.data.FileDataStoreFactorySpi;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.style.StyleFactory;
import org.geotools.feature.LenientFeatureFactoryImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.Hints;

/**
 * Defines static methods used to access the application's default implementation for some common factories. Those
 * "common" factories comprise the {@linkplain StyleFactory style} and {@linkplain FilterFactory filter} factories. Note
 * that some specialized factories finder like {@linkplain org.geotools.referencing.ReferencingFactoryFinder
 * referencing} are defined in specialized classes.
 *
 * <p><b>Tip:</b> The {@link BasicFactories} classes provides an other way to access the various factories from a
 * central point.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Jody Garnett
 */
public final class CommonFactoryFinder extends FactoryFinder {
    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    /** Do not allows any instantiation of this class. */
    private CommonFactoryFinder() {
        // singleton
    }

    /** Returns the service registry. The registry will be created the first time this method is invoked. */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CommonFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                StyleFactory.class,
                FilterFactory.class,
                FileDataStoreFactorySpi.class,
                Function.class,
                FunctionFactory.class,
                FeatureFactory.class,
                FeatureTypeFactory.class
            }));
        }
        return registry;
    }

    /**
     * Returns the first implementation of {@link StyleFactory} matching the specified hints. If no implementation
     * matches, a new one is created if possible or an exception is thrown otherwise.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return The first style factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the {@link StyleFactory}
     *     interface.
     * @see Hints#STYLE_FACTORY
     */
    public static StyleFactory getStyleFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return lookup(StyleFactory.class, hints, Hints.STYLE_FACTORY);
    }
    /**
     * Returns the first implementation of {@link StyleFactory}. If no implementation matches, a new one is created if
     * possible or an exception is thrown otherwise.
     *
     * @return The first style factory available
     * @throws FactoryRegistryException if no implementation was found or can be created for the {@link StyleFactory}
     *     interface.
     * @see Hints#STYLE_FACTORY
     */
    public static StyleFactory getStyleFactory() throws FactoryRegistryException {
        return getStyleFactory(null);
    }

    /**
     * Returns a set of all available implementations for the {@link StyleFactory} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available style factory implementations.
     */
    public static synchronized Set<StyleFactory> getStyleFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<>(getServiceRegistry().getFactories(StyleFactory.class, null, hints));
    }

    /**
     * Returns a set of all available implementations for the {@link Function} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available function expression implementations.
     */
    public static synchronized Set<Function> getFunctions(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<>(getServiceRegistry().getFactories(Function.class, null, hints));
    }

    /**
     * Returns a set of all available implementations of {@link FunctionFactory}.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available function factory implementations.
     */
    public static synchronized Set<FunctionFactory> getFunctionFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<>(getServiceRegistry().getFactories(FunctionFactory.class, null, hints));
    }

    /**
     * Returns a set of all available implementations for the {@link FileDataStoreFactorySpi} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available file data store factory implementations.
     */
    public static synchronized Set<FileDataStoreFactorySpi> getFileDataStoreFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<>(getServiceRegistry().getFactories(FileDataStoreFactorySpi.class, null, hints));
    }

    /**
     * Return an implementation of {@link FeatureFactory} matching the specified hint FEATURE_FACTORY.
     *
     * <p>If no hint is specified, {@link LenientFeatureFactoryImpl} will be used.
     *
     * <p>Uses lookup functionality to get an instance of the factory.
     *
     * @param hints An optional map of hints; or {@code null} if none
     * @return Instance of FeatureFactory
     * @throws FactoryRegistryException if no implementation could be provided
     * @see Hints#FEATURE_FACTORY
     */
    public static FeatureFactory getFeatureFactory(Hints hints) {
        hints = mergeSystemHints(hints);
        if (hints.get(Hints.FEATURE_FACTORY) == null) {
            hints.put(Hints.FEATURE_FACTORY, LenientFeatureFactoryImpl.class);
        }
        return lookup(FeatureFactory.class, hints, Hints.FEATURE_FACTORY);
    }

    /**
     * Return the first implementation of {@link FeatureTypeFactory} matching the specified hints.
     *
     * <p>If no implementation matches, a new one is created if possible or an exception is thrown.
     *
     * @param hints An optional map of hints; or {@code null} if none
     * @return Instance of FeatureTypeFactory matching the supplied hints
     * @throws FactoryRegistryException if no implementation could be provided
     * @see Hints#FEATURE_TYPE_FACTORY
     */
    public static FeatureTypeFactory getFeatureTypeFactory(Hints hints) {
        hints = mergeSystemHints(hints);
        return lookup(FeatureTypeFactory.class, hints, Hints.FEATURE_TYPE_FACTORY);
    }

    /**
     * Returns the first implementation of {@link FilterFactory} matching the specified hints. If no implementation
     * matches, a new one is created if possible or an exception is thrown otherwise.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return The first filter factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the {@link FilterFactory}
     *     interface.
     * @see Hints#FILTER_FACTORY
     */
    public static FilterFactory getFilterFactory(Hints hints) throws FactoryRegistryException {
        hints = mergeSystemHints(hints);
        return lookup(FilterFactory.class, hints, Hints.FILTER_FACTORY);
    }

    /**
     * Returns the first implementation of {@link FilterFactory}. If no implementation matches, a new one is created if
     * possible or an exception is thrown otherwise.
     *
     * @return The first filter factory implementation
     * @throws FactoryRegistryException if no implementation was found or can be created for the {@link FilterFactory}
     *     interface.
     * @see Hints#FILTER_FACTORY
     */
    public static FilterFactory getFilterFactory() throws FactoryRegistryException {
        return getFilterFactory(null);
    }

    /**
     * Looks up a certain factory using two methods:
     *
     * <ul>
     *   <li>First and un-synchronized lookup in the hints, should the user have provided the preferred factroy
     *   <li>A standard SPI registry scan, which has to be fully synchronized
     */
    private static <T> T lookup(Class<T> category, Hints hints, Hints.Key key) {
        // nulls?
        if (hints == null || key == null) {
            return null;
        }

        // see if the user expressed a preference in the hints
        final Object hint = hints.get(key);
        if (hint != null) {
            if (category.isInstance(hint)) {
                return category.cast(hint);
            }
        }

        // otherwise do the lousy slow system scan
        synchronized (CommonFactoryFinder.class) {
            return getServiceRegistry().getFactory(category, null, hints, key);
        }
    }

    /**
     * Returns a set of all available implementations for the {@link FilterFactory} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available filter factory implementations.
     */
    public static synchronized Set<FilterFactory> getFilterFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<>(getServiceRegistry().getFactories(FilterFactory.class, null, hints));
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the application class
     * path can theoretically change, or additional plug-ins may become available. Rather than re-scanning the classpath
     * on every invocation of the API, the class path is scanned automatically only on the first invocation. Clients can
     * call this method to prompt a re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public static synchronized void scanForPlugins() {
        if (registry != null) {
            registry.scanForPlugins();
        }
    }

    /** Resets the factory finder and prepares for a new full scan of the SPI subsystems */
    public static void reset() {
        FactoryRegistry copy = registry;
        registry = null;
        if (copy != null) {
            copy.deregisterAll();
        }
    }
}
