package ejbs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.sql.RowIdLifetime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity 
public class Order {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	private double totalPrice;
	private String status;
	private LocalDateTime date = LocalDateTime.now();
	@Transient
	@OneToMany(mappedBy="order")
	private List<Meal> meals = new ArrayList<>();
	
	@Transient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="restaurantId")
	private Restaurant orderBy;

	@OneToOne(optional = false)
	@JoinColumn(name="runnerId")
	private Runner runner;
	
	public Order(){
		this.date= LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}

	public Restaurant getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Restaurant orderBy) {
		this.orderBy = orderBy;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", totalPrice=" + totalPrice + ", status=" + status + ", date=" + date + ", meals="
				+ meals + ", orderBy=" + orderBy + ", runner=" + runner + "]";
	}
	
	
	
	
}
