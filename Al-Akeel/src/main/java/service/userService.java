package service;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejbs.Order;
import ejbs.Runner;
import ejbs.User;

@Stateless
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userService {
	
	@PersistenceContext
	EntityManager em;
	
    @POST
	@Path("/signup")
    public Response signUp(User user)  {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<User> query=em.createQuery("SELECT u from User u where u.username=?1",User.class);
		query.setParameter(1, user.getUsername());
        List<User> list =query.getResultList();
         if  (list.isEmpty()) {
             em.persist(user);
             res.put("message", "success");
             res.put("user", user);
             return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
         else {
        	 res.put("message", "user already exists");
        	 return Response.status(Response.Status.BAD_REQUEST).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
    }
    
    @POST
    @Path("/edit-order/{id}")
    public Response editOrder(Order order,@PathParam("id") Long orderId) {
    	HashMap<Object, Object> res = new HashMap<>();
    	Order o = em.find(Order.class, orderId);
    	if(o == null) {
    		res.put("message", "order id not found");
       	 	return Response.status(Response.Status.NOT_FOUND).entity(res).type(MediaType.APPLICATION_JSON).build();
    	}
    	if(!order.getMeals().isEmpty())
    		o.setMeals(order.getMeals());
    	if(!order.getOrderBy().equals(null))
    		o.setOrderBy(order.getOrderBy());
    	if(!order.getRunner().equals(null))
    		o.setRunner(order.getRunner());
    	o.setTotalPrice(order.getTotalPrice()-order.getRunner().getDeliveryFees());
    	if(order.getStatus().equals("canceled")) {
    		res.put("message", "order is canceled");
       	 	return Response.status(Response.Status.BAD_REQUEST).entity(res).type(MediaType.APPLICATION_JSON).build();
    	}
    	em.merge(o);
    	res.put("message","success");
    	res.put("order", o);
    	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/create-order")
    public Response createOrder(Order order) {
		HashMap<Object, Object> res = new HashMap<>();
		double orderPrice=0;
		for(int i =0;i<order.getMeals().size();i++) {
			orderPrice+=order.getMeals().get(i).getPrice();
		}
		TypedQuery<Runner> query=em.createQuery("SELECT o from Order o",Runner.class);
        List<Runner> list =query.getResultList();
        for(int i = 0 ; i<list.size();i++) {
        	if(list.get(i).getStatus().equals("avaliable")) {
        		double runnerFees = list.get(i).getDeliveryFees();
        		order.setTotalPrice(orderPrice+runnerFees);
        		em.persist(order);
        		res.put("message","success");
        		res.put("order",order);
            	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
        	}
        }
        res.put("message", "no avaliable runner");
    	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
    }
    
}