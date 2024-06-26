/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import org.geotools.api.data.AttributeReader;
import org.geotools.api.feature.type.AttributeDescriptor;

/**
 * Attribute Reader that joins.
 *
 * @author Ian Schneider
 * @version $Id$
 */
public class JoiningAttributeReader implements AttributeReader {
    private AttributeReader[] readers;
    private int[] index;
    private AttributeDescriptor[] metaData;

    /**
     * Creates a new instance of JoiningAttributeReader
     *
     * @param readers Readers to join
     */
    public JoiningAttributeReader(AttributeReader... readers) {
        this.readers = readers;

        this.metaData = joinMetaData(readers);
    }

    private AttributeDescriptor[] joinMetaData(AttributeReader... readers) {
        int total = 0;
        index = new int[readers.length];

        for (int i = 0, ii = readers.length; i < ii; i++) {
            index[i] = total;
            total += readers[i].getAttributeCount();
        }

        AttributeDescriptor[] md = new AttributeDescriptor[total];
        int idx = 0;

        for (AttributeReader reader : readers) {
            for (int j = 0, jj = reader.getAttributeCount(); j < jj; j++) {
                md[idx] = reader.getAttributeType(j);
                idx++;
            }
        }

        return md;
    }

    @Override
    public void close() throws IOException {
        IOException dse = null;

        for (AttributeReader reader : readers) {
            try {
                reader.close();
            } catch (IOException e) {
                dse = e;
            }
        }

        if (dse != null) {
            throw dse;
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        for (AttributeReader reader : readers) {
            if (reader.hasNext()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void next() throws IOException {
        for (AttributeReader reader : readers) {
            if (reader.hasNext()) {
                reader.next();
            }
        }
    }

    @Override
    public Object read(int idx) throws IOException {
        AttributeReader reader = null;

        for (int i = index.length - 1; i >= 0; i--) {
            if (idx >= index[i]) {
                idx -= index[i];
                reader = readers[i];

                break;
            }
        }

        if (reader == null) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        return reader.read(idx);
    }

    @Override
    public int getAttributeCount() {
        return metaData.length;
    }

    @Override
    public AttributeDescriptor getAttributeType(int i) {
        return metaData[i];
    }
}
