package org.camunda.bpm.engine.osgi.web;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.service.http.HttpService;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

/**
 *
 * @author istvanszabo
 */
public class HttpServiceTracker extends ServiceTracker {

    public HttpServiceTracker(BundleContext context) {
        super(context, HttpService.class.getName(), null);
    }

    @Override
    public Object addingService(ServiceReference reference) {
        HttpService httpService = (HttpService) super.addingService(reference);
        if (httpService == null) {
            return null;
        }

        try {            
            //Admin Api
            System.out.println("Registering servlet at /api/admin/*");
            HttpServletDispatcher adminApi = new HttpServletDispatcher();
            
            Dictionary<String, String> adminInitParams = new Hashtable<String, String>();
            adminInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.admin.impl.web.AdminApplication");
            adminInitParams.put("resteasy.servlet.mapping.prefix", "/api/admin");

            httpService.registerServlet("/api/admin/*", adminApi, adminInitParams, null);
            
            //Engine Api
            System.out.println("Registering servlet at /api/engine/*");
            HttpServletDispatcher engineApi = new HttpServletDispatcher();
            
            Dictionary<String, String> engineInitParams = new Hashtable<String, String>();
            engineInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.webapp.impl.engine.EngineRestApplication");
            engineInitParams.put("resteasy.servlet.mapping.prefix", "/api/engine");

            httpService.registerServlet("/api/engine/*", engineApi, engineInitParams, null);
            
            //Cockpit Api
            System.out.println("Registering servlet at /api/cockpit/*");
            HttpServletDispatcher cockpitApi = new HttpServletDispatcher();
            
            Dictionary<String, String> cockpitInitParams = new Hashtable<String, String>();
            cockpitInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.cockpit.impl.web.CockpitApplication");
            cockpitInitParams.put("resteasy.servlet.mapping.prefix", "/api/cockpit");

            httpService.registerServlet("/api/cockpit/*", cockpitApi, cockpitInitParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpService;
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        HttpService httpService = (HttpService) service;

        System.out.println("Unregistering...");
        httpService.unregister("/api/admin/*");
        httpService.unregister("/api/engine/*");
        httpService.unregister("/api/cockpit/*");

        super.removedService(reference, service);
    }
}
