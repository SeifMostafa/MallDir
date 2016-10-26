package com.example.seifmostafa.malldir.file_model;

import android.util.Log;

import com.example.seifmostafa.malldir.data_model.AccessPoint;
import com.example.seifmostafa.malldir.data_model.Floor;
import com.example.seifmostafa.malldir.data_model.MyMapNode;
import com.logica.xmlengine.querys.XmlQuerys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XReader {

	// instance vars
	private XPath xPath;
	private Document doc;
	public XReader() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	public boolean checkMap() {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		NodeList nodelist;
		String test_exp = "Map/Nodes/Node";

		try {
			nodelist = (NodeList) xPath.compile(test_exp).evaluate(doc, XPathConstants.NODESET);
			if (nodelist.getLength() > 0) {
				return true;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Floor> getFloors() {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		ArrayList<Floor> floors = new ArrayList<>();
		String expression = "/Map/Floors/Floor";
		try {
			NodeList nodelist = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			for (int i = 0; i < nodelist.getLength(); i++) {
				if (nodelist.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodelist.item(i);
					int id = Integer.parseInt(element.getAttribute("ID"));
					String name = element.getAttribute("FloorName");
					String path = element.getAttribute("ImagePath");
					int xFactor = Integer.parseInt(element.getAttribute("xFactor"));
					int yFactor = Integer.parseInt(element.getAttribute("yFactor"));
					Floor floor = new Floor(id, name, path, xFactor, yFactor);
					floors.add(floor);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return floors;
	}

	public Floor getFloor(int id) {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		String expression = "/Map/Floors/Floor[@ID="+id+"]";
		try {
			NodeList nodelist = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			Element element = (Element) nodelist.item(0);
			String name = element.getAttribute("FloorName");
			String path = element.getAttribute("ImagePath");
			int xFactor = Integer.parseInt(element.getAttribute("xFactor"));
			int yFactor = Integer.parseInt(element.getAttribute("yFactor"));
			return new Floor(id, name, path, xFactor, yFactor);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<MyMapNode> getNodesByFloor(int floorID) {

		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		ArrayList<MyMapNode> mapNodes = new ArrayList<>();
		MyMapNode mapNode;
		Element element;
		Node node;
		try {
			String expression = "/Map/Nodes/Node[@FloorID=" + floorID+ "]";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					int id = Integer.parseInt(element.getAttribute("ID"));
					String name = element.getAttribute("Name");
					String type = element.getAttribute("Type");
					int typeId = Integer.parseInt(element.getAttribute("TypeID"));
					String direction=element.getAttribute("Access");
					double x = Double.parseDouble(element.getAttribute("x"));
					double y = Double.parseDouble(element.getAttribute("y"));
					String join = element.getAttribute("join");

					mapNode = new MyMapNode(id, name, floorID, type,typeId,direction, x, y, join);
					mapNodes.add(mapNode);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return mapNodes;
	}

	public AccessPoint getAccess(String type, int typeId) {
		AccessPoint result = null;
		NodeList nodelist;
		Element element;
		String expression = "/Map/" + type + "s/" + type + "[@ID=" + typeId+ "]";

		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		try {
			nodelist = (NodeList) xPath.compile(expression).evaluate(doc,XPathConstants.NODESET);
			element = (Element) nodelist.item(0);
			int id = Integer.parseInt(element.getAttribute("ID"));
			String name = element.getAttribute("name");
			String range = element.getAttribute("range");
			result = new AccessPoint(id, name, range, type);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return result;
	}

	public MyMapNode getMapNode(int id) {
		MyMapNode mapNode = null;
		NodeList nodelist;
		Node node;
		Element element;
		String expression = "/Map/Nodes/Node[@ID=" + id + "]";

		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		try {
			nodelist = (NodeList) xPath.compile(expression).evaluate(doc,XPathConstants.NODESET);
			node = nodelist.item(0);
			element = (Element) node;
			String name=element.getAttribute("Name");
			if (name==null) {
				name="";
			}
			int floorId = Integer.parseInt(element.getAttribute("FloorID"));
			String type = element.getAttribute("Type");
			double x = Double.parseDouble(element.getAttribute("x"));
			double y = Double.parseDouble(element.getAttribute("y"));
			String join = element.getAttribute("join");
			int typeId = Integer.parseInt(element.getAttribute("TypeID"));
			String direction=element.getAttribute("Access");
			mapNode=new MyMapNode(id, name, floorId, type, typeId, direction, x, y, join);
		} catch (XPathExpressionException e) {
			Log.e("XReader", "Xpath Expression Error while retrieving node ");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.e("XReader", "Cannot retrieve node");
		}

		return mapNode;
	}

	public ArrayList<MyMapNode> getAllAccess(int floorId) {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		ArrayList<MyMapNode> accesses=new ArrayList<>();
		MyMapNode access;
		Element element;
		Node node;

		String expression = "/Map/Nodes/Node[@FloorID=" + floorId+ "]";
		try {
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					String type = element.getAttribute("Type");
					if (type.equals("Escalator")||type.equals("Elevator")) {
						int id = Integer.parseInt(element.getAttribute("ID"));
						String name = element.getAttribute("Name");
						int typeId = Integer.parseInt(element.getAttribute("TypeID"));
						String direction=element.getAttribute("Access");
						double x = Double.parseDouble(element.getAttribute("x"));
						double y = Double.parseDouble(element.getAttribute("y"));
						String join = element.getAttribute("join");

						access = new MyMapNode(id, name, floorId, type,typeId, direction, x, y, join);
						accesses.add(access);
					}
					else {
						continue;
					}
				}

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return accesses;

	}

	public boolean floorInRange(String name,int floorId){
		String accessName=name.substring(0,name.indexOf(" "));
		int id=Integer.parseInt(name.substring(name.indexOf(" ")+1));

		NodeList nodelist;
		Element element;
		String expression = "/Map/"+accessName+"s"+"/"+accessName+"[@ID="+id+"]";

		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		try {
			nodelist=(NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodelist.getLength(); i++) {
				element=(Element) nodelist.item(i);
				String range=element.getAttribute("range");
				if (range.contains(""+floorId)) {
					return true;
				}
			}
		} catch (XPathExpressionException e) {
			Log.e("XReader",e.getMessage());
			return false;
		}

		return false;
	}

	public boolean hasNode(String type,int typeId) {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		Element element;
		Node node;
		String expression="";
		if (type.equals("Pharmacy")) {
			expression="/Map/Pharmacies/Pharmacy[@ID="+typeId+"]";
		}
		else {
			expression="/Map/" + type + "s/" + type+"[@ID="+typeId+"]";
		}

		try {
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			node = nodeList.item(0);
			if (node!=null&&node.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) node;
				return Boolean.parseBoolean(element.getAttribute("hasNode"));
			}
			return false;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getCurrentFloorId() {
		XmlData.IntiData();
		doc = XmlData.getXML_DOC();

		NodeList nodelist;
		Element element;
		nodelist = doc.getElementsByTagName("Info");
		element=(Element) nodelist.item(0);
		int id = Integer.parseInt(element.getAttribute("currentScreenID"));

		XmlQuerys query=new XmlQuerys("D:/MallEditor/Resources/Map.xml", "Map");
		query.createJoin();
		query.joinNodeList(query.select("Nodes/Node", "Type=Screen"));
		query.joinNodeList(query.select("Nodes/Node", "TypeID="+id));
		for (Node node : query.getJoinedNodes()) {
			if (node.getNodeType()==Node.ELEMENT_NODE) {
				return Integer.parseInt(((Element)node).getAttribute("FloorID"));

			}
		}
		return getMapNode(id).getFloorId();
	}

	public int getCurrentScreenId() {
		XmlQuerys query=new XmlQuerys("D:/MallEditor/Resources/Map.xml", "Map");
		NodeList nodelist= query.select("Info", "");
		Element element = (Element) nodelist.item(0);
		int screenId = Integer.parseInt(element.getAttribute("currentScreenID"));
		nodelist=query.select("Nodes/Node", "Type=Screen"/*+"TypeID="+screenId*/);
		for (int i = 0; i < nodelist.getLength(); i++) {
			element = (Element) nodelist.item(i);
			if (element.getAttribute("TypeID").contains(""+screenId)) {
				return Integer.parseInt(element.getAttribute("ID"));
			}
		}
		return 0;
	}

	public int getNodeId(String type,int typeId) {
		XmlQuerys query=new XmlQuerys("D:/MallEditor/Resources/Map.xml", "Map");
		NodeList nodelist=query.select("Nodes/Node[@Type='"+type+"' and "+"@TypeID='"+typeId+"']", "");
//		List<Node> nodes = query.getJoinedNodes();
		Element element = (Element) nodelist.item(0);
		if (element==null) {
			return 0;
		}
	return Integer.parseInt(element.getAttribute("ID"));
	}
	
}