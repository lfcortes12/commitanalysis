package com.infosys.lucene.code;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import unal.edu.co.entities.CommitDiff;

import com.infosys.lucene.code.JavaParser.JClass;
import com.infosys.lucene.code.JavaParser.JMethod;

public class JavaSourceCodeIndexer {

	private static final String IMPLEMENTS = "implements";
	private static final String IMPORT = "import";
	private static final String CLASS = "class";
	private static final String METHOD = "method";
	private static final String CODE = "code";
	private static final String COMMENT = "comment";
	private static final String RETURN = "return";
	private static final String PARAMETER = "parameter";
	private static final String EXTENDS = "extends";
	private static IndexWriter writer;
	private final File indexDir = new File("/home/fernando/Desarrollo/indexData");

	public JavaSourceCodeIndexer() {
		try {
			SimpleFSDirectory indexDirectory;
			indexDirectory = new SimpleFSDirectory(indexDir);
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35,new JavaSourceCodeAnalyzer());
			writer = new IndexWriter(indexDirectory,conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void indexCommitDiff(List<CommitDiff> commitDiffs) {
		try {
			for (CommitDiff commitDiff : commitDiffs) {
				Document doc = new Document();
			    doc.add(new Field("id", commitDiff.getChangeset(), Field.Store.YES, Field.Index.ANALYZED));
			    doc.add(new Field("diff",new String(commitDiff.getDiff()), Field.Store.YES, Field.Index.ANALYZED));  
			    indexFile(writer, new String(commitDiff.getDiff()));
			    writer.addDocument(doc);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void indexDirectory(IndexWriter writer, File dir)
			throws IOException {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory())
				indexDirectory(writer, f);
			else if (f.getName().endsWith(".java"))
				indexFile(writer, f);
		}
	}*/

	public static void indexFile(IndexWriter writer, String f) {
		//if (f.isHidden() || !f.exists() || !f.canRead())
		//	return;
		Document doc = new Document();
		JavaParser parser = new JavaParser();
		parser.setSource(f);
		addImportDeclarations(doc, parser);
		addComments(doc, parser);
		JClass cls = parser.getDeclaredClass();
		addClass(doc, cls);
		//doc.add(Field.UnIndexed("filename", f.getName()));
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private static void addImportDeclarations(Document doc, JavaParser parser) {
		ArrayList imports = parser.getImportDeclarations();
		if (imports == null)
			return;
		for (int i = 0; i < imports.size(); i++) {
			String importName = (String) imports.get(i);
			doc.add(new Field(IMPORT, importName, Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	@SuppressWarnings("rawtypes")
	private static void addComments(Document doc, JavaParser parser) {
		ArrayList comments = parser.getComments();
		if (comments == null)
			return;
		for (int i = 0; i < comments.size(); i++) {
			String docComment = (String) comments.get(i);
			doc.add(new Field(COMMENT, docComment, Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	@SuppressWarnings("rawtypes")
	private static void addClass(Document doc, JClass cls) {
		doc.add(new Field(CLASS, cls.className, Field.Store.YES, Field.Index.ANALYZED));
		
		String superCls = cls.superClass;
		if (superCls != null) {
			doc.add(new Field(EXTENDS, superCls, Field.Store.YES, Field.Index.ANALYZED));
			
		}
		ArrayList interfaces = cls.interfaces;
		for (int i = 0; i < interfaces.size(); i++) {
			String interfaceName = (String) interfaces.get(i);
			doc.add(new Field(IMPLEMENTS, interfaceName, Field.Store.YES, Field.Index.ANALYZED));
		}

		addMethods(cls, doc);
		ArrayList innerCls = cls.innerClasses;
		for (int i = 0; i < innerCls.size(); i++) {
			addClass(doc, (JClass) innerCls.get(i));
		}

	}

	@SuppressWarnings("rawtypes")
	private static void addMethods(JClass cls, Document doc) {
		ArrayList methods = cls.methodDeclarations;
		for (int i = 0; i < methods.size(); i++) {
			JMethod method = (JMethod) methods.get(i);
			doc.add(new Field(METHOD, method.methodName, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(RETURN, method.returnType, Field.Store.YES, Field.Index.ANALYZED));
			ArrayList params = method.parameters;
			for (int k = 0; k < params.size(); k++) {
				String paramType = (String) params.get(k);
				doc.add(new Field(PARAMETER, paramType, Field.Store.YES, Field.Index.ANALYZED));
			}
			String code = method.codeBlock;
			if (code != null) {
				doc.add(new Field(CODE, code, Field.Store.YES, Field.Index.ANALYZED));
			}

		}
	}

}
