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

import unal.edu.co.entities.Commit;
import unal.edu.co.service.CommitService;

/**
 * @author fernando
 *
 */
public class CommitTest {
	
	
	static CommitService service;

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

	@Test
	public void test() {
		service = CommitService.getInstance();
		String searched = service.findByChangeSet("00005ddfe1ef1b50aa9b7f9d70f04e4bd1034738");
		Assert.assertEquals(true, "00005ddfe1ef1b50aa9b7f9d70f04e4bd1034738".equals(searched));
	}
	
	@Test
	public void testPaging() {
		service = CommitService.getInstance();
		List<Commit> searched = service.findByChangeSetPaging(1, 10);
		for (Commit commit : searched) {
			System.out.println(commit.getChangeset());
		}
		Assert.assertEquals(10, searched.size());
	}




}
