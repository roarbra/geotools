package org.geotools.wms.v1_3;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WMSConfigurationTest {

    static final Logger LOGGER = Logging.getLogger(WMSConfigurationTest.class);

    @Test
    public void testValidate() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMSConfiguration());
        p.setValidating(true);
        try (InputStream is = getClass().getResourceAsStream("./caps130.xml")) {
            p.parse(is);
        }
        if (!p.getValidationErrors().isEmpty()) {
            for (Exception exception : p.getValidationErrors()) {
                SAXParseException ex = (SAXParseException) exception;
                LOGGER.log(Level.SEVERE, ex.getLineNumber() + "," + ex.getColumnNumber() + " -" + ex.toString());
            }
            fail("Document did not validate.");
        }
    }
}
