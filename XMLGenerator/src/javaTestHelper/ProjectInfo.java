package javaTestHelper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import utility.MaintainLog;
import utility.ReadProperties;

public class ProjectInfo {

	public static String getProjectFolder() throws IOException {
		String inputFolder = null;
		String ProjectTestFolder_Identifier1 = ReadProperties.getValueName("ProjectTestFolder_Identifier1");
		String ProjectTestFolder_Identifier2 = ReadProperties.getValueName("ProjectTestFolder_Identifier2");
		MaintainLog.logInfo("Project folder identifiers used: " + ProjectTestFolder_Identifier1 + " and "
				+ ProjectTestFolder_Identifier2);
		try {
			boolean continueFlag = true;
			do {
				MaintainLog.logInfo("Method-getProjectFolder-Asking User to inputs for Project folder");
				System.out.println("Please input the folder path for your Project - Tests");
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
				inputFolder = bufferReader.readLine();
				System.out.println("User input path: " + inputFolder);
				MaintainLog.logInfo("Reading user's input folder path: " + inputFolder);
				if (inputFolder.endsWith(ProjectTestFolder_Identifier1)
						|| inputFolder.endsWith(ProjectTestFolder_Identifier2)) {
					System.out.println("Project Test Folder is correct: " + inputFolder);
					continueFlag = false;
				} else {
					System.out.println("Project folder is invalid. Please try again or press Ctrl+C to exit");
					MaintainLog.logInfo("Project Test Folder is incorrect so iterating same again: " + inputFolder);
					inputFolder = null;
				}
			} while (continueFlag);
		} catch (Exception e) {
			MaintainLog.logError("Exception in ProjectInfo.getProjectFolder: " + e);
		}
		MaintainLog.logInfo("Project folder returing to XML Generator: " + inputFolder);
		return inputFolder;
	}

	public static String getProjectName(String inputFolder) {
		MaintainLog.logInfo("Method-getProjectName-Getting project name from input folder: " + inputFolder);
		String projectName = "";
		String[] project;
		String ProjectTestFolder_Identifier1 = ReadProperties.getValueName("ProjectTestFolder_Identifier1");
		String ProjectTestFolder_Identifier2 = ReadProperties.getValueName("ProjectTestFolder_Identifier2");
		try {
			if (inputFolder.contains("\\")) {
				MaintainLog.logInfo("Ignoring " + ProjectTestFolder_Identifier1 + " in project folder then splitting");
				projectName = inputFolder.substring(0, inputFolder.indexOf(ProjectTestFolder_Identifier1));
				project = projectName.split("\\\\");
			} else {
				MaintainLog.logInfo("Ignoring " + ProjectTestFolder_Identifier2 + " in project folder then splitting");
				projectName = inputFolder.substring(0, inputFolder.indexOf(ProjectTestFolder_Identifier2));
				project = projectName.split("/");
			}
			projectName = project[project.length - 1];
			System.out.println("Project Name: " + projectName);
		} catch (Exception e) {
			System.out.println("Exception in ProjectInfo.getProjectName: " + e);
			MaintainLog.logError("Exception in ProjectInfo.getProjectName: " + e);
		}
		MaintainLog.logInfo("Project Name returing to XML generator: " + projectName);
		return projectName;
	}

	public static ArrayList<String> getAllClassesNames(String inputFolder) {
		String Required_File_Extension = ReadProperties.getValueName("Required_File_Extension");
		MaintainLog.logInfo("Method-getAllClassesNames-Getting all classes under input folder: " + inputFolder
				+ " with file extension as: " + Required_File_Extension);
		ArrayList<String> classNames = new ArrayList<>();
		System.out.println("Files under given path: ");
		try {
			File directory = new File(inputFolder);
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile() && file.getName().substring(file.getName().indexOf('.') + 1, file.getName().length())
						.equalsIgnoreCase(Required_File_Extension)) {
					classNames.add(file.getName());
					System.out.println(file.getName());
				}
			}
			MaintainLog.logInfo("Classes Names: " + classNames);
			MaintainLog.logInfo("Returing list of classes to XML Generator: " + classNames);
			return classNames;
		} catch (Exception e) {
			System.out.println("Exception in ProjectInfo.getAllClassesNames: " + e);
			MaintainLog.logError("Exception in ProjectInfo.getAllClassesNames: " + e);
			return null;
		}
	}

	public static String getClassName(ArrayList<String> classNames, int index) {
		MaintainLog.logInfo("Method-getClassName-Getting class name at index: " + index);
		try {
			MaintainLog.logInfo("Returing class name to XML Generator: " + classNames.get(index));
			return classNames.get(index);
		} catch (Exception e) {
			System.out.println("Exception in ProjectInfo.getClassName: " + e);
			MaintainLog.logError("Exception in ProjectInfo.getClassName");
			return null;
		}
	}

	public static ArrayList<String> getAllMethodsNames(String inputFolder, String classFileName) {
		String Required_Method_Identifier = ReadProperties.getValueName("Required_Method_Identifier");
		String Method_Declaration_Ignoring_Keyword = ReadProperties.getValueName("Method_Declaration_Ignoring_Keyword");
		//String Method_Word_Index = ReadProperties.getValueName("Method_Word_Index");
		MaintainLog.logInfo("Method-getAllMethodsNames-Getting all methods under class file: " + inputFolder + "/"
				+ classFileName + " with method identifer as: " + Required_Method_Identifier);
		ArrayList<String> methodsNames = new ArrayList<>();
		try {
			FileInputStream javaTestFile = new FileInputStream(inputFolder + "/" + classFileName);
			DataInputStream inputStream = new DataInputStream(javaTestFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String stringLine;

			while ((stringLine = br.readLine()) != null) {
				stringLine = stringLine.trim();
				if (stringLine.contains(Required_Method_Identifier)) {
					stringLine = stringLine.substring(0, stringLine.indexOf(Method_Declaration_Ignoring_Keyword));
					String[] LineToWords = stringLine.split(" ");
					// methodsNames.add(LineToWords[Integer.parseInt(Method_Word_Index)]);
					for (int i = 0; i < LineToWords.length; i++) {
						if (LineToWords[i].equalsIgnoreCase(Required_Method_Identifier)) {
							methodsNames.add(LineToWords[i + 1]);
						}
					}
				}
			}
			MaintainLog.logInfo("Methods :" + methodsNames);
			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception in ProjectInfo.getAllMethodsNames: " + e);
		}
		MaintainLog.logInfo("Returing list of methods names to XML Generator: " + methodsNames);
		return methodsNames;
	}

}