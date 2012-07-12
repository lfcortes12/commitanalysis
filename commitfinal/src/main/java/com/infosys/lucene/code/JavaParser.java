package com.infosys.lucene.code;

import java.util.*;
import java.io.*;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaParser {

	@SuppressWarnings("deprecation")
	private ASTParser _parser = ASTParser.newParser(AST.JLS2);
	private CompilationUnit _unit = null;
	private JClass _class = null;

	@SuppressWarnings("resource")
	public  void setSource(String f) {
		//BufferedReader reader;
		try {
			//reader = new BufferedReader(new FileReader(f));
			StringBuffer sourceStr = new StringBuffer(f);
			String tempStr;
			BufferedReader fR = new BufferedReader(new FileReader(f));
			while((tempStr = fR.readLine()) != null)
				sourceStr.append("\n").append(tempStr);
			//reader.close();
			_parser.setKind(ASTParser.K_COMPILATION_UNIT);
			_parser.setSource(sourceStr.toString().toCharArray());
			_unit = (CompilationUnit) _parser.createAST(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  ArrayList getImportDeclarations() {
		List imports = _unit.imports();
		if(imports.size() == 0) return null;
		ArrayList importDecl = new ArrayList();
		ListIterator iter = imports.listIterator();
		while (iter.hasNext()) {
			ImportDeclaration decl = (ImportDeclaration)iter.next();
			importDecl.add(decl.getName().toString());
		}
		return importDecl;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public  ArrayList getComments() {
		List comments = _unit.getCommentList();
		if(comments.size() == 0)return null;
		ArrayList javaDocComments = new ArrayList();
		ListIterator iterator = comments.listIterator();
		while(iterator.hasNext()) {
			Object object =  iterator.next();
			if(object instanceof Javadoc){
				String comment = ((Javadoc)object).getComment();
				javaDocComments.add(comment);
			}
		}
		return javaDocComments;
	}

	@SuppressWarnings("rawtypes")
	public JClass getDeclaredClass() {
		List types = _unit.types();
		ListIterator typeIter = types.listIterator(0);
		if (typeIter.hasNext()) {
			TypeDeclaration object = (TypeDeclaration) typeIter.next();
			_class = new JClass();
			setClassInformation(_class,object);
			return _class;
		}
		return null;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private  void setClassInformation(JClass cls ,TypeDeclaration object) {
		String clsName = object.getName().toString();
		cls.className = clsName;
		Name superClsName = object.getSuperclass();
		if(superClsName != null)
		 cls.superClass =  superClsName.toString();
		 List interfaceLst = object.superInterfaces();
		 ListIterator interfaces = interfaceLst.listIterator();
		 while(interfaces.hasNext()){
		 {
		 Name sin = (Name)interfaces.next();
		 cls.interfaces.add(sin.toString());
		 }
		 
		 }
		addMethods(cls,object);
		TypeDeclaration[] innerTypes = object.getTypes();
		for (int i = 0; i < innerTypes.length; i++) {
			JClass innerCls = new JClass();
			setClassInformation(innerCls, innerTypes[i]);
			cls.innerClasses.add(innerCls);
		}

	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private void addMethods(JClass cls, TypeDeclaration object) {
		MethodDeclaration[] met = object.getMethods();
		for (int i = 0; i < met.length; i++) {
			MethodDeclaration dec = met[i];
			JMethod method = new JMethod();
			method.methodName = dec.getName().toString();
			method.returnType = dec.getReturnType().toString();
			Block d = dec.getBody();
			if (d == null)
				continue;
			method.codeBlock =  d.toString();
			List param = dec.parameters();
			ListIterator paramList = param.listIterator();
			while (paramList.hasNext()) {
				SingleVariableDeclaration sin = (SingleVariableDeclaration) paramList
						.next();
				sin.getType().toString();
			}
			
			cls.methodDeclarations.add(method);
		}
	}

public class JClass {
	String className = null;
	@SuppressWarnings("rawtypes")
	ArrayList methodDeclarations = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList innerClasses = new ArrayList();
	String superClass = null;
	@SuppressWarnings("rawtypes")
	ArrayList interfaces = new ArrayList();
}

public class JMethod{
	String methodName = null;
	@SuppressWarnings("rawtypes")
	ArrayList parameters = new ArrayList();
	String codeBlock = null;
	String returnType = null;
}
}
