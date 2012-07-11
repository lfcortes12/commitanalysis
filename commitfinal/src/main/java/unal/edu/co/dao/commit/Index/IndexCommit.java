package unal.edu.co.dao.commit.Index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import unal.edu.co.dao.commit.analyzer.CommitAnalyzer;


public class IndexCommit {
	final File INDEX_DIR = new File("/home/fernando/Desarrollo/indexDataCommit");
	
	private IndexWriter writer;
	
	public IndexCommit() {
		SimpleFSDirectory directory;
		try {
			if(!INDEX_DIR.exists()) {
				INDEX_DIR.mkdir();
			}
			directory = new SimpleFSDirectory(INDEX_DIR);
			CommitAnalyzer commitAnalyzer = new CommitAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,commitAnalyzer);
			conf.setOpenMode(OpenMode.APPEND);
			writer = new IndexWriter(directory, conf);
			//cleanIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanIndex() {
		if(writer != null) {
			try {
				writer.deleteAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void indexCommitData(String changeset,String message,String author,String projectName) {
		try {
			Document doc = new Document();
		    doc.add(new Field("changeset", changeset, Field.Store.YES, Field.Index.NOT_ANALYZED));
		    doc.add(new Field("message", message, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
		    doc.add(new Field("author", author, Field.Store.YES, Field.Index.NOT_ANALYZED ));
		    doc.add(new Field("project", projectName, Field.Store.YES, Field.Index.NOT_ANALYZED ));
		    writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeWriter() {
		try {
			writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
