package org.camunda.bpm.engine.osgi.web;

import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.ServletException;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.ops4j.pax.web.service.WebContainer;

import org.camunda.bpm.webapp.impl.security.auth.AuthenticationFilter;
import org.camunda.bpm.cockpit.impl.web.filter.plugin.ClientPluginsFilter;
import org.camunda.bpm.engine.rest.filter.CacheControlFilter;
import org.camunda.bpm.webapp.impl.engine.ProcessEnginesFilter;
import org.camunda.bpm.cockpit.impl.web.bootstrap.CockpitContainerBootstrap;
import org.camunda.bpm.webapp.impl.security.filter.SecurityFilter;

/**
 *
 * @author istvanszabo
 */
public class CamundaWebappWebRegister implements Runnable {

    private WebContainer webContainer;

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            registerResources();
            registerServlets();
            registerFilters();
            registerListener();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setWebContainerService(WebContainer webContainer) {
        this.webContainer = webContainer;
    }

    private void registerServlets() throws ServletException {
        //Admin Api
        System.out.println("Registering servlet at /api/admin/*");
        HttpServletDispatcher adminApi = new HttpServletDispatcher();

        Dictionary<String, String> adminInitParams = new Hashtable<String, String>();
        adminInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.admin.impl.web.AdminApplication");
        adminInitParams.put("resteasy.servlet.mapping.prefix", "/api/admin");

        webContainer.registerServlet(adminApi, new String[]{"/api/admin/*"}, adminInitParams, null);

        //Engine Api
        System.out.println("Registering servlet at /api/engine/*");
        HttpServletDispatcher engineApi = new HttpServletDispatcher();

        Dictionary<String, String> engineInitParams = new Hashtable<String, String>();
        engineInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.webapp.impl.engine.EngineRestApplication");
        engineInitParams.put("resteasy.servlet.mapping.prefix", "/api/engine");

        webContainer.registerServlet(engineApi, new String[]{"/api/engine/*"}, engineInitParams, null);

        //Cockpit Api
        System.out.println("Registering servlet at /api/cockpit/*");
        HttpServletDispatcher cockpitApi = new HttpServletDispatcher();

        Dictionary<String, String> cockpitInitParams = new Hashtable<String, String>();
        cockpitInitParams.put("javax.ws.rs.Application", "org.camunda.bpm.cockpit.impl.web.CockpitApplication");
        cockpitInitParams.put("resteasy.servlet.mapping.prefix", "/api/cockpit");

        webContainer.registerServlet(cockpitApi, new String[]{"/api/cockpit/*"}, cockpitInitParams, null);
    }

    private void registerFilters() {
        //Authentication filter
        System.out.println("Registering authentication filter");
        AuthenticationFilter authFilter = new AuthenticationFilter();

        webContainer.registerFilter(authFilter, new String[]{"/*"}, null, null, null);

        //Security filter
        System.out.println("Registering security filter");
        SecurityFilter secFilter = new SecurityFilter();
        
        Dictionary<String, String> secFilterInitParams = new Hashtable<String, String>();
        secFilterInitParams.put("configFile", "/WEB-INF/securityFilterRules.json");
        
        webContainer.registerFilter(secFilter, new String[]{"/*"}, null, null, null);
        
        //Client plugin filter
        System.out.println("Registering client plugin filter");
        ClientPluginsFilter clientPluginFilterFirst = new ClientPluginsFilter();

        webContainer.registerFilter(clientPluginFilterFirst, new String[]{"/app/cockpit/cockpit-bootstrap.js"}, null, null, null);

        ClientPluginsFilter clientPluginFilterSecond = new ClientPluginsFilter();

        webContainer.registerFilter(clientPluginFilterSecond, new String[]{"/app/cockpit/cockpit.js"}, null, null, null);

        //REST cache control filter
        System.out.println("Registering REST cache control filter");
        CacheControlFilter cacheControlFilter = new CacheControlFilter();

        webContainer.registerFilter(cacheControlFilter, new String[]{"/api/*"}, null, null, null);

        //Engines filter
        System.out.println("Registering engine filter");
        ProcessEnginesFilter enginesFilter = new ProcessEnginesFilter();

        webContainer.registerFilter(enginesFilter, new String[]{"/app/*"}, null, null, null);
    }

    private void registerListener() {
        //Cockpit bootstrap listener
        System.out.println("Registering bootstrap listener");
        CockpitContainerBootstrap bootstrapListener = new CockpitContainerBootstrap();

        webContainer.registerEventListener(bootstrapListener, null);
    }

    private void registerResources() throws Exception {
        webContainer.registerResources("/html", "app/cockpit", null);
        webContainer.registerJsps(new String[]{"/camunda"}, null);
    }
}
