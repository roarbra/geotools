/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata;

/**
 * Thrown when a setter method is invoked on a {@linkplain org.geotools.metadata.iso.MetadataEntity metadata entity},
 * but this entity was declared unmodifiable.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public class UnmodifiableMetadataException extends UnsupportedOperationException {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -1885135341334523675L;

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public UnmodifiableMetadataException(final String message) {
        super(message);
    }
}
