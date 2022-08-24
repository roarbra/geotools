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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ows.wms;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test how WebMapServer works with concurrent usage. Especially testing if using
 * MultithreadedHttpClient works.
 *
 * @author Roar Brænden
 */
public class WebMapServerConcurrentTest {

    private static Logger LOGGER = Logging.getLogger(WebMapServerConcurrentTest.class);

    // use a dynamic http port to avoid conflicts
    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.options().dynamicPort());

    /**
     * Test if the three calls uses 400 - 600 ms.
     *
     * <p>Reasoning being that each request takes approx. 200 ms, and one must wait for the other
     * two.
     */
    @Test
    public void testConcurrentGetMapRequests() throws Exception {
        HTTPClient client = HTTPClientFinder.createClient(HTTPConnectionPooling.class);
        ((HTTPConnectionPooling) client).setMaxConnections(2);
        client.setConnectTimeout(1);
        final URL testUrl = new URL("http://localhost:" + wireMockRule.port() + "/wms");

        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/wms?REQUEST=GetMap&VERSION=1.3.0&SERVICE=WMS"))
                                .willReturn(
                                        aResponse()
                                                .withHeader("Content-type", "image/png")
                                                .withFixedDelay(200))));

        String capsBody =
                IOUtils.toString(new FileReader(TestData.file(this, "concurCap.xml")))
                        .replaceAll("http://geoserver.org/geoserver/ows", testUrl.toExternalForm());

        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/wms?REQUEST=GetCapabilities&VERSION=1.3.0&SERVICE=WMS"))
                                .willReturn(
                                        aResponse()
                                                .withHeader("Content-type", "text/xml")
                                                .withBody(capsBody))));

        ExecutorService executors = Executors.newFixedThreadPool(3);
        final AtomicInteger counter = new AtomicInteger();
        long timer;

        final WebMapServer server = new WebMapServer(testUrl, client);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {

            executors.execute(
                    new Runnable() {
                        public void run() {
                            try {
                                LOGGER.info(
                                        "Issue request on thread :"
                                                + Thread.currentThread().getName());
                                GetMapRequest request = server.createGetMapRequest();
                                GetMapResponse response = server.issueRequest(request);
                                try {
                                    IOUtils.consume(response.getInputStream());
                                } finally {
                                    response.dispose();
                                }
                                LOGGER.info(
                                        "Consumed response on thread :"
                                                + Thread.currentThread().getName());
                                counter.incrementAndGet();
                            } catch (ServiceException | IOException e) {
                                LOGGER.log(Level.WARNING, "Failure in test call.", e);
                            }
                        }
                    });
        }
        executors.shutdown();
        executors.awaitTermination(2, TimeUnit.SECONDS);
        timer = System.currentTimeMillis() - start;

        Assert.assertEquals("Some of the requests failed.", 3, counter.get());
        Assert.assertTrue("Test ended too early. (" + timer + " ms)", timer > 2 * 200);
        Assert.assertFalse(
                "Request seems to go serial. (" + timer + " ms)", timer > 3 * 200 && timer < 1000);
        Assert.assertFalse("Test ended with a timeout. (" + timer + " ms)", timer > 1000);
    }
}
