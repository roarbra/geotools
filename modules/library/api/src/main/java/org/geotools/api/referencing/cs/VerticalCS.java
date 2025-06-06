/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.cs;

/**
 * A one-dimensional coordinate system used to record the heights (or depths) of points. Such a coordinate system is
 * usually dependent on the Earth's gravity field, perhaps loosely as when atmospheric pressure is the basis for the
 * vertical coordinate system axis. An exact definition is deliberately not provided as the complexities of the subject
 * fall outside the scope of this specification. A {@code VerticalCS} shall have one {@linkplain #getAxis axis
 * association}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.crs.VerticalCRS    Vertical},
 *   {@link org.geotools.api.referencing.crs.EngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface VerticalCS extends CoordinateSystem {}
