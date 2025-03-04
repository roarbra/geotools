/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.logging;

import java.util.logging.Logger;
import org.junit.Test;

/**
 * Test the {@link MonolineFormatter} class.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class MonolineFormatterTest {
    /**
     * Run the test. This is only a visual test. This test is disabled by default in order to avoid polluting the output
     * stream with logs during Maven builds.
     */
    @Test
    public void testInitialization() {
        Logging.ALL.forceMonolineConsoleOutput();
        final String[] namespaces = {
            "org.geotools.core",
            "org.geotools.resources",
            "org.geotools.referencing",
            "org.geotools.api.referencing" // Non-geotools logger should not be affected.
        };
        for (String namespace : namespaces) {
            // System.out.println();
            // System.out.print("Testing ");
            final Logger logger = Logging.getLogger(namespace);
            // System.out.println(logger.getName());
            logger.severe("Don't worry, just a test");
            logger.warning("This is an imaginary warning");
            logger.info("This is a pseudo-information message");
            logger.config("Not really configuring anything...");
            logger.fine("This is a detailed (but useless) message\nWe log this one on two lines!");
            logger.finer("This is a debug message");
        }
    }
}
