package unal.edu.co.dao.commit.Index;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import unal.edu.co.dao.commit.analyzer.DiffAnalyzer;
import unal.edu.co.entities.ConcreteDiff;


public class IndexDiff {
	final File INDEX_DIR = new File("/home/fernando/Desarrollo/indexData");
	
	private IndexWriter writer;
	private IndexReader reader;
	IndexSearcher searcher;
	
	public IndexDiff() {
		SimpleFSDirectory directory;
		try {
			if(!INDEX_DIR.exists()) {
				INDEX_DIR.mkdir();
			}
			directory = new SimpleFSDirectory(INDEX_DIR);
			DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,diffAnalyzer);
			conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
			writer = new IndexWriter(directory, conf);
			reader = IndexReader.open(directory);
			searcher = new IndexSearcher(reader);
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
	
	public void indexDiff(List<ConcreteDiff> concreteDiff) {
		
		try {
			for (ConcreteDiff concreteDiff2 : concreteDiff) {
				Document doc = new Document();
			    doc.add(new Field("id", concreteDiff2.getChangeset(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			    doc.add(new Field("diffwords",new String(concreteDiff2.getProcessedDiff()), Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS));  
			    writer.addDocument(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void finalIndexDiff(String changeset,String otherWords,List<String> packagesANDExceptions,String projectName) {
		
		try {
			
			Document doc = new Document();
		    doc.add(new Field("changeset", changeset, Field.Store.YES, Field.Index.NOT_ANALYZED));
		    doc.add(new Field("diffwords", otherWords, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));
		    doc.add(new Field("project", projectName, Field.Store.YES, Field.Index.NOT_ANALYZED ));
		    
		    for (String packageString : packagesANDExceptions) {
		    	doc.add(new Field("packageORImport", packageString, Field.Store.YES, Field.Index.NOT_ANALYZED));
			}
		    writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document searchDiff(String valueSearch) {
		// Build a Query object
		Document diffSearched = null;
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, "changeset", new StandardAnalyzer(Version.LUCENE_36));
        Query query;
		try {
			query = queryParser.parse(valueSearch);
			TopDocs hits = searcher.search(query,1);
	        // Examine the Hits object to see if there were any matches
	        int hitCount = hits.totalHits;
	        if (hitCount == 0) {
	            System.out.println("No matches were found for \"" + valueSearch + "\"");
	        }
	        else {
	          //System.out.println("Hits for \"" + valueSearch + "\" were found in quotes by:");
	            ScoreDoc []docs = hits.scoreDocs;
	            for (ScoreDoc scoreDoc : docs) {
	            	Document d = searcher.doc(scoreDoc.doc);
	            	diffSearched = d;
	                //System.out.println("project: "+d.get("project"));
	                //System.out.println("changeset: "+d.get("changeset"));
				}
	        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     // Search for the query
		return diffSearched;
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
	
	public int getMaxDocIndex() {
		return reader.maxDoc();
	}
	
	public void closeReader() {
		try {
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
