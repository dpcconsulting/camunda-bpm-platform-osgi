package org.camunda.bpm.engine.osgi.web;

import org.camunda.bpm.engine.ProcessEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.ops4j.pax.web.service.WebContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class CamundaWebappActivator implements BundleActivator, ServiceListener {

    private final CamundaWebappWebRegister webappRegister = new CamundaWebappWebRegister();
    
    private ServiceReference ProcessengineReference;
    private ServiceReference WebContainerReference;

    public static ProcessEngine ENGINE;
    private WebContainer webContainer;
    private BundleContext bundleContext;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        
        System.out.println("STARTING CAMUNDA WEBAPP BUNDLE");
        ProcessengineReference = bundleContext.getServiceReference(ProcessEngine.class.getName());
        if (ProcessengineReference != null) {
            ENGINE = (ProcessEngine) bundleContext.getService(ProcessengineReference);
        }

        WebContainerReference = bundleContext.getServiceReference(WebContainer.class.getName());
        
        if (WebContainerReference != null) {
            webContainer = (WebContainer) bundleContext.getService(WebContainerReference);
            if (webContainer != null) {
                webappRegister.setWebContainerService(webContainer);
                webappRegister.start();
            }
        } else {
            System.out.println("reference is null!");
        }
        
        bundleContext.addServiceListener(this, "(objectClass=" + WebContainer.class.getName() + ")");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("STOPPING CAMUNDA WEBAPP BUNDLE");
        if (ProcessengineReference != null) {
            bundleContext.ungetService(ProcessengineReference);
            ProcessengineReference = null;
        }
        if (WebContainerReference != null) {
            bundleContext.ungetService(WebContainerReference);
            WebContainerReference = null;
        }
    }

    @Override
    public void serviceChanged(ServiceEvent se) {
        switch(se.getType()){
            case ServiceEvent.UNREGISTERING:{
                bundleContext.ungetService(se.getServiceReference());
                break;
            }
            case ServiceEvent.REGISTERED:{
                webContainer = (WebContainer) bundleContext.getService(se.getServiceReference());
                if(webContainer != null){
                    webappRegister.setWebContainerService(webContainer);
                    webappRegister.start();
                }
                
                break;
            }
        }
    }
}
