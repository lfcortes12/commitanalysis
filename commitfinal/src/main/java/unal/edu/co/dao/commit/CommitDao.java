package unal.edu.co.dao.commit;

import java.util.List;

import javax.persistence.Query;

import unal.edu.co.dao.generic.GenericDaoJpaImpl;
import unal.edu.co.entities.Commit;

public class CommitDao extends GenericDaoJpaImpl<Commit, String> {

	public String findByChangeSet(String ChangeSet) {
		String sql = "SELECT c.changeset FROM Commit c where c.changeset='"+ChangeSet+"'";
		Query query = this.entityManager.createQuery(sql);
		String rpta = (String) query.getSingleResult();
		return rpta;
	}
	
	@SuppressWarnings("unchecked")
	public List<Commit> findByChangeSetPaging(int page,int pageSize) {
		String sql = "SELECT c FROM Commit c";
		Query query = this.entityManager.createQuery(sql);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		List<Commit> commits = (List<Commit>) query.getResultList();
		return commits;
	}

}