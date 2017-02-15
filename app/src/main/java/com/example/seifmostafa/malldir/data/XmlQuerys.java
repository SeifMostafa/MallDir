package com.example.seifmostafa.malldir.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The XmlQuerys program implements an
 * application that make you connect to 
 * xml file and read/write on this file.
 *
 * @author  Mahmoud Ameen.
 * @version 2.0
 * @since   2016-10-31 
 */
public class XmlQuerys {

	private XmlData xmlData = new XmlData();

	private List<NodeList> nodeList = new ArrayList<NodeList>();

	/**
	 * This method is the class constructor used to set xml file path
	 * and to set xml root node.
	 * @param xmlFilePath This is the first parameter to set xml file path.
	 * @param rootNode  This is the second parameter to set xml root node.
	 */
	public XmlQuerys(String xmlFilePath, String rootNode)
	{
		File f = new File(xmlFilePath);
		if(!f.exists()) { 
			System.err.println("Xml file does not exists!");

		}
		else {
			xmlData.setXML_Path(xmlFilePath);

			if (rootNode.equals("")) {
				xmlData.setRoot_Node(rootNode);
			}
			else {
				xmlData.setXML_Path(xmlFilePath);
				xmlData.IntiData();
				Document doc = xmlData.getXML_DOC();
				XPath xPath =  XPathFactory.newInstance().newXPath();
				try {
					Node node = (Node) xPath.compile(rootNode).evaluate(doc, XPathConstants.NODE);
					if (node.getNodeName().equals(rootNode)) {
						xmlData.setRoot_Node(rootNode + "/");
					}
				} catch (Exception e) {
					System.err.println("Root node does not exists!");
				}
			}	
		}
	}

	/**
	 * This method is used to get last ID of list of nodes.
	 * @param parent This is the first parameter to set parent node.
	 * @param child  This is the second parameter to set child node.
	 *@return the value of last ID.
	 */
	public String get_last_id(String parent, String child){

		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();

		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = xmlData.getRoot_Node() + parent + "/" + child + "[@ID]";
		String lastID = "1";
		try {
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			ArrayList<Integer> arrayList = new ArrayList<Integer>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element element=(Element) node;
				arrayList.add(Integer.parseInt(element.getAttribute("ID")));
			}
			if(!arrayList.isEmpty()){ lastID = ""+(Collections.max(arrayList) + 1) ; }
			else { lastID = "1"; }

		} catch (XPathExpressionException e1) { e1.printStackTrace(); }
		return lastID;
	}

	/**
	 * This method is used to make where condition.
	 * @param where This is the first parameter to set where.
	 *@return the value of condition.
	 */
	private String makeWhere(String where) {
		String result = "";

		if (where.contains("<=")) { 
			result = "[@" + where.split("<=")[0] + "<=\"" + where.split("<=")[1].toLowerCase() + "\" or @" + where.split("<=")[0] + "<=\"" + where.split("<=")[1].toUpperCase() + "\"]"; 
		}
		else if (where.contains("!=")) { 
			result = "[@" + where.split("!=")[0] + "!=\"" + where.split("!=")[1].toLowerCase() + "\" or @" + where.split("!=")[0] + "!=\"" + where.split("!=")[1].toUpperCase() + "\"]"; 
		}
		else if (where.contains(">=")) { result = "[@" + where.split(">=")[0] + ">=\"" + where.split(">=")[1] + "\"]"; }
		else if (where.contains(">")) { result = "[@" + where.split(">")[0] + ">\"" + where.split(">")[1] + "\"]"; }
		else if (where.contains("<")) { result = "[@" + where.split("<")[0] + "<\"" + where.split("<")[1] + "\"]"; }
		else if (where.contains("=")) { result = "[@" + where.split("=")[0] + "=\"" + where.split("=")[1] + "\"]"; }
		else if (where.equals("")) { result = ""; }
		else {
			result = "Wrong condition!";
			System.err.println("Wrong condition!");
		}

		return result;
	}

	/**
	 * This method is used to insert xml element.
	 * @param parent This is the first paramter to set parent node.
	 * @param child  This is the second parameter to set child node.
	 * @param nodeIndex  This is the thared parameter to set index of same nodes Make it's value=0 as default.
	 * @param arrOfAttributesAndVaules  This is the fourth parameter to set Array of attributes and it's values
	 * Example: new String[] {"name=Mahmoud Ameen", "job=Java developer"};.
	 */
	public void insert(String parent, String child, int nodeIndex, String[] arrOfAttributesAndVaules){

		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();

		Node itmParent = doc.getElementsByTagName(parent).item(nodeIndex);
		Element itmChild = doc.createElement(child);

		// item data block
		//itmChild.setAttribute("ID", get_last_id(parent, child));
		for (int i = 0; i < arrOfAttributesAndVaules.length; i++) {
			itmChild.setAttribute(arrOfAttributesAndVaules[i].split("=")[0], arrOfAttributesAndVaules[i].split("=")[1]);
		}

		itmParent.appendChild(itmChild);
		xmlData.setXML_DOC(doc);
	}
	
	/**
	 * This method is used to insert xml element.
	 * @param parent This is the first paramter to set parent node.
	 * @param nodeIndex  This is the thared parameter to set index of same nodes Make it's value=0 as default.
	 * @param element pass an element.
	 */
	public void insert(String parent, int nodeIndex, Element element){

		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();

		Node itmParent = doc.getElementsByTagName(parent).item(nodeIndex);
		
		itmParent.appendChild(doc.importNode(element, true));
		xmlData.setXML_DOC(doc);
	}
	
/*	public static void main(String[] args) {
		XmlQuerys querys = new XmlQuerys("C:\\Users\\Mahmoud\\Desktop\\5-2016.xml", "");
		
		querys.insert("data", "event", 0, new String[]{"brideImage=kk", "brideName=uu", "groomImage=ii", "groomName=oo"
				, "id=444", "partyType=tt"});
		
		NodeList l = querys.select("sheet/day", "date=1-5-2016");
		Element e = (Element)l.item(0) ;
//		e.setAttribute("id", "4");
		
		querys.insert("sheet", 0, e);
	}*/

	/**
	 * This method is used to delete xml elment.
	 * @param ExpressionValue This is the first paramter to set Expression Value 
	 * Example: "Floors/Floor".
	 * @param whereAttributeNameANDvalue  This is the second parameter to set condition to expression
	 * Example: "ID=1".
	 */
	public void delete(String ExpressionValue, String whereAttributeNameANDvalue){

		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();
		XPath xPath =  XPathFactory.newInstance().newXPath();

		if (!makeWhere(whereAttributeNameANDvalue).equals("Wrong condition!")) {
			String expression = xmlData.getRoot_Node() + ExpressionValue + makeWhere(whereAttributeNameANDvalue);

			try {
				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
				Node node;
				for (int i = 0; i < nodeList.getLength(); i++) {
					node = nodeList.item(i);
					node.getParentNode().removeChild(node);
				}

			} catch (XPathExpressionException e1) { e1.printStackTrace(); }

			xmlData.setXML_DOC(doc);
		}
	}

	/**
	 * This method is used to update xml element.
	 * @param ExpressionValue This is the first paramter to set Expression Value 
	 * Example: "Floors/Floor".
	 * @param whereAttributeNameANDvalue  This is the second parameter to set condition to expression
	 * Example: "ID=1".
	 * @param arrOfAttributesAndVaules  This is the thared parameter to set Array of attributes and it's values
	 * Example: new String[] {"name=Mahmoud Ameen", "job=Java developer"};.
	 */
	public void update(String ExpressionValue, String whereAttributeNameANDvalue, String[] arrOfAttributesAndVaules){
		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();
		XPath xPath =  XPathFactory.newInstance().newXPath();

		if (!makeWhere(whereAttributeNameANDvalue).equals("Wrong condition!")) {
			String expression = xmlData.getRoot_Node() + ExpressionValue + makeWhere(whereAttributeNameANDvalue);

			try {
				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
				Node node;   Element itmChild;

				for (int i = 0; i < nodeList.getLength(); i++) {
					node = nodeList.item(i);
					itmChild = (Element) node;

					for (int j = 0; j < arrOfAttributesAndVaules.length; j++) {
						itmChild.setAttribute(arrOfAttributesAndVaules[j].split("=")[0], arrOfAttributesAndVaules[j].split("=")[1]);
					}
				}
			} catch (XPathExpressionException e1) { e1.printStackTrace(); }

			xmlData.setXML_DOC(doc);
		}
	}

	/**
	 * This method is used to select xml element.
	 * @param ExpressionValue This is the first paramter to set Expression Value 
	 * Example: "Floors/Floor".
	 * @param whereAttributeNameANDvalue  This is the second parameter to set condition to expression
	 * Example: "ID=1".
	 * @return NodeList of selected Nodes.
	 */
	public NodeList select(String ExpressionValue, String whereAttributeNameANDvalue){
		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();
		XPath xPath =  XPathFactory.newInstance().newXPath();

		NodeList nodeList = null;
		if (!makeWhere(whereAttributeNameANDvalue).equals("Wrong condition!")) {
			String expression = xmlData.getRoot_Node() + ExpressionValue + makeWhere(whereAttributeNameANDvalue);


			try {
				nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e1) { e1.printStackTrace(); }
		}
		return nodeList;
	}

	/**
	 * This method is used to select xml element by contains.
	 * @param ExpressionValue This is the first paramter to set Expression Value 
	 * Example: "Floors/Floor".
	 * @param attName  This is the second parameter to set Attribute name.
	 * @param searchValue  This is the thared parameter to set search text
	 * @return NodeList of selected Nodes.
	 */
	public NodeList selectContains(String ExpressionValue, String attName, String searchText){
		xmlData.IntiData();
		Document doc = xmlData.getXML_DOC();
		XPath xPath =  XPathFactory.newInstance().newXPath();

		NodeList nodeList = null;

		String expression = xmlData.getRoot_Node() + ExpressionValue 
				+ "[contains(translate(@" + attName + ", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),\"" + searchText.toLowerCase() + "\")]";

		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e1) { e1.printStackTrace(); }

		return nodeList;
	}

	/**
	 * This method is used to create Join between Multi NodeLists.
	 */
	public void createJoin() {
		nodeList.clear();
	}

	/**
	 * This method is used to Add NodeLists.
	 * @param list This is the first paramter to Add NodeList.
	 * @see You must call createJoin() function before call this Function.
	 */
	public void joinNodeList(NodeList list) {
		if (list.getLength() > 0) {
			nodeList.add(list);
		}
	}

	/**
	 * This method is used to Get List of Joined Nodes.
	 * @return List of selected Joined Nodes.
	 */
	public List<Node> getJoinedNodes() {
		List<Node> nodes = new ArrayList<Node>();

		if (!nodeList.isEmpty()) {
			for (int i = 0; i < nodeList.size(); i++) {
				for (int j = 0; j < ((NodeList)nodeList.get(i)).getLength(); j++) {
					nodes.add(((NodeList)nodeList.get(i)).item(j));
				}
			}
		}
		else {
			System.err.println("List is Empty!");
		}

		return nodes;
	}

	static String parent, child;
	public static String makeParentAndChildByParent(String itmType) {

		if (itmType.equals("Categories")) {
			parent = "Categories";
			child = "Category";
		}
		else if (itmType.equals("Pharmacies")) {
			parent = "Pharmacies";
			child = "Pharmacy";
		}
		else if (itmType.equals("Movies")) {
			parent = "Cinemas";
			child = "Cinema";
		}
		else {
			parent = itmType;
			child = itmType.substring(0, itmType.length()-1);
		}

		return parent + "/" + child;
	}
}
