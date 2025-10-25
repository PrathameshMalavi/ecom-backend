package org.prathame.malavi.controller;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.OrderDetail;
import org.prathame.malavi.entity.OrderInput;
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
    @RolesAllowed("User")
    public void placeOrder(@PathParam("isSingleProductCheckout") boolean isSingleProductCheckout,
                           OrderInput orderInput) {
        orderDetailService.placeOrder(orderInput, isSingleProductCheckout);
    }

    @GET
    @Path("/getOrderDetails")
    @RolesAllowed("User")
    public List<OrderDetail> getOrderDetails() {
        return orderDetailService.getOrderDetails();
    }

    @GET
    @Path("/getAllOrderDetails/{status}")
    @RolesAllowed("Admin")
    public List<OrderDetail> getAllOrderDetails(@PathParam("status") String status) {
        return orderDetailService.getAllOrderDetails(status);
    }

    @GET
    @Path("/markOrderAsDelivered/{orderId}")
    @RolesAllowed("Admin")
    public void markOrderAsDelivered(@PathParam("orderId") Integer orderId) {
        orderDetailService.markOrderAsDelivered(orderId);
    }

    @GET
    @Path("/createTransaction/{amount}")
    @RolesAllowed("User")
    public TransactionDetails createTransaction(@PathParam("amount") Double amount) {
        return orderDetailService.createTransaction(amount);
    }

}
