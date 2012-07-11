package unal.edu.co.service;

import java.util.List;

import unal.edu.co.dao.commit.CommitDao;
import unal.edu.co.entities.Commit;
/**
 * @author Fernando Cortes
 *
 */
public class CommitService {
	
	private CommitDao dao;
	private static CommitService instance;
	
	public CommitService() {
		super();
	}

	public static CommitService getInstance() {
		if(instance == null) {
			instance = new CommitService();
		} 
		return instance;
	}
	
	public Commit read(String changeset) {
		dao = new CommitDao();
		return (Commit) dao.read(changeset);
	}
	
	public String findByChangeSet(String ChangeSet) {
		dao = new CommitDao();
		return (String) dao.findByChangeSet(ChangeSet);
	}
	
	public List<Commit> findByChangeSetPaging(int page,int pageSize) {
		dao = new CommitDao();
		List<Commit> commits = dao.findByChangeSetPaging(page, pageSize);
		return commits;
	}

}
