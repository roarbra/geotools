/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.util.GenericName;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Abstract implementation for EPSG (has a DataSource reference inside).
 *
 * <p>DataSource docs needed:
 *
 * @author Cory Horner (Refractions Research)
 */
public abstract class AbstractEpsgMediator extends AbstractAuthorityMediator {

    /** The default priority level for this factory. */
    public static final int PRIORITY = NORMAL_PRIORITY - 10;

    private static final Logger LOGGER = Logging.getLogger(AbstractEpsgMediator.class);

    protected DataSource datasource;

    /** No argument constructor - must not fail for factory finder registration. */
    public AbstractEpsgMediator() {}

    public AbstractEpsgMediator(Hints hints) throws FactoryException {
        this(hints, lookupDataSource(hints));
    }
    /**
     * We expect the EPSG_DATASOURCE to provide a DataSource. Either:
     *
     * <ul>
     *   <li>A name we can use to look up the DataSource in the initial context
     *   <li>An actual DataSource instance
     * </ul>
     *
     * @return DataSource
     */
    static DataSource lookupDataSource(Hints hints) throws FactoryException {
        Object hint = hints.get(Hints.EPSG_DATA_SOURCE);
        if (hint instanceof DataSource) {
            return (DataSource) hint;
        } else if (hint instanceof String) {
            String name = (String) hint;
            try {
                return (DataSource) GeoTools.jndiLookup(name);
            } catch (Exception e) {
                throw new FactoryException("EPSG_DATA_SOURCE '" + name + "' not found:" + e, e);
            }
        }
        throw new FactoryException("EPSG_DATA_SOURCE must be provided");
    }

    public AbstractEpsgMediator(Hints hints, DataSource datasource) {
        super(PRIORITY, hints);

        if (datasource != null) {
            this.datasource = datasource;
        } else {
            try {
                this.datasource = lookupDataSource(hints);
            } catch (FactoryException lookupFailed) {
                throw (NullPointerException)
                        new NullPointerException("DataSource not provided:" + lookupFailed).initCause(lookupFailed);
            }
        }
        hints.put(Hints.EPSG_DATA_SOURCE, this.datasource);
    }

    protected Connection getConnection() throws SQLException {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            throw e;
        }
    }

    @Override
    public Citation getAuthority() {
        return Citations.EPSG;
    }

    @Override
    public void dispose() throws FactoryException {
        super.dispose();
        datasource = null;
    }

    @Override
    public boolean isConnected() {
        return datasource != null && super.isConnected();
    }

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object corresponding to the specified {@code code}
     *     has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    @Override
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        IdentifiedObject identifiedObject = createObject(code);
        final Identifier identifier = identifiedObject.getName();
        if (identifier instanceof GenericName) {
            return ((GenericName) identifier).toInternationalString();
        }
        return new SimpleInternationalString(identifier.getCode());
    }
}
