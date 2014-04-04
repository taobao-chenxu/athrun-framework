package org.athrun.android.framework.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

final class XmlParser {

	private XmlParser() {
		throw new AssertionError();
	}
//
//	public static void main(String[] args) {
//		getAllActions(new File("./res/login.xml"));
//	}
	
	static Element getRootElement(File xmlFile) {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(xmlFile);
			Element root = document.getRootElement();
			
			return root;
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	static List<Map<String, String>> getAllActions(File xmlFile) {
		ArrayList<Map<String, String>> actions = new ArrayList<Map<String, String>>();

		Element root = getRootElement(xmlFile);
		
		for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			Map<String, String> actionAttributes = getActionAttributes(element);
			actions.add(actionAttributes);
		}

		return actions;
	}

	private static Map<String, String> getActionAttributes(Element element) {
		Map<String, String> attributes = new HashMap<String, String>();

		for (Iterator<?> iter = element.attributeIterator(); iter.hasNext();) {
			Attribute attr = (Attribute) iter.next();
			String key = attr.getName();
			String value = attr.getValue();
			attributes.put(key, value);
		}

		return attributes;
	}
}
