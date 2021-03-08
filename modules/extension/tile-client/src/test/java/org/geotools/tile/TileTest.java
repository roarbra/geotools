/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.MockHttpClient;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TileTest {

    protected Tile tile;

    @Before
    public void beforeTest() {
        this.tile = new CustomTile();
    }

    @After
    public void afterTest() {
        this.tile = null;
    }

    @Test
    public void testContructor() {
        Assert.assertNotNull(this.tile);
    }

    @Test
    public void testEquals() {

        Assert.assertEquals(this.tile, this.tile);
        Assert.assertNotEquals(null, this.tile);
        Assert.assertNotEquals("Blah", this.tile);

        Tile otherTile = new CustomTile();
        Assert.assertEquals(this.tile, otherTile);
        Assert.assertEquals(otherTile, this.tile);
    }

    @Test
    public void testCreateErrorImage() {
        BufferedImage img = this.tile.createErrorImage("Failed:" + this.tile.getId());
        Assert.assertNotNull(img);
        Assert.assertEquals(256, img.getHeight());
        Assert.assertEquals(256, img.getWidth());
    }

    @Test
    public void testGetExtent() {
        ReferencedEnvelope env =
                new ReferencedEnvelope(new Envelope(6, 15, 47, 55), DefaultGeographicCRS.WGS84);
        Assert.assertEquals(env, this.tile.getExtent());
    }

    @Test
    public void testGetId() {
        Assert.assertEquals("SomeService_01234xy56789", this.tile.getId());
    }

    protected static class CustomTile extends Tile {

        public CustomTile() {
            super(
                    TileIdentifierTest.createTileIdentifierPrototype(
                            new WebMercatorZoomLevel(5), 10, 12, "SomeService"),
                    new ReferencedEnvelope(new Envelope(6, 15, 47, 55), DefaultGeographicCRS.WGS84),
                    256,
                    new CustomService("http://test.tile-service.com/"));
        }

        @Override
        public URL getUrl() {
            try {
                return new URL(service.getBaseUrl() + "tile123");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected static class CustomService extends TileService {

        protected CustomService(String baseURL) {
            super("Custom tile service", baseURL, new MockHttpClient());
        }

        @Override
        public double[] getScaleList() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferencedEnvelope getBounds() {
            throw new UnsupportedOperationException();
        }

        @Override
        public CoordinateReferenceSystem getProjectedTileCrs() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TileFactory getTileFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Tile findTileAtCoordinate(double lon, double lat, ZoomLevel zoomLevel) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ZoomLevel getZoomLevel(int zoomLevel) {
            throw new UnsupportedOperationException();
        }
    }
}
