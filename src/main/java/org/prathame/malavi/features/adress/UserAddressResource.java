package org.prathame.malavi.features.adress;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAddressResource {

    @Inject
    UserAddressRepository userAddressRepository;

    @GET
    @Path("/{userName}")
    @RolesAllowed("user")
    public Response getUserAddresses(@PathParam("userName") String userName) {
        List<UserAddress> addresses = userAddressRepository.find("userName", userName).list();
        if (addresses.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No addresses found for user: " + userName)
                    .build();
        }
        return Response.ok(addresses).build();
    }

    @POST
    @Transactional
    @RolesAllowed("user")
    public Response addUserAddress(UserAddress address) {
        if (address.getUserName() == null || address.getUserName().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("userName is required.")
                    .build();
        }
        userAddressRepository.persist(address);
        return Response.status(Response.Status.CREATED)
                .entity(address)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("user")
    public Response updateUserAddress(@PathParam("id") Long id, UserAddress updatedAddress) {
        UserAddress existing = userAddressRepository.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Address with ID " + id + " not found.")
                    .build();
        }

        // Update fields
        existing.setName(updatedAddress.getName());
        existing.setAddress(updatedAddress.getAddress());
        existing.setCity(updatedAddress.getCity());
        existing.setState(updatedAddress.getState());
        existing.setPincode(updatedAddress.getPincode());
        existing.setContact(updatedAddress.getContact());

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("user")
    public Response deleteAddress(@PathParam("id") Long id) {
        boolean deleted = userAddressRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Address with ID " + id + " not found.")
                    .build();
        }
    }
}