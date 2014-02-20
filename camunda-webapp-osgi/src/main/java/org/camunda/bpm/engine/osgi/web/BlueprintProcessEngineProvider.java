package org.camunda.bpm.engine.osgi.web;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlueprintProcessEngineProvider implements ProcessEngineProvider {


    @Override
    public ProcessEngine getDefaultProcessEngine() {
        return CamundaWebappActivator.ENGINE;
    }

    @Override
    public ProcessEngine getProcessEngine(String s) {
        return CamundaWebappActivator.ENGINE;
    }

    @Override
    public Set<String> getProcessEngineNames() {
        if (CamundaWebappActivator.ENGINE != null) {
            return new HashSet<String>() {{
                add("default");
            }};
        } else {
            return new HashSet<String>();
        }

    }
}
