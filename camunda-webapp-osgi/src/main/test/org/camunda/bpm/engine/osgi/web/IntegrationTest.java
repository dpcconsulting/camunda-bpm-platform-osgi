package org.camunda.bpm.engine.osgi.web;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;

@RunWith(PaxExam.class)
public class IntegrationTest {

  @Configuration
  public Option[] config() {
    return new Option[]{
        keepRuntimeFolder(),
        logLevel(LogLevel.ERROR) };
  }

  @Test
  public void test() throws Exception {
    assertTrue(true);
    fail();
  }
}
