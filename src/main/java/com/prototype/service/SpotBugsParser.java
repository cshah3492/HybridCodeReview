package com.prototype.service;

import com.prototype.model.Finding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				Element bugElement = (Element) node;

				Finding finding = new Finding();

				finding.setBugType(bugElement.getAttribute("type"));

				finding.setCategory(bugElement.getAttribute("category"));

				finding.setPriority(bugElement.getAttribute("priority"));

				/*
				 * SpotBugs findings may contain several Class, Method and SourceLine elements.
				 *
				 * We first look for the element marked primary="true". If SpotBugs does not
				 * provide one, we fall back to the first available element.
				 */

				Element classElement = findPrimaryOrFirst(bugElement, "Class");

				if (classElement != null) {
					finding.setClassName(classElement.getAttribute("classname"));
				}

				Element methodElement = findPrimaryOrFirst(bugElement, "Method");

				if (methodElement != null) {
					finding.setMethodName(methodElement.getAttribute("name"));
				}

				Element sourceElement = findPrimaryOrFirst(bugElement, "SourceLine");

				if (sourceElement != null) {
					finding.setSourceFile(sourceElement.getAttribute("sourcefile"));

					finding.setStartLine(parseIntegerAttribute(sourceElement, "start"));

					finding.setEndLine(parseIntegerAttribute(sourceElement, "end"));
				}

				NodeList shortMessageNodes = bugElement.getElementsByTagName("ShortMessage");

				if (shortMessageNodes.getLength() > 0) {
					finding.setMessage(shortMessageNodes.item(0).getTextContent().trim());
				}

				findings.add(finding);
			}

		} catch (Exception e) {
			System.err.println("Error parsing SpotBugs XML: " + e.getMessage());

			e.printStackTrace();
		}

		return findings;
	}

	/**
	 * Returns the element marked primary="true". If no primary element exists,
	 * returns the first element.
	 */
	private Element findPrimaryOrFirst(Element parent, String tagName) {
		NodeList nodes = parent.getElementsByTagName(tagName);

		Element firstElement = null;

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;

			if (firstElement == null) {
				firstElement = element;
			}

			if ("true".equalsIgnoreCase(element.getAttribute("primary"))) {
				return element;
			}
		}

		return firstElement;
	}

	/**
	 * Safely parses an integer XML attribute.
	 */
	private int parseIntegerAttribute(Element element, String attributeName) {
		try {
			String value = element.getAttribute(attributeName);

			if (value == null || value.trim().isEmpty()) {
				return 0;
			}

			return Integer.parseInt(value.trim());

		} catch (NumberFormatException e) {
			return 0;
		}
	}
}