/**
 * 
 */
package unal.edu.co.persistence;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import unal.edu.co.dao.commit.Index.IndexCommit;
import unal.edu.co.dao.commit.Index.IndexDiff;
import unal.edu.co.indexers.VectorGenerator;
import unal.edu.co.service.CommitDiffService;
import unal.edu.co.service.CommitService;
import unal.edu.co.service.ConcreteDiffService;

/**
 * @author Fernando Cortes
 *
 */
public class CommitDiffTest {
	
	
	static CommitDiffService service;
	static ConcreteDiffService serviceConcreteCommitDiff;
	static CommitService commitService;
	private VectorGenerator vectorGenerator;
	private Map<String, Reader> documents = new LinkedHashMap<String, Reader>();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		vectorGenerator = new VectorGenerator();
		vectorGenerator.setDataSource(new DriverManagerDataSource(
				"com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/tmdb",
				"root", "root"));
		documents = new LinkedHashMap<String, Reader>();
		IndexDiff indexDiff = new IndexDiff();
		IndexCommit indexCommit = new IndexCommit();
		for(int i = 1; i < indexDiff.getMaxDocIndex() / 8; i++) {
			String changeset = "";
			try {
				Document commit = indexCommit.getCommit(i);
				changeset =  commit.get("changeset");
				Document diff = indexDiff.searchDiff(changeset);
				if(commit != null && diff != null) {
					System.out.println("procesando commit: " + i + " changeset: " + changeset);
					String message = commit.get("message");
					changeset =  commit.get("changeset");
					String diffText = diff.get("diffwords");
					if (message != null && !"".equals(message) && diffText != null && !"".equals(diffText)) {
						documents.put("C" + i, new StringReader(message));
						documents.put("C" + i + "-DIFF", new StringReader(diffText));
					}
				}
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		indexDiff.closeWriter();
		indexCommit.closeWriter();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	 //SE APLICÓ CORRECTAMENTE A TODOS LOS DIFFS PARA PROCESARLOS
	public void testCleanDiff() throws IOException {
		service = CommitDiffService.getInstance();
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		int i = 1;
		//187 - 805 finaliza en 2192 82592
		int j = 1;
		for (i = 1079; i <= 1095; i++ ) {
			System.out.println("va a buscar los diffs de la pagina " + i);
			List<CommitDiff> searched = service.findByChangeSetPaging(i, 100);
			System.out.println("ya consulto los diffs de la página " + i);
			for (CommitDiff commitDiff : searched) {
				System.out.println("procesando los diffs de la página = " + i + " DIFF Nro: " + j);
				if(commitDiff.getDiff() != null) {
					System.out.println("antes de tokenizar de diff " + j);
					String proccessedDiff = Tokenizer.diffProccessing(new String(commitDiff.getDiff()));
					System.out.println("termino de procesar el diff " + j);
					if(proccessedDiff != null && !"".equals(proccessedDiff)) {
						commitDiff.setProcessedDiff(proccessedDiff.getBytes());
						service.update(commitDiff);
					} 
				}
				j++;
			}
		}
		Assert.assertEquals(i, 1096);
	}*/
	/*
	//CORRIO PARA INDEXAR LA TOTALIDAD DE LOS DIFFS PROCESADOS
	@Test
	public void testProcessAllCommitDiff() {
		service = CommitDiffService.getInstance();
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		IndexDiff indexConcreteDiff = new IndexDiff();
		int i = 0;
		//indexConcreteDiff.cleanIndex();		
		int j = 1;
		//1095
		for (i = 901; i <= 1095; i++ ) {
			System.out.println("va a buscar los diffs de la pagina " + i);
			List<CommitDiff> searched = service.findByChangeSetPaging(i, 100);
			System.out.println("ya consulto los diffs de la página " + i);
			for (CommitDiff commitDiff : searched) {
				System.out.println("procesando los diffs de la página = " + i + " DIFF Nro: " + j);
				System.out.println("va a procesar el diff " + j);
				if(commitDiff.getProcessedDiff() != null) {
					indexConcreteDiff.finalIndexDiff(commitDiff.getChangeset(), new String(commitDiff.getProcessedDiff()), new ArrayList<String>(),commitDiff.getProjectName().trim().toLowerCase());
				} else {
					indexConcreteDiff.finalIndexDiff(commitDiff.getChangeset(), "empty", new ArrayList<String>(),commitDiff.getProjectName().trim().toLowerCase());
				}
				j++;
			}
		}
		indexConcreteDiff.closeWriter();
		Assert.assertEquals(i, 1096);
	}
	*/
	/*@Test
	public void testProcessAllCommits() {
		commitService = CommitService.getInstance();
		IndexCommit indexCommit = new IndexCommit();
		int i = 1;
		//187 - 805 finaliza en 2192
		int j = 1;
		for (i = 1; i <= 2192; i++ ) {
			if(i == 1) {
				indexCommit.cleanIndex();
			}
			System.out.println("va a buscar los commits de la pagina " + i);
			List<Commit> searched = commitService.findByChangeSetPaging(i, 50);
			System.out.println("ya consulto los commits de la página " + i);
			for (Commit commit : searched) {
				System.out.println("va a indexar el commit " + j);
				if(commit.getDescription() != null && !"".equals(commit.getDescription())) {
					indexCommit.indexCommitData(commit.getChangeset(), commit.getDescription().toLowerCase(), commit.getAuthor(), commit.getProjectName().trim().toLowerCase());
				} else {
					indexCommit.indexCommitData(commit.getChangeset(), "empty", commit.getAuthor(), commit.getProjectName().trim().toLowerCase());
				}
				j++;
			}
		}
		indexCommit.closeWriter();
		Assert.assertEquals(i, 2193);
	}*/
	
	/*@Test
	public void testSearchOnIndex() {
		IndexDiff indexDiff = new IndexDiff();
		IndexCommit indexCommit = new IndexCommit();
		
		for(int i = 1; i < 5; i++) {
			String changeset = "";
			try {
				Document commit = indexCommit.getCommit(i);
				String message = commit.get("message");
				changeset =  commit.get("changeset");
				Document diff = indexDiff.searchDiff(changeset);
				String diffText = diff.get("diffwords");
				if (message != null && !"".equals(message) && diffText != null
						&& !"".equals(diffText)) {
					documents.put("C" + i, new StringReader(message));
					documents.put("C" + i + "-DIFF", new StringReader(diffText));
				}
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
		
	}*/
	
	/*@Test
	public void testLsiIndexer() throws Exception {
		vectorGenerator.generateVector(documents);
		LsiIndexer indexer = new LsiIndexer();
		RealMatrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());
		File file = new File("/home/fernando/Desarrollo/resultados/lsi/lsi.txt");
		prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix,vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),new PrintWriter(file));
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(lsiMatrix);
		File cosineFile = new File("/home/fernando/Desarrollo/resultados/lsi/cosinesimilarity.txt");
		prettyPrintMatrix("Cosine Similarity (LSI)", similarity,vectorGenerator.getDocumentNames(), new PrintWriter(cosineFile));
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

	@SuppressWarnings("unused")
	private void prettyPrintMatrix(String legend, RealMatrix matrix,String[] documentNames, String[] words, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf(";", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%s;", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < words.length; i++) {
			writer.printf("%1s;", words[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%f;", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}

	@SuppressWarnings("unused")
	private void prettyPrintMatrix(String legend, RealMatrix matrix,String[] documentNames, PrintWriter writer) {
		writer.printf("=== %s ===%n", legend);
		writer.printf("%s;", " ");
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
