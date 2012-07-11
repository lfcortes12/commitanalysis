/**
 * 
 */
package unal.edu.co.persistence;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import unal.edu.co.dao.commit.Index.IndexCommit;
import unal.edu.co.dao.commit.Index.IndexDiff;
import unal.edu.co.dao.utils.Tokenizer;
import unal.edu.co.dao.utils.Utils;
import unal.edu.co.entities.Commit;
import unal.edu.co.entities.CommitDiff;
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
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void test() {
		service = CommitDiffService.getInstance();
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		CommitDiff searched = service.findByChangeSetValue("0011258f2872cd24b8a98c9353de42f128375bd3");
		String proccessedDiff = Tokenizer.getTokens(Utils.getAddedLines(new String(searched.getDiff())));
		System.out.println("0011258f2872cd24b8a98c9353de42f128375bd3: " + proccessedDiff);
		for(String packageString : Tokenizer.packagesImported) {
			System.out.println("packagesImported: " + packageString);
		}
		Assert.assertEquals(true, "0011258f2872cd24b8a98c9353de42f128375bd3".equals(searched.getChangeset()));
	}*/
	
	@Test
	public void testProcessAllCommitDiff() {
		service = CommitDiffService.getInstance();
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		IndexDiff indexConcreteDiff = new IndexDiff();
		int i = 187;
		//187 - 805 finaliza en 2192
		// desde i=1; i <= 500
		int j = 1;
		for (i = 801; i <= 1100; i++ ) {
			if(i == 1) {
				indexConcreteDiff.cleanIndex();
			}
			System.out.println("va a buscar los diffs de la pagina " + i);
			List<CommitDiff> searched = service.findByChangeSetPaging(i, 50);
			System.out.println("ya consulto los diffs de la página " + i);
			for (CommitDiff commitDiff : searched) {
				System.out.println("procesando los diffs de la página = " + i + " DIFF Nro: " + j);
				System.out.println("va a procesar el diff " + j);
				if(commitDiff.getDiff() != null) {
					String proccessedDiff = Tokenizer.getTokens(Utils.getAddedLines(new String(commitDiff.getDiff())));
					System.out.println("termino de procesar el diff " + j);
					if(proccessedDiff != null && "".equals(proccessedDiff)) {
						indexConcreteDiff.finalIndexDiff(commitDiff.getChangeset(), proccessedDiff, Tokenizer.packagesImported,commitDiff.getProjectName().trim().toLowerCase());
					}
				}
				j++;
			}
		}
		indexConcreteDiff.closeWriter();
		Assert.assertEquals(i, 801);
	}
	
	
	 
	@Test
	public void testProcessAllCommits() {
		commitService = CommitService.getInstance();
		IndexCommit indexCommit = new IndexCommit();
		int i = 1;
		//187 - 805 finaliza en 2192
		// desde i=1; i <= 500
		int j = 1;
		for (i = 801; i <= 1100; i++ ) {
			if(i == 1) {
				indexCommit.cleanIndex();
			}
			System.out.println("va a buscar los commits de la pagina " + i);
			List<Commit> searched = commitService.findByChangeSetPaging(i, 50);
			System.out.println("ya consulto los commits de la página " + i);
			for (Commit commit : searched) {
				System.out.println("va a indexar el commit " + j);
				if(commit.getDescription() != null && "".equals(commit.getDescription())) {
					indexCommit.indexCommitData(commit.getChangeset(), commit.getDescription().toLowerCase(), commit.getAuthor(), commit.getProjectName().trim().toLowerCase());
				}
				j++;
			}
		}
		indexCommit.closeWriter();
		Assert.assertEquals(i, 801);
	}
	
	/*@Test 
	public void testSimilarity() {
		SimilarityCommitDiff similarityCommitDiff = new SimilarityCommitDiff();
		similarityCommitDiff.searchDataIndex("");
	}*/
	
	/*@Test 
	public void testLSI() {
		SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
	}*/
	
	/*@Test
	public void testLoadConcreteDiff() {
		serviceConcreteCommitDiff = ConcreteDiffService.getInstance();
		int i = 1;
		indexConcreteDiff indexConcreteDiff = new IndexConcreteDiff();
		for (i = 1; i <= 5; i++ ) {
			System.out.println("para i = " + i);
			List<ConcreteDiff> searched = serviceConcreteCommitDiff.findByChangeSetPaging(i, 50);
			indexConcreteDiff.indexDiff(searched);
		}
		indexConcreteDiff.closeWriter();
		Assert.assertEquals(i, 6);
	}*/
	
}
