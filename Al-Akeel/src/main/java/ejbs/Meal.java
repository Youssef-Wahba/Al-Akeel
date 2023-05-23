package ejbs;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
public class Meal{
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	private String name;
	private double price;
	
	@Transient
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name="orderId")
	private Order order;

	@Transient
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name="restaurantId")
	private Restaurant byRestaurant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Restaurant getByRestaurant() {
		return byRestaurant;
	}

	public void setByRestaurant(Restaurant byRestaurant) {
		this.byRestaurant = byRestaurant;
	}

	@Override
	public String toString() {
		return "Meal [id=" + id + ", name=" + name + ", price=" + price + ", order=" + order + ", byRestaurant="
				+ byRestaurant + "]";
	}
}
