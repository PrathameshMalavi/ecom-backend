package org.prathame.malavi.controller;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.OrderDetail;
import org.prathame.malavi.entity.OrderInput;
import org.prathame.malavi.entity.OrderTrack;
import org.prathame.malavi.entity.TransactionDetails;
import org.prathame.malavi.service.OrderDetailService;

import java.util.List;


import java.util.List;
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderDetailController {

    @Inject
    OrderDetailService orderDetailService;

    @POST
    @Path("/placeOrder/{isSingleProductCheckout}")
    @RolesAllowed("user")
    public void placeOrder(@PathParam("isSingleProductCheckout") boolean isSingleProductCheckout,
                           OrderInput orderInput) {
        orderDetailService.placeOrder(orderInput, isSingleProductCheckout);
    }

    @GET
    @Path("/getAllOrderDetails")
    @RolesAllowed("user")
    public List<OrderDetail> getOrderDetails() {
        return orderDetailService.getOrderDetails();
    }

    @GET
    @Path("/getAllOrderDetails/{status}")
    @RolesAllowed("admin")
    public List<OrderDetail> getAllOrderDetails(@PathParam("status") String status) {
        return orderDetailService.getAllOrderDetails(status);
    }

    @GET
    @Path("/markAsShipped/{orderId}")
    @RolesAllowed("admin")
    public void markOrderAsShipped(@PathParam("orderId") Integer orderId) {
        orderDetailService.markOrderAsShipped(orderId);
    }

    @POST
    @Path("/addOrderTrack/{orderId}")
    @RolesAllowed("admin")
    public void addOrderTrack(@PathParam("orderId") Integer orderId, OrderTrack orderTrack) {
        orderDetailService.addTrackingOrder(orderId, orderTrack);
    }

    @GET
    @Path("/getOrderTracks/{orderId}")
    @RolesAllowed("admin")
    public List<OrderTrack> addOrderTrack(@PathParam("orderId") Integer orderId) {
        return orderDetailService.getOrderTrackList(orderId);
    }


    @GET
    @Path("/markAsReachedDestination/{orderId}")
    @RolesAllowed("admin")
    public void markOrderAsReachedDestination(@PathParam("orderId") Integer orderId) {
        orderDetailService.markOrderAsShipped(orderId);
    }

    @GET
    @Path("/markAsDelivered/{orderId}")
    @RolesAllowed("admin")
    public void markOrderAsDelivered(@PathParam("orderId") Integer orderId) {
        orderDetailService.markOrderAsDelivered(orderId);
    }

    @GET
    @Path("/createTransaction/{amount}")
    @RolesAllowed("user")
    public TransactionDetails createTransaction(@PathParam("amount") Double amount) {
        return orderDetailService.createTransaction(amount);
    }

}
