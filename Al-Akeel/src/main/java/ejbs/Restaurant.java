package ejbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
public class Restaurant {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	private String name;
	private Long ownerId;
	private int completedOrdersCount = 0;
	private int canceledOrdersCount = 0;
	private double restaurantEarns = 0;
	@Transient
	@OneToMany(mappedBy="byRestaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Meal> meals = new ArrayList<>();
	@Transient
	@OneToMany(mappedBy="orderBy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();

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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals=meals;
	}

	public List<Order> getOrders() {
		return orders;
	}
	public void addMeal(Meal meal) {
		this.meals.add(meal);
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public int getCompletedOrdersCount() {
		return completedOrdersCount;
	}

	public void setCompletedOrdersCount(int completedOrdersCount) {
		this.completedOrdersCount = completedOrdersCount;
	}

	public int getCanceledOrdersCount() {
		return canceledOrdersCount;
	}

	public void setCanceledOrdersCount(int canceledOrdersCount) {
		this.canceledOrdersCount = canceledOrdersCount;
	}

	public double getRestaurantEarns() {
		return restaurantEarns;
	}

	public void setRestaurantEarns(double restaurantEarns) {
		this.restaurantEarns = restaurantEarns;
	}
	

}
