package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.v2_0.FES;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.w3c.dom.Document;

public class PropertyIsNullTypeBindingTest {

    private static final String PROP = "prop";

    /** Test checking correct encoding for isNull filter and its property name. */
    @Test
    public void testPropertyIsNullEncoding() throws Exception {
        FilterFactory ff = new FilterFactoryImpl();
        Filter filter = ff.isNull(ff.property(PROP));
        Configuration configuration = new org.geotools.filter.v2_0.FESConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.setIndenting(true);
        Document encodedDoc = encoder.encodeAsDOM(filter, FES.Filter);
        XPath xpath = XPathFactory.newInstance().newXPath();
        defaultNamespaceContext(xpath);
        String prop = xpath.evaluate("/fes:Filter/fes:PropertyIsNull/fes:ValueReference", encodedDoc);
        assertEquals(PROP, prop);
    }

    @SuppressWarnings("unchecked") // Java 8 vs Java 11 differences
    private void defaultNamespaceContext(XPath xpath) {
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public Iterator getPrefixes(String namespaceURI) {
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public String getNamespaceURI(String prefix) {
                if ("fes".equals(prefix)) return "http://www.opengis.net/fes/2.0";
                return null;
            }
        });
    }
}
