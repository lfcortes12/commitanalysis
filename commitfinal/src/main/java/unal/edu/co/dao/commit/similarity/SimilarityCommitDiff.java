package unal.edu.co.dao.commit.similarity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.commons.math.linear.SparseRealVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.SimpleFSDirectory;


public class SimilarityCommitDiff {
	private final File COMMIT_INDEX_DIR = new File("/home/fernando/Desarrollo/indexDataCommit");
	private final File COMMIT_DIFF_INDEX_DIR = new File("/home/fernando/Desarrollo/indexData");
	private SimpleFSDirectory directoryCommitIndex;
	private SimpleFSDirectory directoryCommitDiffIndex;
	//private IndexSearcher commitIndexSearcher;
	//private IndexSearcher commitDiffIndexSearcher;
	private IndexReader commitIndexReader;
	private IndexReader commitDiffIndexReader ;
	
	public SimilarityCommitDiff() {
		
		try {
			directoryCommitIndex = new SimpleFSDirectory(COMMIT_INDEX_DIR);
			directoryCommitDiffIndex = new SimpleFSDirectory(COMMIT_DIFF_INDEX_DIR);
			commitIndexReader = IndexReader.open(directoryCommitIndex);
			commitDiffIndexReader = IndexReader.open(directoryCommitDiffIndex);
		    //commitIndexSearcher = new IndexSearcher(commitIndexReader);
		    //commitDiffIndexSearcher = new IndexSearcher(commitDiffIndexReader);
		   
			//CommitAnalyzer commitAnalyzer = new CommitAnalyzer();
			//IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,commitAnalyzer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double getCosineSimilarity(DocVector d1, DocVector d2) {
		//return (d1.vector.dotProduct(d2.vector.getSubVector(0, d1.vector.getDimension()))) / (d1.vector.getNorm() * d2.vector.getSubVector(0, d1.vector.getDimension()).getNorm());
		return (d2.vector.dotProduct(d1.vector)) / (d1.vector.getNorm() * d2.vector.getNorm());
	}
	
	public void searchDataIndex(String changeset) {
		try {
			Map<String,Integer> termsCommit = new HashMap<String,Integer>();
			Map<String,Integer> termsDiff = new HashMap<String,Integer>();
			
			TermEnum termEnum = commitIndexReader.terms(new Term("message"));
		    int pos = 0;
		    while (termEnum.next()) {
		      Term term = termEnum.term();
		      if (! "message".equals(term.field())) { 
		        break;
		      }
		      termsCommit.put(term.text(), pos++);
		      System.out.println("1 - termino: " + term.text() +  " - posicion: " + pos);
		    }
		    
		    TermEnum diffTermEnum = commitDiffIndexReader.terms(new Term("diffwords"));
		    int posDiff = 0;
		    while (diffTermEnum.next()) {
		      Term term = diffTermEnum.term();
		      if (! "diffwords".equals(term.field())) { 
		        break;
		      }
		      termsDiff.put(term.text(), posDiff++);
		      System.out.println("2 - termino: " + term.text() +  " - posicion: " + posDiff);
		    }
			
			TermFreqVector[] tfvs = commitIndexReader.getTermFreqVectors(100);
			Assert.assertTrue(tfvs.length == 1);
			
			TermFreqVector[] tfvscd = commitDiffIndexReader.getTermFreqVectors(100);
			Assert.assertTrue(tfvscd.length == 1);
			int sizeVectors = 0;
			if(termsCommit.size() < termsDiff.size()) {
				sizeVectors = termsDiff.size();
			} else {
				sizeVectors = termsCommit.size();
			}
			
			
			DocVector[] docs = new DocVector[2];
			docs[0] = new DocVector(termsCommit,sizeVectors);
			for (TermFreqVector tfv : tfvs) {
				String[] termTexts = tfv.getTerms();
				int[] termFreqs = tfv.getTermFrequencies();
				Assert.assertEquals(termTexts.length, termFreqs.length);
				for (int j = 0; j < termTexts.length; j++) {
					docs[0].setEntry(termTexts[j], termFreqs[j]);
				}
			}
			docs[0].normalize();
			docs[1] = new DocVector(termsDiff,sizeVectors);
			for (TermFreqVector tfv : tfvscd) {
				String[] termTexts = tfv.getTerms();
				int[] termFreqs = tfv.getTermFrequencies();
				Assert.assertEquals(termTexts.length, termFreqs.length);
				for (int j = 0; j < termTexts.length; j++) {
					docs[1].setEntry(termTexts[j], termFreqs[j]);
				}
			}
			/*if(termsCommit.size() < termsDiff.size()) {
				for(int i = docs[0].vector.getDimension(); i < docs[1].vector.getDimension(); i++) {
					docs[0].setEntry("", 0);
				}
			} else {
				for(int i = docs[1].vector.getDimension(); i < docs[0].vector.getDimension(); i++) {
					docs[1].setEntry("", 0);
				}
			}*/
			
			docs[1].normalize();
			// now get similarity between doc[0] and doc[1]
		    double cosim01 = getCosineSimilarity(docs[0], docs[1]);
		    System.out.println("cosim(0,1)=" + cosim01);
			closeReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*private double getCosineSimilarity(DocVector d1, DocVector d2) {
		return (d1.vector.dotProduct(d2.vector)) / (d1.vector.getNorm() * d2.vector.getNorm());
	}*/
	
	public void closeReader() {
		try {
			commitIndexReader.close();
			commitDiffIndexReader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class DocVector {
	    public Map<String,Integer> terms;
	    public SparseRealVector vector;
	    
		public DocVector(Map<String,Integer> terms,int size) {
	      this.terms = terms;
	      this.vector = new OpenMapRealVector(size);
	    }
	    
	    public void setEntry(String term, int freq) {
	      if (terms.containsKey(term)) {
	        int pos = terms.get(term);
	        vector.setEntry(pos, (double) freq);
	      }
	    }
	    
	    public void normalize() {
	      double sum = vector.getL1Norm();
	      vector = (SparseRealVector) vector.mapDivide(sum);
	    }
	    
	    public String toString() {
	      RealVectorFormat formatter = new RealVectorFormat();
	      return formatter.format(vector);
	    }
	  }

}
