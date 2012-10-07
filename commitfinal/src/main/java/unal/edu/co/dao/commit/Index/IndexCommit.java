package unal.edu.co.dao.commit.Index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import unal.edu.co.dao.commit.analyzer.CommitAnalyzer;
import unal.edu.co.dao.utils.NumeroAleatorio;


public class IndexCommit {
	final File INDEX_DIR = new File("/home/fernando/Desarrollo/indexDataCommit");
	
	private IndexWriter writer;
	private IndexReader reader;
	IndexSearcher searcher;
	
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
	
	public Document getCommit(int i) throws CorruptIndexException, IOException {
		return reader.document(i);
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
	
	public List<Document> getCommitByProjectName(String valueSearch) {
		// Build a Query object
		List<Document> resultados = new ArrayList<Document>();
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, "project", new StandardAnalyzer(Version.LUCENE_36));
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
	            //ScoreDoc []docs = hits.scoreDocs;
	            int tamanoMuestra = (int) Math.floor(hits.totalHits * 0.02);
	            int total = tamanoMuestra + 1;
	            NumeroAleatorio na = new NumeroAleatorio(1,tamanoMuestra);   
			    for(int i = 0; i < total;i++){
			    	int randomValue = na.generar();
			    	if(randomValue != -1) {
			    		Document d = searcher.doc(randomValue);
			    		if(d != null) {
			    			resultados.add(d);
			    		}
			    	}
			    }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultados;
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
