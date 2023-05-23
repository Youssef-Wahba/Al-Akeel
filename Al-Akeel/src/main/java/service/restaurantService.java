package service;

import java.util.Arrays;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejbs.Meal;
import ejbs.Restaurant;


@Stateless
@Path("/restaurants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class restaurantService {
	@PersistenceContext
	EntityManager em;

	@GET()
	@Path("/")
	public Response getAllRestaurants() {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<Restaurant> query=em.createQuery("SELECT r from Restaurant r",Restaurant.class);
		List<Restaurant> list =query.getResultList();
		res.put("message", "success");
		res.put("Restaurants", list);
		return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/create")
	public Response addRestaurant(Restaurant r) {
		HashMap<Object, Object> res = new HashMap<>();
		em.persist(r);
		res.put("message", "success");
		res.put("Restaurants", r);
		return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
	}
	
	@GET()
	@Path("/{id}")
	public Response getRestaurantById(@PathParam("id") Long id) {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<Restaurant> query=em.createQuery("SELECT r from Restaurant r WHERE r.id=?1",Restaurant.class);
		query.setParameter(1,id);
		List<Restaurant> list =query.getResultList();
		 if  (!list.isEmpty()) {
             res.put("message", "success");
             res.put("Restaurant", list.get(0));
             return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
         else {
        	 res.put("message", "restaurant not found");
        	 return Response.status(Response.Status.BAD_REQUEST).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
	}
	
	
	@POST
	@Path("/create-meal/{id}")
	public Response addRestaurant(Meal m,@PathParam("id") Long restaurantId) {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<Restaurant> query=em.createQuery("SELECT r from Restaurant r WHERE r.id=?1",Restaurant.class);
		query.setParameter(1,restaurantId);
		List<Restaurant> list =query.getResultList();
		 if  (!list.isEmpty()) {
			 Restaurant r = list.get(0);
			 m.setByRestaurant(r);
			 r.setMeals(Arrays.asList(m));
			 em.persist(m);
             res.put("message", "success");
             res.put("Restaurant", r);
             return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
         else {
        	 res.put("message", "restaurant not found");
        	 return Response.status(Response.Status.BAD_REQUEST).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
	}
}
