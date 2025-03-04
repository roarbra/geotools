package org.geotools.renderer.label;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.style.Font;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestGraphics;
import org.geotools.util.NumberRange;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mockito;

public class LabelCacheImplTest {

    private static final Geometry L4 = geometry("POLYGON((0 0, 0 0.1, 0.1 0.1, 0.1 0, 0 0))");

    private static final Geometry L3 = geometry("LINESTRING(20 20, 30 30)");

    private static final Geometry L2 = geometry("LINESTRING(10 10, 20 20)");

    private static final Geometry L1 = geometry("LINESTRING(0 0, 10 10)");

    private static final String LAYER_ID = "layerId";

    SimpleFeatureType schema;

    SimpleFeatureBuilder fb;

    LabelCacheImpl cache;

    StyleBuilder sb;

    NumberRange<Double> ALL_SCALES =
            new NumberRange<>(Double.class, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    @Before
    public void setupSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("name", String.class);
        tb.add("geom", Geometry.class, DefaultGeographicCRS.WGS84);
        schema = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(schema);
        cache = new LabelCacheImpl();
        cache.startLayer(LAYER_ID);
        sb = new StyleBuilder();
    }

    @Before
    public void setVeraFont() throws IOException, FontFormatException {
        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testSimpleGrouping() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");
        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        addToCache(cache, ts, f1);
        addToCache(cache, ts, f2);

        // we have just one
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(1, labels.size());
        LabelCacheItem item = labels.get(0);
        assertEquals("label1", item.getLabel());
        assertEquals(2, item.getGeoms().size());
    }

    @Test
    public void testGroupDifferentSymbolizers() throws Exception {
        TextSymbolizer ts1 = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts1.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");
        TextSymbolizer ts2 = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        ts2.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        addToCache(cache, ts1, f1);
        addToCache(cache, ts2, f2);

        // two different symbolizers, we should have two
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
    }

    @Test
    public void testMinNonGrouped() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        TextSymbolizer tsGroup = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        tsGroup.getOptions().put(org.geotools.api.style.TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        SimpleFeature f3 = createFeature("label1", L3);
        addToCache(cache, tsGroup, f1);
        addToCache(cache, ts, f2);
        addToCache(cache, tsGroup, f3);

        // two different symbolizers, we should have two (the first grouped with the third)
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
        LabelCacheItem item1 = labels.get(0);
        assertEquals("label1", item1.getLabel());
        assertEquals(Arrays.asList(L1, L3), item1.getGeoms());
        LabelCacheItem item2 = labels.get(1);
        assertEquals("label1", item2.getLabel());
        assertEquals(Collections.singletonList(L2), item2.getGeoms());
    }

    @Test
    public void testCustomBehaviour() throws TransformException, FactoryException {
        final List<String> labels = new ArrayList<>();
        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        LabelCacheImpl myCache = new LabelCacheImpl() {
            @Override
            int paintLabel(
                    Graphics2D graphics,
                    Rectangle displayArea,
                    LabelIndex glyphs,
                    int paintedLineLabels,
                    LabelPainter painter,
                    LabelCacheItem labelItem) {
                labels.add(labelItem.getLabel());
                return super.paintLabel(graphics, displayArea, glyphs, paintedLineLabels, painter, labelItem);
            }
        };
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        SimpleFeature f1 = createFeature("label1", L1);
        myCache.enableLayer(LAYER_ID);
        addToCache(myCache, ts, f1);

        myCache.endLayer(LAYER_ID, graphics, new Rectangle(0, 0, 256, 256));
        myCache.end(graphics, new Rectangle(0, 0, 256, 256));
        assertEquals(1, labels.size());
    }

    @Test
    public void testRendererListener() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");

        AtomicReference<Exception> exception = new AtomicReference<>(null);
        RenderListener listener = new RenderListener() {

            @Override
            public void featureRenderer(SimpleFeature feature) {
                // TODO Auto-generated method stub

            }

            @Override
            public void errorOccurred(Exception e) {
                exception.set(e);
            }
        };
        cache.addRenderListener(listener);
        SimpleFeature f1 = createFeature("label1", L1);
        addToCache(cache, ts, f1);
        cache.endLayer("layerId", null, null);
        TestGraphics testGraphics = new TestGraphics();
        testGraphics.setRenderingHints(Collections.emptyMap());
        cache.end(testGraphics, new Rectangle(0, 0, 10, 10));
        // got here, did we get the exception
        assertNotNull(exception.get());
    }

    @Test
    public void testUsesCustomLabelPainter() throws Exception {
        LabelPainter painter = Mockito.mock(LabelPainter.class);
        Graphics2D graphics = Mockito.mock(Graphics2D.class);

        cache.setConstructPainter((x, y) -> painter);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        SimpleFeature f1 = createFeature("label1", L1);
        addToCache(cache, ts, f1);

        cache.endLayer(LAYER_ID, graphics, new Rectangle(0, 0, 256, 256));
        cache.end(graphics, new Rectangle(0, 0, 256, 256));

        Mockito.verify(painter).setLabel(Mockito.any(LabelCacheItem.class));
        Mockito.verify(painter, Mockito.atLeastOnce()).getLabel();
    }

    @Test
    public void testDecimateSmallRing() throws Exception {
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.FOLLOW_LINE_KEY, "true");

        AtomicReference<Exception> exception = new AtomicReference<>(null);
        RenderListener listener = new RenderListener() {

            @Override
            public void featureRenderer(SimpleFeature feature) {
                // TODO Auto-generated method stub

            }

            @Override
            public void errorOccurred(Exception e) {
                exception.set(e);
            }
        };
        cache.addRenderListener(listener);
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);
        cache.endLayer("layerId", null, null);
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bi.createGraphics();
        cache.end(graphics, new Rectangle(0, 0, 10, 10));
        // got here, no exception
        assertNull(exception.get());
    }

    @Test
    public void testFollowLineAutoWrap() throws Exception {
        // these two right now are not compatible
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.AUTO_WRAP_KEY, "10");
        ts.getOptions().put(org.geotools.api.style.TextSymbolizer.FOLLOW_LINE_KEY, "true");

        // put in cache
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);

        // check autowrap got disabled
        List<LabelCacheItem> items = cache.getActiveLabels();
        LabelCacheItem item = items.get(0);
        assertTrue(item.isFollowLineEnabled());
        assertEquals(0, item.getAutoWrap());
    }

    private SimpleFeature createFeature(String label, Geometry geom) {
        fb.add(label);
        fb.add(geom);
        return fb.buildFeature(null);
    }

    private static Geometry geometry(String wkt) {
        try {
            return new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSymbolizerCaching() throws Exception {
        // these two right now are not compatible
        Font font = sb.createFont("Bitstream Vera Sans", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, font, "name");

        // put in cache
        SimpleFeature f1 = createFeature("label1", L4);
        addToCache(cache, ts, f1);
        SimpleFeature f2 = createFeature("label2", L4);
        addToCache(cache, ts, f2);

        // check TextStyle2D got cached
        List<LabelCacheItem> items = cache.getActiveLabels();
        assertEquals(2, items.size());
        assertEquals("label1", items.get(0).getLabel());
        assertEquals("label2", items.get(1).getLabel());
        assertSame(items.get(0).getTextStyle(), items.get(1).getTextStyle());
    }

    private void addToCache(LabelCacheImpl cache, TextSymbolizer ts, SimpleFeature f1)
            throws TransformException, FactoryException {
        cache.put(LAYER_ID, ts, f1, new LiteShape2((Geometry) f1.getDefaultGeometry(), null, null, false), ALL_SCALES);
    }
}
