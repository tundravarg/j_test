package tumnis.j_test.hibernate;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import tumnis.j_test.hibernate.entity.OrderEntity;



/**
 * Hibernate Test
 */
public class HibernateTest {

	private static final EntityManagerFactory entityManagerFactory;
	static {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("test");
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	private static EntityManager entityManager = getEntityManager();



	public static void main(String[] args) {
		System.out.println("Hibernate Test");
		createData();
		listData();
	}



	private static void createData() {
		runInTransaction(() -> {

			OrderEntity orderEntity;
			OrderEntity orderEntity1;

			orderEntity = new OrderEntity("T1", "T1-P");
			entityManager.persist(orderEntity);

			orderEntity1 = new OrderEntity("T1", "T1-C1");
			orderEntity1.setParent(orderEntity);
			entityManager.persist(orderEntity1);

		});
	}

	private static void listData() {
		runInTransaction(() -> {

			List<OrderEntity> orders = entityManager.createQuery("from OrderEntity").getResultList();
			for (OrderEntity order: orders) {
				System.out.printf("order: %s\n", order);
			}

		});
	}



	private interface TransactionalExec {
		void run() throws Exception;
	}

	private static void runInTransaction(TransactionalExec exec) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			exec.run();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

}
