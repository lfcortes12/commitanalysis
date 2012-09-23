package unal.edu.co.dao.commit;

import java.util.List;

import javax.persistence.Query;

import unal.edu.co.dao.generic.GenericDaoJpaImpl;
import unal.edu.co.entities.CommitDiff;
/**
 * @author Fernando Cortes
 *
 */
public class CommitDiffDao extends GenericDaoJpaImpl<CommitDiff, String> {

	public String findByChangeSet(String ChangeSet) {
		String sql = "SELECT c.changeset FROM CommitDiff c where c.changeset='"+ChangeSet+"'";
		Query query = this.entityManager.createQuery(sql);
		String rpta = (String) query.getSingleResult();
		return rpta;
	}
	
	public CommitDiff findByChangeSetValue(String ChangeSet) {
		String sql = "SELECT c FROM CommitDiff c where c.changeset='"+ChangeSet+"'";
		Query query = this.entityManager.createQuery(sql,CommitDiff.class);
		return (CommitDiff) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CommitDiff> findByChangeSetPaging(int page,int pageSize) {
		String sql = "SELECT c FROM CommitDiff c";
		Query query = this.entityManager.createQuery(sql);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		List<CommitDiff> commitsDiff = (List<CommitDiff>) query.getResultList();
		return commitsDiff;
	}
	
	public boolean updateCommitDiff(CommitDiff diff) {
			entityManager.getTransaction().begin();
			CommitDiff commitdiffx = entityManager.find(CommitDiff.class,diff.getChangeset());
			commitdiffx.setDiff(diff.getDiff());
			commitdiffx.setProcessedDiff(diff.getProcessedDiff());
			commitdiffx.setProjectName(diff.getProjectName());
			entityManager.getTransaction().commit();
			return true;
		}

}