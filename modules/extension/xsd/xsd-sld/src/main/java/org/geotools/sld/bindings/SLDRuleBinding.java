/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.api.filter.Filter;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.Rule;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.util.InternationalString;
import org.geotools.sld.CssParameter;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:Rule.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="Rule"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A Rule is used to attach
 *              property/scale conditions to and group         the
 *              individual symbolizers used for rendering.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:LegendGraphic" minOccurs="0"/&gt;
 *              &lt;xsd:choice minOccurs="0"&gt;
 *                  &lt;xsd:element ref="ogc:Filter"/&gt;
 *                  &lt;xsd:element ref="sld:ElseFilter"/&gt;
 *              &lt;/xsd:choice&gt;
 *              &lt;xsd:element ref="sld:MinScaleDenominator" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:MaxScaleDenominator" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Symbolizer" maxOccurs="unbounded"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDRuleBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDRuleBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.RULE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return Rule.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Rule rule = styleFactory.createRule();

        // &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
        if (node.hasChild("Name")) {
            rule.setName((String) node.getChildValue("Name"));
        }

        // &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
        if (node.hasChild("Title")) {
            rule.getDescription().setTitle((InternationalString) node.getChildValue("Title"));
        }

        // &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
        if (node.hasChild("Abstract")) {
            rule.getDescription().setAbstract((InternationalString) node.getChildValue("Abstract"));
        }

        // &lt;xsd:element ref="sld:LegendGraphic" minOccurs="0"/&gt;
        if (node.hasChild("LegendGraphic")) {
            rule.setLegend((GraphicLegend) node.getChildValue("LegendGraphic"));
        }

        // &lt;xsd:choice minOccurs="0"&gt;
        //	 &lt;xsd:element ref="ogc:Filter"/&gt;
        //	 &lt;xsd:element ref="sld:ElseFilter"/&gt;
        // &lt;/xsd:choice&gt;
        if (node.hasChild(Filter.class)) {
            rule.setFilter(node.getChildValue(Filter.class));
        } else if (node.hasChild("ElseFilter")) {
            rule.setElseFilter(true);
        }

        // &lt;xsd:element ref="sld:MinScaleDenominator" minOccurs="0"/&gt;
        if (node.hasChild("MinScaleDenominator")) {
            rule.setMinScaleDenominator(((Double) node.getChildValue("MinScaleDenominator")).doubleValue());
        }

        // &lt;xsd:element ref="sld:MaxScaleDenominator" minOccurs="0"/&gt;
        if (node.hasChild("MaxScaleDenominator")) {
            rule.setMaxScaleDenominator(((Double) node.getChildValue("MaxScaleDenominator")).doubleValue());
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : node.getChildValues(CssParameter.class)) {
            rule.getOptions().put(param.getName(), param.getExpression().evaluate(null, String.class));
        }

        // &lt;xsd:element ref="sld:Symbolizer" maxOccurs="unbounded"/&gt;
        List<Symbolizer> syms = node.getChildValues(Symbolizer.class);
        rule.symbolizers().addAll(syms);

        return rule;
    }
}
