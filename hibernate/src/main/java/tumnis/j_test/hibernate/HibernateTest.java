package tumnis.j_test.hibernate;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import tumnis.j_test.hibernate.entity.CustomerEntity;
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
		listOrdersByTypeAndCustomerName("Element", "Customer-2");
	}



	private static void createData() {
		runInTransaction(() -> {

			CustomerEntity customer;
			OrderEntity orderEntity;
			OrderEntity orderEntity1;

			customer = new CustomerEntity("Customer-1");
			entityManager.persist(customer);

			orderEntity = new OrderEntity("Package", "p1");
			orderEntity.setCustomer(customer);
			entityManager.persist(orderEntity);

			orderEntity = new OrderEntity("Package", "p2");
			orderEntity.setCustomer(customer);
			entityManager.persist(orderEntity);

			orderEntity1 = new OrderEntity("Element", "p2-e1");
			orderEntity1.setParent(orderEntity);
			orderEntity1.setCustomer(customer);
			entityManager.persist(orderEntity1);

			customer = new CustomerEntity("Customer-2");
			entityManager.persist(customer);

			orderEntity = new OrderEntity("Package", "p3");
			orderEntity.setCustomer(customer);
			entityManager.persist(orderEntity);

			orderEntity1 = new OrderEntity("Element", "p3-e1");
			orderEntity1.setParent(orderEntity);
			orderEntity1.setCustomer(customer);
			entityManager.persist(orderEntity1);

			orderEntity1 = new OrderEntity("Element", "p3-e2");
			orderEntity1.setParent(orderEntity);
			orderEntity1.setCustomer(customer);
			entityManager.persist(orderEntity1);

			customer = new CustomerEntity("Customer-3");
			entityManager.persist(customer);

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

	private static void listOrdersByTypeAndCustomerName(String orderType, String cutomerName) {
		runInTransaction(() -> {

			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<OrderEntity> orderRoot = cq.from(OrderEntity.class);
			Root<CustomerEntity> customerRoot = cq.from(CustomerEntity.class);
			cq.multiselect(orderRoot, customerRoot);

			Predicate jointPredicate = cb.equal(orderRoot.get("customer"), customerRoot.get("id"));
			Predicate orderTypePredicate = cb.equal(orderRoot.get("type"), orderType);
			Predicate customerNamePredicate = cb.equal(customerRoot.get("name"), cutomerName);
			Predicate predicate = cb.and(jointPredicate, orderTypePredicate, customerNamePredicate);
			cq.where(predicate);

			TypedQuery<Object[]> query = entityManager.createQuery(cq);
			List<Object[]> result = query.getResultList();

			for (Object[] order: result) {
				System.out.printf("order-by-type: %s\n", order[0]);
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
