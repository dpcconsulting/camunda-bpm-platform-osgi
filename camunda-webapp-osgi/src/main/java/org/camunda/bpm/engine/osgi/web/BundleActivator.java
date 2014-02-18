package org.camunda.bpm.engine.osgi.web;

import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.ServletException;
import org.camunda.bpm.engine.ProcessEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

public class BundleActivator implements org.osgi.framework.BundleActivator {

    private ServiceReference reference;
    
    public static ProcessEngine ENGINE;
    
    private ServiceTracker serviceTracker;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("STARTING CAMUNDA WEBAPP BUNDLE");
        reference = bundleContext.getServiceReference(ProcessEngine.class.getName());
        if (reference != null) {
            ENGINE = (ProcessEngine) bundleContext.getService(reference);
        }
        
        serviceTracker = new HttpServiceTracker(bundleContext);
        serviceTracker.open();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("STOPPING CAMUNDA WEBAPP BUNDLE");
        if (reference != null) {
            bundleContext.ungetService(reference);
        }
        
        serviceTracker.close();
        serviceTracker = null;
    }
}
