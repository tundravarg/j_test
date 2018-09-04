package tumnis.j_test.hibernate.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "\"order\"")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String type;

	@Column
	private String name;

	@ManyToOne(targetEntity=OrderEntity.class, fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="parent")
	private OrderEntity parent;

	@ManyToOne(targetEntity=CustomerEntity.class, fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="customer")
	private CustomerEntity customer;



	public OrderEntity() {
	}

	public OrderEntity(String type, String name) {
		this.type = type;
		this.name = name;
	}



	@Override
	public String toString() {
		return String.format("{ id: %d, type: '%s', name: '%s', parent: %s, customer: %s }", id, type, name, parent, customer);
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderEntity getParent() {
		return parent;
	}

	public void setParent(OrderEntity parent) {
		this.parent = parent;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

}
