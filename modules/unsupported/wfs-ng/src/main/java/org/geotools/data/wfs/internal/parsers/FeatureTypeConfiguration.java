package org.geotools.data.wfs.internal.parsers;

import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.XSD;

/**
 * Representing the configuration used when requesting GetFeature. Using the result of a prior
 * DescribeFeatureType and the configuration from WfsStrategy.
 */
public class FeatureTypeConfiguration extends Configuration {

    public FeatureTypeConfiguration(
            QName featureName, URL schemaLocation, Configuration wfsConfiguration) {
        super(new FeatureTypeXSD(featureName, schemaLocation));
        wfsConfiguration.getDependencies().forEach(this::addDependency);
    }

    private static class FeatureTypeXSD extends XSD {

        private final String namespaceURI;

        private final String schemaLocation;

        private FeatureTypeXSD(QName featureName, URL schemaLocation) {
            this.namespaceURI = featureName.getNamespaceURI();
            this.schemaLocation = schemaLocation.toString();
        }

        @Override
        public String getNamespaceURI() {
            return namespaceURI;
        }

        @Override
        public String getSchemaLocation() {
            return schemaLocation;
        }
    }
}
