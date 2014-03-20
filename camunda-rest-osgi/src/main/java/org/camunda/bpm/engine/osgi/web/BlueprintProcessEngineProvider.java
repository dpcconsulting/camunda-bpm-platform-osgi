package org.camunda.bpm.engine.osgi.web;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlueprintProcessEngineProvider implements ProcessEngineProvider {


  @Override
  public ProcessEngine getDefaultProcessEngine() {
    BundleContext context = FrameworkUtil.getBundle(BlueprintProcessEngineProvider.class).getBundleContext();
    ServiceReference ref = context.getServiceReference(ProcessEngine.class.getName());
    return (ProcessEngine) context.getService(ref);
  }

  @Override
  public ProcessEngine getProcessEngine(String s) {
    return getDefaultProcessEngine();
  }

  @Override
  public Set<String> getProcessEngineNames() {
    if (getDefaultProcessEngine() != null) {
      return new HashSet<String>() {{
        add("default");
      }};
    } else {
      return new HashSet<String>();
    }

  }
}
