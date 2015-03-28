package database;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

//TODO: impl IQueryExecutor + IQueryExecutorsFactory
public class DBContext implements IDBContext
{
	private final static Logger log = LogManager.getLogger(DBContext.class);
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("overclocking");

	public DBContext()
	{
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("overclocking");
	}

	@Override
	public <T> List<T> select(Class<T> c) throws DBException
	{
		EntityManager entityManager = null;
		try
		{
			entityManager = entityManagerFactory.createEntityManager();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> query = criteriaBuilder.createQuery(c);
			CriteriaQuery<T> select = query.select(query.from(c));
			TypedQuery<T> typedQuery = entityManager.createQuery(select);
			return typedQuery.getResultList();
		}
		catch (Exception e)
		{
			String message = "Fail to execute select query for class " + c.toString();
			log.error(message, e);
			throw new DBException(message);
		}
		finally
		{
			if (entityManager != null)
				entityManager.close();
		}
	}

	@Override
	public <T> void truncateTable(Class<T> c) throws DBException
	{
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try
		{
			entityManager = entityManagerFactory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			Query query = entityManager.createQuery("DELETE FROM " + c.getSimpleName());
			query.executeUpdate();
			transaction.commit();
		}
		catch (Exception e)
		{
			String message = "Fail to delete table " + c;
			if (transaction != null)
				transaction.rollback();
			log.error(message, e);
			throw new DBException(message);
		}
		finally
		{
			if (entityManager != null)
				entityManager.close();
		}
	}

	public <T> void create(T entity)
	{
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try
		{
			entityManager = entityManagerFactory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.merge(entity);
			transaction.commit();
		}
		catch (Exception e)
		{
			String message = "Fail to create entity";
			log.error(message, e);
			if (transaction != null)
				transaction.rollback();
			throw new DBException(message);
		}
		finally
		{
			if (entityManager != null)
				entityManager.close();
		}
	}
}