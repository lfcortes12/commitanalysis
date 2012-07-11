package unal.edu.co.service;

import java.util.List;

import unal.edu.co.dao.commit.ConcreteDiffDao;
import unal.edu.co.entities.ConcreteDiff;
/**
 * @author Fernando Cortes
 *
 */
public class ConcreteDiffService {
	
	private ConcreteDiffDao dao;
	private static ConcreteDiffService instance;
	
	public ConcreteDiffService() {
		super();
	}

	public static ConcreteDiffService getInstance() {
		if(instance == null) {
			instance = new ConcreteDiffService();
		} 
		return instance;
	}
	
	public ConcreteDiff read(String changeset) {
		dao = new ConcreteDiffDao();
		return (ConcreteDiff) dao.read(changeset);
	}
	
	/*public String findByChangeSet(String ChangeSet) {
		dao = new ConcreteDiffDao();
		return (String) dao.findByChangeSet(ChangeSet);
	}
	
	public CommitDiff findByChangeSetValue(String ChangeSet) {
		dao = new CommitDiffDao();
		return dao.findByChangeSetValue(ChangeSet);
	}
	
	public List<CommitDiff> findByChangeSetPaging(int page,int pageSize) {
		return dao.findByChangeSetPaging(page, pageSize);
	}*/
	
	public List<ConcreteDiff> findByChangeSetPaging(int page,int pageSize) {
		dao = new ConcreteDiffDao();
		return dao.findByChangeSetPaging(page, pageSize);
	}
	
	public ConcreteDiff save(ConcreteDiff concreteDiff) {
		dao = new ConcreteDiffDao();
		dao.create(concreteDiff);
		return concreteDiff;
	}
	
}
