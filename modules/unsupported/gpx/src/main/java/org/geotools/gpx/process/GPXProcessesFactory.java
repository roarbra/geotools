package org.geotools.gpx.process;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.text.Text;

/**
 * Used to initiate a process.
 *
 * <p>Could be used to have some sort of caching for the processes.
 *
 * @author Roar Brænden
 */
public class GPXProcessesFactory extends AnnotatedBeanProcessFactory {

    public GPXProcessesFactory() {
        super(Text.text("GPX processes"), "gpx", GPXExtentProcess.class);
    }
}
