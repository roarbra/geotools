package org.geotools.gpx.process;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gpx.xsd.GpxConfiguration;
import org.geotools.gpx.xsd.GpxDataSet;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Parser;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Extract extents both in temporal and spatial of the tracks within the GPX file.
 *
 * @author Roar Brænden
 */
@DescribeProcess(title = "Extent of Gpx track")
public class GPXExtentProcess {

    private static Logger LOGGER = Logging.getLogger(GPXExtentProcess.class);

    @DescribeResult(description = "Start and end time pf track in addition to bounds.")
    public Extents execute(@DescribeParameter(name = "filepath") String filepath) {

        File gpxFile = new File(filepath);
        GpxDataSet gpx;
        try (FileInputStream input = new FileInputStream(gpxFile)) {
            Parser parser = new Parser(new GpxConfiguration());
            gpx = (GpxDataSet) parser.parse(input);
        } catch (Exception e) {
            LOGGER.severe("GPX extent process had an exception: " + e.getMessage());
            return new Extents(null, new Period(null, null));
        }
        Date start, end, next;
        SimpleFeatureCollection tracksCollection = gpx.extractTracks(schemaWithTime());
        ReferencedEnvelope extent = tracksCollection.getBounds();
        try (SimpleFeatureIterator features = tracksCollection.features()) {
            end = start = next = nextDate(features);
            while (features.hasNext()) {
                next = nextDate(features);
                if (next.before(start)) {
                    start = next;
                } else if (next.after(end)) {
                    end = next;
                }
            }
        }

        return new Extents(extent, new Period(start, end));
    }

    private Date nextDate(SimpleFeatureIterator features) {
        return (Date) features.next().getAttribute(1);
    }

    private SimpleFeatureType schemaWithTime() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Trackpoints");
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add("trkpt", Point.class);
        builder.add("time", Date.class);

        return builder.buildFeatureType();
    }

    /** A temporal and spatial extent, both given as immutable. */
    public static class Extents {

        private final Period temporal;
        private final Envelope spatial;

        Extents(Envelope spatial, Period temporal) {

            this.spatial = spatial;
            this.temporal = temporal;
        }

        public Envelope getSpatial() {
            return spatial;
        }

        public Period getTemporal() {
            return temporal;
        }
    }

    public static class Period {
        private final Date start;
        private final Date end;

        Period(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public Date getEnd() {
            return end;
        }

        public Date getStart() {
            return start;
        }
    }
}
