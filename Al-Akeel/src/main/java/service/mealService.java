package service;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejbs.Meal;

@Stateless
@Path("/meals")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class mealService {
	@PersistenceContext
	EntityManager em;
	
	@GET()
	@Path("/")
	public Response getAllMeals() {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<Meal> query=em.createQuery("SELECT m from Meal m",Meal.class);
		List<Meal> list =query.getResultList();
		res.put("message", "success");
		res.put("Meals", list);
		return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/create")
	public Response createMeal(Meal m) {
		HashMap<Object, Object> res = new HashMap<>();
		em.persist(m);
		res.put("message", "success");
		res.put("Meal", m);
		return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
	}
}
