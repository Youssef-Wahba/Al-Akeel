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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Runner;

@Stateless
@Path("/runners")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class runnerService {
	
	@PersistenceContext
	EntityManager em;
	
	private static String [] runnerStatus= {"avaliable","busy"};
	private static String [] orderStatus = {"completed","canceled"};
	
    @POST
	@Path("/signup")
	public Response signUp(Runner runner)  {
		HashMap<Object, Object> res = new HashMap<>();
		TypedQuery<Runner> query=em.createQuery("SELECT r from Runner r where r.username=?1",Runner.class);
		query.setParameter(1, runner.getUsername());
        List<Runner> list =query.getResultList();
         if  (list.isEmpty()) {
             em.persist(runner);
             res.put("message", "success");
             res.put("runner", runner);
             return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
         else {
        	 res.put("message", "runner already exists");
        	 return Response.status(Response.Status.BAD_REQUEST).entity(res).type(MediaType.APPLICATION_JSON).build();
         }
    }
    
    @POST
    @Path("/order-completed/{runnerId}")
    public Response orderCompleted(@PathParam("runnerId") Long runnerId) {
    	HashMap<Object, Object> res = new HashMap<>();
    	TypedQuery<Runner> query=em.createQuery("SELECT r from Runner r where r.id=?1",Runner.class);
    	query.setParameter(1,runnerId );
    	List<Runner> list =query.getResultList();
    	if  (list.isEmpty()) {
            res.put("message", "runner id not found");
            return Response.status(Response.Status.NOT_FOUND).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
        else {
        	Runner r = list.get(0);
        	r.getOrder().setStatus(orderStatus[0]);
        	double orderValue =r.getOrder().getTotalPrice();
        	double runnerDeliveryFees = r.getDeliveryFees();
        	double restaurantEarns = r.getOrder().getOrderBy().getRestaurantEarns();
        	r.getOrder().getOrderBy().setRestaurantEarns(restaurantEarns+orderValue-runnerDeliveryFees);
        	r.setStatus(runnerStatus[0]);
        	r.setTripsCount(r.getTripsCount()+1);
        	int restaurantCompletedOrders = r.getOrder().getOrderBy().getCompletedOrdersCount()+1;
        	r.getOrder().getOrderBy().setCompletedOrdersCount(restaurantCompletedOrders);
        	res.put("message", "success");
       	 	res.put("runner", r);
       	 	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @POST
    @Path("/order-canceled/{runnerId}")
    public Response orderCanceled(@PathParam("runnerId") Long runnerId) {
    	HashMap<Object, Object> res = new HashMap<>();
    	TypedQuery<Runner> query=em.createQuery("SELECT r from Runner r where r.id=?1",Runner.class);
    	query.setParameter(1,runnerId );
    	List<Runner> list =query.getResultList();
    	if  (list.isEmpty()) {
            res.put("message", "runner id not found");
            return Response.status(Response.Status.NOT_FOUND).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
        else {
        	Runner r = list.get(0);
        	r.getOrder().setStatus(orderStatus[1]);
        	double runnerDeliveryFees = r.getDeliveryFees();
        	double restaurantEarns = r.getOrder().getOrderBy().getRestaurantEarns();
        	r.getOrder().getOrderBy().setRestaurantEarns(restaurantEarns-runnerDeliveryFees);
        	r.setStatus(runnerStatus[0]);
        	r.setTripsCount(r.getTripsCount()+1);
        	int restaurantCanceledOrders = r.getOrder().getOrderBy().getCanceledOrdersCount()+1;
        	r.getOrder().getOrderBy().setCanceledOrdersCount(restaurantCanceledOrders);
        	res.put("message", "success");
       	 	res.put("runner", r);
       	 	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
    }
    @GET
    @Path("/trips/{runnerId}")
    public Response orderNumber(@PathParam("runnerId") Long runnerId) {
    	HashMap<Object, Object> res = new HashMap<>();
    	TypedQuery<Runner> query=em.createQuery("SELECT r from Runner r where r.id=?1",Runner.class);
    	query.setParameter(1,runnerId);
    	List<Runner> list =query.getResultList();
    	if  (list.isEmpty()) {
            res.put("message", "runner id not found");
            return Response.status(Response.Status.NOT_FOUND).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
        else {
        	Runner r = list.get(0);
        	res.put("Id",r.getId());
        	res.put("Username",r.getUsername());
        	res.put("Number of trips",r.getTripsCount());
        	return Response.status(Response.Status.OK).entity(res).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
