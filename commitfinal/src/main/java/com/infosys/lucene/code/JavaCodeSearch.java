package com.infosys.lucene.code;


import java.io.File;

import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;


public class JavaCodeSearch  {

    /** Main for running test case by itself. */
    @SuppressWarnings("deprecation")
	public static void main(String args[]) {
		try {
			File indexDir = new File("C:\\Lucene\\index");
			String q = "code:JTextField +code:setEditable";
			PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
					new JavaSourceCodeAnalyzer());
			analyzer.addAnalyzer("import", new KeywordAnalyzer());
			Directory fsDir;
			fsDir = new SimpleFSDirectory(indexDir);
			IndexSearcher is = new IndexSearcher(fsDir);
			Query query = new QueryParser(Version.LUCENE_35, "code", analyzer).parse(q);
			long start = System.currentTimeMillis();
			int hitsPerPage = 10;
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			is.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			long end = System.currentTimeMillis();

			System.err.println("Found " + hits.length + " docs in " + (end - start) + " millisec");
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document doc = is.doc(docId);
				System.out.println(doc.get("filename") + " with a score of " + hits[i].score);
			}
			is.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
