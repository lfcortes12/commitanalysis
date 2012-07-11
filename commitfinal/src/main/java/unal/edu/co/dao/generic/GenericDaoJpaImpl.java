package unal.edu.co.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class GenericDaoJpaImpl<T, PK extends Serializable> 
    implements GenericDao<T, PK> {

    protected Class<T> entityClass;

    @PersistenceContext(name="commitfinal")
    protected EntityManager entityManager;
    
    public EntityManagerFactory factory;

    @SuppressWarnings("unchecked")
	public GenericDaoJpaImpl() {
    	factory = Persistence.createEntityManagerFactory("commitfinal");
    	entityManager = factory.createEntityManager();
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T create(T t) {
    	entityManager.getTransaction().begin();
        this.entityManager.persist(t);
        this.entityManager.flush();
        entityManager.getTransaction().commit();
        return t;
    }

    @Override
    public T read(PK id) {
        return this.entityManager.find(entityClass, id);
    }

    @Override
    public T update(T t) {
        return this.entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }
}