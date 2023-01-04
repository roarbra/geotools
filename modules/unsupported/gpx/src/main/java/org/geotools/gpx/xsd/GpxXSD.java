package org.geotools.gpx.xsd;

import javax.xml.namespace.QName;
import org.geotools.xsd.XSD;

/** @author Roar Brænden */
class GpxXSD extends XSD {

    static final String NAMESPACE = "http://www.topografix.com/GPX/1/1";

    static final QName GpxType = new QName(NAMESPACE, "gpx");

    static final String Wpt = "wpt";

    static final QName WptType = new QName(NAMESPACE, Wpt);

    static final String Rte = "rte";
    static final String Rtept = "rtept";

    static final QName RteType = new QName(NAMESPACE, Rte);

    static final String Trk = "trk";
    static final String Trkseg = "trkseg";
    static final String Trkpt = "trkpt";

    static final QName TrkType = new QName(NAMESPACE, Trk);

    static final QName TrksegType = new QName(NAMESPACE, Trkseg);

    private GpxXSD() {}

    /** singleton instance. */
    static GpxXSD instance = new GpxXSD();

    /** The singleton instance; */
    static GpxXSD getInstance() {
        return instance;
    }

    @Override
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    @Override
    public String getSchemaLocation() {
        return getClass().getResource("gpx_1_1.xsd").toString();
    }
}
