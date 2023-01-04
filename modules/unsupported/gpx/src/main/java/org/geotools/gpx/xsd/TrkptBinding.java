package org.geotools.gpx.xsd;

import javax.xml.namespace.QName;
import org.locationtech.jts.geom.GeometryFactory;

/** Same as Way point, but for a track */
public class TrkptBinding extends WptTypeBinding {

    public TrkptBinding(GeometryFactory gFactory) {
        super(gFactory);
    }

    @Override
    public QName getTarget() {
        return new QName(GpxXSD.NAMESPACE, GpxXSD.Trkpt);
    }
}
