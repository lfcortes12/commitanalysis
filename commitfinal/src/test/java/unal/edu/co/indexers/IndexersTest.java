package unal.edu.co.indexers;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * Test class for generating term/document matrices using various methods.
 * 
 * @author Sujit Pal
 * @version $Revision: 55 $
 */
@SuppressWarnings("unused")
public class IndexersTest {

	
	private VectorGenerator vectorGenerator;
	private Map<String, Reader> documents;
	private final File COMMIT_INDEX_DIR = new File(
			"/home/fernando/Desarrollo/indexDataCommit");
	private final File COMMIT_DIFF_INDEX_DIR = new File(
			"/home/fernando/Desarrollo/indexData");
	private SimpleFSDirectory directoryCommitIndex;
	private SimpleFSDirectory directoryCommitDiffIndex;
	private IndexReader commitIndexReader;
	private IndexReader commitDiffIndexReader;

	/*@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		directoryCommitIndex = new SimpleFSDirectory(COMMIT_INDEX_DIR);
		directoryCommitDiffIndex = new SimpleFSDirectory(COMMIT_DIFF_INDEX_DIR);
		commitIndexReader = IndexReader.open(directoryCommitIndex);
		commitDiffIndexReader = IndexReader.open(directoryCommitDiffIndex);

		vectorGenerator = new VectorGenerator();
		vectorGenerator.setDataSource(new DriverManagerDataSource(
				"com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/tmdb",
				"root", "root"));
		documents = new LinkedHashMap<String, Reader>();
		for (int i = 2; i < 5; i++) {
			Document docCommit = commitIndexReader.document(i);
			String message = docCommit.get("message");

			Document docDiff = commitDiffIndexReader.document(i);
			String diff = docDiff.get("diffwords");

			if (message != null && !"".equals(message) && diff != null
					&& !"".equals(diff)) {
				documents.put("C" + i, new StringReader(message));
				documents.put("C" + i + "-DIFF", new StringReader(diff));
			}
		}

	}*/

	/*
	 * @Test public void testVectorGeneration() throws Exception {
	 * vectorGenerator.generateVector(documents);
	 * prettyPrintMatrix("Occurences", vectorGenerator.getMatrix(),
	 * vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new
	 * PrintWriter(System.out, true)); }
	 */

	/*
	 * @Test public void testTfIndexer() throws Exception {
	 * vectorGenerator.generateVector(documents); TfIndexer indexer = new
	 * TfIndexer(); RealMatrix tfMatrix =
	 * indexer.transform(vectorGenerator.getMatrix());
	 * prettyPrintMatrix("Term Frequency",
	 * tfMatrix,vectorGenerator.getDocumentNames(),
	 * vectorGenerator.getWords(),new PrintWriter(System.out, true)); }
	 */

	/*
	 * @Test public void testIdfIndexer() throws Exception {
	 * vectorGenerator.generateVector(documents); IdfIndexer indexer = new
	 * IdfIndexer(); RealMatrix idfMatrix =
	 * indexer.transform(vectorGenerator.getMatrix());
	 * prettyPrintMatrix("Inverse Document Frequency", idfMatrix,
	 * vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new
	 * PrintWriter(System.out, true)); }
	 */

	/*@Test
	public void testLsiIndexer() throws Exception {
		vectorGenerator.generateVector(documents);
		LsiIndexer indexer = new LsiIndexer();
		RealMatrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());
		prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix,vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),new PrintWriter(System.out, true));
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(lsiMatrix);
		prettyPrintMatrix("Cosine Similarity (LSI)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(System.out,true));
	}*/

	/*@SuppressWarnings("unused")
	@Test
	public void testCosineSimilarityWithTfIdfVector() throws Exception {
		vectorGenerator.generateVector(documents);
		IdfIndexer indexer = new IdfIndexer();
		RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);
		prettyPrintMatrix("Cosine Similarity (TF/IDF)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(System.out,true));
		PearsonsCorrelation correlation = new PearsonsCorrelation(similarity);
		prettyPrintMatrix("PearsonsCorrelation", termDocMatrix,vectorGenerator.getDocumentNames(), new PrintWriter(System.out,true));
	}*/

	private void prettyPrintMatrix(String legend, RealMatrix matrix,String[] documentNames, String[] words, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf("%15s", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%8s", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < words.length; i++) {
			writer.printf("%15s", words[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%8.4f", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}

	private void prettyPrintMatrix(String legend, RealMatrix matrix,String[] documentNames, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf("%6s", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%s;", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%s;", documentNames[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%f;", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}
}
