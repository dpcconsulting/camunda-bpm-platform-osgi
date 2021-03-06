/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.osgi;

import org.apache.felix.fileinstall.ArtifactUrlTransformer;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A fileinstall deployer transforming a BPMN xml definition file into
 * an installable bundle.
 *
 * @author <a href="gnodet@gmail.com">Guillaume Nodet</a>
 */
public class BpmnDeploymentListener implements ArtifactUrlTransformer {

    private static final Logger LOGGER = Logger.getLogger(BpmnDeploymentListener.class.getName());

    private DocumentBuilderFactory dbf;

    public boolean canHandle(File artifact) {
        try {
            String artifactName = artifact.getName();
			if (artifact.isFile() && (artifactName.endsWith(".xml") || artifactName.endsWith(".bpmn"))) {
                Document doc = parse(artifact);
                String name = doc.getDocumentElement().getLocalName();
                String uri  = doc.getDocumentElement().getNamespaceURI();
                if ("definitions".equals(name) && "http://www.omg.org/spec/BPMN/20100524/MODEL".equals(uri)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to parse deployed file " + artifact.getAbsolutePath(), e);
        }
        return false;
    }

    public URL transform(URL artifact) {
        try {
            return new URL("bpmn", null, artifact.toString());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to build BPMN bundle", e);
            return null;
        }
    }

    protected Document parse(File artifact) throws Exception {
        if (dbf == null) {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
        }
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException exception) throws SAXException {
            }
            public void error(SAXParseException exception) throws SAXException {
            }
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });
        return db.parse(artifact);
    }

}
