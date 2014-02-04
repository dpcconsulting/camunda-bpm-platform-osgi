package org.camunda.bpm.engine.osgi.web;

import org.camunda.bpm.engine.ProcessEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class BundleActivator implements org.osgi.framework.BundleActivator {

    private ServiceReference reference;

    public static ProcessEngine ENGINE;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        reference = bundleContext.getServiceReference(ProcessEngine.class.getName());
        if (reference != null) {
            ENGINE = (ProcessEngine) bundleContext.getService(reference);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        if (reference != null) {
            bundleContext.ungetService(reference);
        }
    }
}
