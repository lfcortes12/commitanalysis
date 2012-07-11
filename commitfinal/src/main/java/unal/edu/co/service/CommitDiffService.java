package unal.edu.co.service;

import java.util.List;

import unal.edu.co.dao.commit.CommitDiffDao;
import unal.edu.co.entities.CommitDiff;
/**
 * @author Fernando Cortes
 *
 */
public class CommitDiffService {
	
	private CommitDiffDao dao;
	private static CommitDiffService instance;
	
	public CommitDiffService() {
		super();
		dao = new CommitDiffDao();
	}

	public static CommitDiffService getInstance() {
		if(instance == null) {
			instance = new CommitDiffService();
		} 
		return instance;
	}
	
	public CommitDiff read(String changeset) {
		dao = new CommitDiffDao();
		return (CommitDiff) dao.read(changeset);
	}
	
	public String findByChangeSet(String ChangeSet) {
		dao = new CommitDiffDao();
		return (String) dao.findByChangeSet(ChangeSet);
	}
	
	public CommitDiff findByChangeSetValue(String ChangeSet) {
		dao = new CommitDiffDao();
		return dao.findByChangeSetValue(ChangeSet);
	}
	
	public List<CommitDiff> findByChangeSetPaging(int page,int pageSize) {
		return dao.findByChangeSetPaging(page, pageSize);
	}
	
}
