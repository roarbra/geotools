/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.bindings;

import java.lang.reflect.Field;
import javax.measure.Unit;
import javax.xml.namespace.QName;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;
import si.uom.NonSI;
import si.uom.SI;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.BaseUnit;

public class UnitBinding extends AbstractSimpleBinding {

    @Override
    public QName getTarget() {
        return OWS.UOM;
    }

    @Override
    public Class getType() {
        return Unit.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /** @override */
    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        // Object parseObject = UnitFormat.getInstance().parseObject((String) value);
        // Object parseObject = UnitFormat.getAsciiInstance().parseObject((String) value);
        Unit<?> valueOf = lookup((String) value);
        return valueOf;
    }

    private Unit<?> lookup(String name) {
        Unit<?> unit = lookup(SI.class, name);
        if (unit != null) return unit;

        unit = lookup(NonSI.class, name);
        if (unit != null) return unit;

        if (name.endsWith("s") || name.endsWith("S")) {
            return lookup(name.substring(0, name.length() - 1));
        }
        // if we get here, try some aliases
        if (name.equalsIgnoreCase("feet")) {
            return lookup(NonSI.class, "foot");
        }
        // if we get here, try some aliases
        if (name.equalsIgnoreCase("meters") || name.equalsIgnoreCase("meter")) {
            return lookup(SI.class, "m");
        }
        if (name.equalsIgnoreCase("unity")) {
            return AbstractUnit.ONE;
        }
        return null;
    }

    private Unit<?> lookup(Class<?> class1, String name) {
        Unit<?> unit;
        Field[] fields = class1.getDeclaredFields();
        for (Field field : fields) {
            String name2 = field.getName();
            if ((field.getType().isAssignableFrom(BaseUnit.class)
                            || field.getType().isAssignableFrom(AlternateUnit.class))
                    && name2.equalsIgnoreCase(name)) {

                try {
                    unit = (Unit<?>) field.get(class1);
                    return unit;
                } catch (Exception e) {
                    // continue searching
                }
            }
        }

        return null;
    }

    /**
     * Performs the encoding of the object as a String.
     *
     * @param object The object being encoded, never null.
     * @param value The string returned from another binding in the type hierachy, which could be null.
     * @return A String representing the object.
     * @override
     */
    @Override
    public String encode(Object object, String value) throws Exception {
        return object.toString();
    }
}
