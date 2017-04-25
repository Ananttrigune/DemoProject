package handleXML;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javaTestHelper.ProjectInfo;
import utility.MaintainLog;
import utility.ReadProperties;

public class GenerateXML {

	public static void main(String argv[]) {
		System.out.println("...This application generates XML from input project tests...");
		MaintainLog.logInfo("***Program execution started***");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			// Get ProjectFolder and ProjectName
			String projectFolder = ProjectInfo.getProjectFolder();
			String projectName = ProjectInfo.getProjectName(projectFolder);
			ArrayList<String> classNames = ProjectInfo.getAllClassesNames(projectFolder);
			MaintainLog.logInfo("Collected all inputs for Project");

			// Get all XML Tags, Attributes
			String Root_Tag = ReadProperties.getValueName("Root_Tag");
			String Root_Tag_Attribute = ReadProperties.getValueName("Root_Tag_Attribute");
			String Parent_Tag = ReadProperties.getValueName("Parent_Tag");
			String Child_Tag = ReadProperties.getValueName("Child_Tag");
			String Child_Tag_Attribute = ReadProperties.getValueName("Child_Tag_Attribute");
			String Sub_Child_Tag = ReadProperties.getValueName("Sub_Child_Tag");
			String Sub_Child_DetailTag = ReadProperties.getValueName("Sub_Child_DetailTag");
			String Sub_Child_DetailTag_Attribute = ReadProperties.getValueName("Sub_Child_DetailTag_Attribute");

			// Root element
			Element projectTag = doc.createElement(Root_Tag);
			doc.appendChild(projectTag);
			Attr attrProjectName = doc.createAttribute(Root_Tag_Attribute);
			attrProjectName.setValue(projectName);
			projectTag.setAttributeNode(attrProjectName);
			MaintainLog.logInfo("XML-Tag done with Tag name: " + Root_Tag + " Attribute: " + Root_Tag_Attribute
					+ " Attribute value: " + projectName);

			// Parent element
			Element classesTag = doc.createElement(Parent_Tag);
			projectTag.appendChild(classesTag);
			MaintainLog.logInfo("XML-Tag done with Tag name: " + Parent_Tag);

			// Child Element
			for (int indexClass = 0; indexClass < classNames.size(); indexClass++) {
				Element classTag = doc.createElement(Child_Tag);
				classesTag.appendChild(classTag);
				Attr attrClassName = doc.createAttribute(Child_Tag_Attribute);
				String className = ProjectInfo.getClassName(classNames, indexClass);
				attrClassName.setValue(className.substring(0, className.indexOf(".")));
				classTag.setAttributeNode(attrClassName);
				MaintainLog.logInfo("XML-Tag done with Tag name: " + Child_Tag + " Attribute: " + Child_Tag_Attribute
						+ " Attribute value: " + className.substring(0, className.indexOf(".")));

				// ChildOfChild element
				Element methodTag = doc.createElement(Sub_Child_Tag);
				classTag.appendChild(methodTag);
				ArrayList<String> allMethodsNames = ProjectInfo.getAllMethodsNames(projectFolder, className);
				MaintainLog.logInfo("XML-Tag done with Tag name: " + Sub_Child_Tag);
				for (int indexMethod = 0; indexMethod < allMethodsNames.size(); indexMethod++) {
					// ChildOfChildDetails element
					Element includeTag1 = doc.createElement(Sub_Child_DetailTag);
					methodTag.appendChild(includeTag1);
					Attr attrMethodName1 = doc.createAttribute(Sub_Child_DetailTag_Attribute);
					attrMethodName1.setValue(allMethodsNames.get(indexMethod));
					includeTag1.setAttributeNode(attrMethodName1);
					MaintainLog.logInfo("XML-Tag done with Tag name: " + Sub_Child_DetailTag + " Attribute: "
							+ Sub_Child_DetailTag_Attribute + " Attribute value: " + allMethodsNames.get(indexMethod));
				}
			}

			System.out.println("Generating XML file");
			MaintainLog.logInfo("Saving XML to file");
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setParameter(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			File myXML = new File(projectName + ".xml");
			StreamResult result = new StreamResult(myXML);
			String xmlFilePath = myXML.getAbsolutePath();

			transformer.transform(source, result);
			System.out.println("Sucessfully generated XML file: " + projectName + ".xml");
			MaintainLog.logInfo("Sucessfully generated XML file: " + projectName + ".xml");
			MaintainLog.logInfo("XML file and path: " + xmlFilePath);
			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			MaintainLog.logInfo("***Program execution stopped***");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}