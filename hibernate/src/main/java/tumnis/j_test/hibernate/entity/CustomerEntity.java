package tumnis.j_test.hibernate.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "customer")
public class CustomerEntity {

	@Id
	private int id;

	@Column
	private String name;



	public CustomerEntity() {
	}

	public CustomerEntity(String name) {
		super();
		this.name = name;
	}



	@Override
	public String toString() {
		return String.format("{ id: %d, name: '%s' }", id, name);
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

}
