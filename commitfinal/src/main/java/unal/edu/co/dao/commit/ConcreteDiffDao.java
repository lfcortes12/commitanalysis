package unal.edu.co.dao.commit;

import java.util.List;

import javax.persistence.Query;

import unal.edu.co.dao.generic.GenericDaoJpaImpl;
import unal.edu.co.entities.ConcreteDiff;

public class ConcreteDiffDao extends GenericDaoJpaImpl<ConcreteDiff, String> {

	@SuppressWarnings("unchecked")
	public List<ConcreteDiff> findByChangeSetPaging(int page,int pageSize) {
		String sql = "SELECT c FROM ConcreteDiff c";
		Query query = this.entityManager.createQuery(sql);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		List<ConcreteDiff> concreteDiffs = (List<ConcreteDiff>) query.getResultList();
		return concreteDiffs;
	}

}