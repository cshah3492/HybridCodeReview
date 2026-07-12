package com.prototype.service;

import org.w3c.dom.*;

import com.prototype.model.Finding;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpotBugsParser {

    public List<Finding> parse(String xmlPath) {
        List<Finding> findings = new ArrayList<Finding>();

        try {
            File file = new File(xmlPath);

            if (!file.exists()) {
                System.out.println("SpotBugs XML not found: " + xmlPath);
                return findings;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList bugInstances = document.getElementsByTagName("BugInstance");

            for (int i = 0; i < bugInstances.getLength(); i++) {
                Node node = bugInstances.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element bugElement = (Element) node;

                    Finding finding = new Finding();
                    finding.setBugType(bugElement.getAttribute("type"));
                    finding.setCategory(bugElement.getAttribute("category"));
                    finding.setPriority(bugElement.getAttribute("priority"));

                    NodeList classNodes = bugElement.getElementsByTagName("Class");
                    if (classNodes.getLength() > 0) {
                        Element classElement = (Element) classNodes.item(0);
                        finding.setClassName(classElement.getAttribute("classname"));
                    }

                    NodeList methodNodes = bugElement.getElementsByTagName("Method");
                    if (methodNodes.getLength() > 0) {
                        Element methodElement = (Element) methodNodes.item(0);
                        finding.setMethodName(methodElement.getAttribute("name"));
                    }

                    NodeList sourceNodes = bugElement.getElementsByTagName("SourceLine");
                    if (sourceNodes.getLength() > 0) {
                        Element sourceElement = (Element) sourceNodes.item(0);
                        finding.setSourceFile(sourceElement.getAttribute("sourcefile"));

                        try {
                            finding.setStartLine(Integer.parseInt(sourceElement.getAttribute("start")));
                        } catch (Exception ignored) {}

                        try {
                            finding.setEndLine(Integer.parseInt(sourceElement.getAttribute("end")));
                        } catch (Exception ignored) {}
                    }

                    NodeList shortMessageNodes = bugElement.getElementsByTagName("ShortMessage");
                    if (shortMessageNodes.getLength() > 0) {
                        finding.setMessage(shortMessageNodes.item(0).getTextContent());
                    }

                    findings.add(finding);
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing SpotBugs XML: " + e.getMessage());
        }

        return findings;
    }
}