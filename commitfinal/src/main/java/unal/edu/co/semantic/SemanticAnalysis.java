package unal.edu.co.semantic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.SimpleFSDirectory;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.vector.DoubleVector;

public class SemanticAnalysis {
	private final File COMMIT_INDEX_DIR = new File("/home/fernando/Desarrollo/indexDataCommit");
	private final File COMMIT_DIFF_INDEX_DIR = new File("/home/fernando/Desarrollo/indexData");
	private SimpleFSDirectory directoryCommitIndex;
	private SimpleFSDirectory directoryCommitDiffIndex;
	private IndexReader commitIndexReader;
	private IndexReader commitDiffIndexReader ;
	private LatentSemanticAnalysis lsa2;
	private LatentSemanticAnalysis lsaDiff2;
	
	public SemanticAnalysis() {
		try {
			directoryCommitIndex = new SimpleFSDirectory(COMMIT_INDEX_DIR);
			directoryCommitDiffIndex = new SimpleFSDirectory(COMMIT_DIFF_INDEX_DIR);
			commitIndexReader = IndexReader.open(directoryCommitIndex);
			commitDiffIndexReader = IndexReader.open(directoryCommitDiffIndex);
			List<BufferedReader> documentsCommit = new ArrayList<BufferedReader>();
			List<BufferedReader> documentsDiff = new ArrayList<BufferedReader>();
			for(int i = 1; i < 200; i++) {
				Document docCommit = commitIndexReader.document(i);
				String message = docCommit.get("message");
				String changesetCommit = docCommit.get("changeset");
				
				Document docDiff = commitDiffIndexReader.document(i);
				String diff = docDiff.get("diffwords");
				String changesetDiff = docCommit.get("changeset");
				
				if(changesetCommit.equals(changesetDiff)) {
					System.out.println("" + i);
				}
				
				if(message != null && !"".equals(message) && diff != null && !"".equals(diff)) {
					BufferedReader bufferedReader = new BufferedReader(new StringReader(message));
					documentsCommit.add(bufferedReader);
					
					BufferedReader bufferedReaderDiff = new BufferedReader(new StringReader(diff));
					documentsDiff.add(bufferedReaderDiff);
				} else {
					System.out.println("commit o diff vació");
				}
				
			}
			semanticAnalysisMessage(documentsCommit);
			semanticAnalysisDiff(documentsDiff);
			int limit = 0;
			if(lsa2.documentSpaceSize() < lsaDiff2.documentSpaceSize()) {
				limit = lsa2.documentSpaceSize();
			} else {
				limit = lsaDiff2.documentSpaceSize();
			}
			for(int i = 0; i < limit; i++) {
				System.out.println(measure_similarity_document(lsa2.getDocumentVector(i),lsaDiff2.getDocumentVector(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public void semanticAnalysisMessage(Collection<BufferedReader> documents) {
	    // create the Algorithm, which should initialize any of its
	    // required resources
		try {
			lsa2 = new LatentSemanticAnalysis(true, 164, new LogEntropyTransform(),
		             SVD.getFastestAvailableFactorization(), false, new StringBasisMapping());
			// Process each of the documents in the corpus.  The algorithm
		    // should incrementally build its semantic space
			for (BufferedReader document : documents) {
				lsa2.processDocument(document);
			}
		    // The algorithm needs to do any post-processing once the
		    // entire corpus has been seen.  The Properties argument
		    // allows us to specify any optional configuration/processing
		    // parameters that the algorithm defines.
			lsa2.processSpace(System.getProperties());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void semanticAnalysisDiff(Collection<BufferedReader> documents) {
	    // create the Algorithm, which should initialize any of its
	    // required resources
		try {
			lsaDiff2 = new LatentSemanticAnalysis(true, 164, new LogEntropyTransform(),
		             SVD.getFastestAvailableFactorization(), false, new StringBasisMapping());
			// Process each of the documents in the corpus.  The algorithm
		    // should incrementally build its semantic space
		    for (BufferedReader document : documents) {
		    	lsaDiff2.processDocument(document);
		    }

		    // The algorithm needs to do any post-processing once the
		    // entire corpus has been seen.  The Properties argument
		    // allows us to specify any optional configuration/processing
		    // parameters that the algorithm defines.
			lsaDiff2.processSpace(System.getProperties());
			lsaDiff2.getDocumentVector(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	double measure_similarity_document(DoubleVector messageVector, DoubleVector diffVector) {
		
		double sum = 0.0;
		sum = Similarity.getSimilarity(Similarity.SimType.COSINE, messageVector, diffVector);

		return sum;
	}

}

