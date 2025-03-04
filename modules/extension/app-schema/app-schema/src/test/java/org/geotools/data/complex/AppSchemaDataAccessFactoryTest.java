/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 * @since 2.4
 */
public class AppSchemaDataAccessFactoryTest extends AppSchemaTestSupport {

    AppSchemaDataAccessFactory factory;

    Map<String, Serializable> params;

    private static final String NSURI = "http://online.socialchange.net.au";

    static final Name mappedTypeName = Types.typeName(NSURI, "RoadSegment");

    @Before
    public void setUp() throws Exception {
        factory = new AppSchemaDataAccessFactory();
        params = new HashMap<>();
        params.put("dbtype", "app-schema");
        URL resource = getClass().getResource("/test-data/roadsegments.xml");
        if (resource == null) {
            fail("Can't find resouce /test-data/roadsegments.xml");
        }
        params.put("url", resource);
    }

    @After
    public void tearDown() throws Exception {
        factory = null;
        params = null;
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.createDataStore(Map)' */
    @Test
    public void testCreateDataStorePreconditions() {
        Map<String, Serializable> badParams = new HashMap<>();
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
        badParams.put("dbtype", "app-schema");
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
        badParams.put("url", "file://_inexistentConfigFile123456.xml");
        try {
            factory.createDataStore(badParams);
            fail("allowed bad params");
        } catch (IOException e) {
            // OK
        }
    }

    /** @throws IOException */
    @Test
    public void testCreateDataStore() throws IOException {
        DataAccess<FeatureType, Feature> ds = factory.createDataStore(params);
        assertNotNull(ds);
        FeatureSource<FeatureType, Feature> mappedSource = ds.getFeatureSource(mappedTypeName);
        assertNotNull(mappedSource);
        assertSame(ds, mappedSource.getDataStore());
        ds.dispose();
    }

    /** @throws IOException */
    @Test
    public void testFactoryLookup() throws IOException {
        DataAccess<FeatureType, Feature> ds = DataAccessFinder.getDataStore(params);
        assertNotNull(ds);
        assertTrue(ds instanceof AppSchemaDataAccess);

        FeatureSource<FeatureType, Feature> mappedSource = ds.getFeatureSource(mappedTypeName);
        assertNotNull(mappedSource);

        ds.dispose();
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.createNewDataStore(Map)' */
    @Test
    public void testCreateNewDataStore() throws IOException {
        try {
            factory.createNewDataStore(Collections.emptyMap());
            fail("unsupported?");
        } catch (UnsupportedOperationException e) {
            // OK
        }
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.getParametersInfo()' */
    @Test
    public void testGetParametersInfo() {
        DataStoreFactorySpi.Param[] params = factory.getParametersInfo();
        assertNotNull(params);
        assertEquals(2, params.length);
        assertEquals(String.class, params[0].type);
        assertEquals(URL.class, params[1].type);
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.canProcess(Map)' */
    @Test
    public void testCanProcess() {
        Map<String, Serializable> params = new HashMap<>();
        assertFalse(factory.canProcess(params));
        params.put("dbtype", "arcsde");
        params.put("url", "http://somesite.net/config.xml");
        assertFalse(factory.canProcess(params));
        params.remove("url");
        params.put("dbtype", "app-schema");
        assertFalse(factory.canProcess(params));

        params.put("url", "http://somesite.net/config.xml");
        assertTrue(factory.canProcess(params));
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.isAvailable()' */
    @Test
    public void testIsAvailable() {
        assertTrue(factory.isAvailable());
    }

    /** Test method for 'org.geotools.data.complex.AppSchemaDataAccessFactory.getImplementationHints()' */
    @Test
    public void testGetImplementationHints() {
        assertNotNull(factory.getImplementationHints());
        assertEquals(0, factory.getImplementationHints().size());
    }

    /** Testing class for check data store creation failure handling and resilience. */
    public static class AppSchemaDataAccessFactoryFailureTest extends AppSchemaTestSupport {

        private AppSchemaDataAccessFactory factory;
        private Map<String, Serializable> params;

        /**
         * Checks that App-schema data store factory unregisters correctly the included mappings stores on a creation
         * failure.
         */
        @Test
        public void testUnregisterOnFailure() throws Exception {
            factory = new AppSchemaDataAccessFactory();
            params = new HashMap<>();
            params.put("dbtype", "app-schema");
            URL resource = getClass().getResource("/test-data/creation_failure/roadsegments_bad.xml");
            if (resource == null) {
                fail("Can't find resouce /test-data/creation_failure/roadsegments_bad.xml");
            }
            params.put("url", resource);

            DataAccess<FeatureType, Feature> ds;
            boolean exceptionCatched = false;
            try {
                ds = factory.createDataStore(params);
                assertNotNull(ds);
                FeatureSource<FeatureType, Feature> mappedSource = ds.getFeatureSource(mappedTypeName);
                assertNull(mappedSource);
            } catch (Exception ex) {
                exceptionCatched = true;
            }
            assertTrue(exceptionCatched);
            assertFalse(DataAccessRegistry.hasName(mappedTypeName));
        }

        @After
        public void tearDown() throws Exception {
            factory = null;
            params = null;
        }
    }

    /**
     * Test that a mapping file with include can be loaded twice without throwing a duplicate mapping error (meaning
     * that the registry must dispose of it properly)
     *
     * @throws IOException
     */
    @Test
    public void testCreateFeatureChainedTwice() throws IOException {
        URL resource = getClass().getResource("/test-data/GeologicUnit.xml");
        params.put("url", resource);
        DataAccess<FeatureType, Feature> ds = factory.createDataStore(params);
        assertNotNull(ds);
        ds.dispose();
        ds = factory.createDataStore(params);
        assertNotNull(ds);
    }
}
