/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.dted;

import it.geosolutions.imageio.plugins.dted.DTEDImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.util.factory.Hints;

/**
 * An implementation of {@link Format} for the DTED format.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class DTEDFormat extends BaseGDALGridFormat implements Format {
    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DTEDFormat.class);

    /** Creates an instance and sets the metadata. */
    public DTEDFormat() {
        super(new DTEDImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new DTEDFormat.");
        }

        setInfo();
    }

    private static final InfoWrapper INFO = new InfoWrapper("DTED Coverage Format", "DTED");

    /** Sets the metadata information. */
    @Override
    protected void setInfo() {
        setInfo(INFO);
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    @Override
    public DTEDReader getReader(Object source, Hints hints) {
        try {
            return new DTEDReader(source, hints);
        } catch (MismatchedDimensionException | DataSourceException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
}
