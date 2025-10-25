package org.prathame.malavi.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.Cart;
import org.prathame.malavi.service.CartService;


import java.util.List;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {

    @Inject
    CartService cartService;

    @GET
    @Path("/addToCart/{productId}")
    @RolesAllowed("User")
    public Cart addToCart(@PathParam("productId") Integer productId) {
        return cartService.addToCart(productId);
    }

    @DELETE
    @Path("/deleteCartItem/{cartId}")
    @RolesAllowed("User")
    public void deleteCartItem(@PathParam("cartId") Integer cartId) {
        cartService.deleteCartItem(cartId);
    }

    @GET
    @Path("/getCartDetails")
    @RolesAllowed("User")
    public List<Cart> getCartDetails() {
        return cartService.getCartDetails();
    }
}