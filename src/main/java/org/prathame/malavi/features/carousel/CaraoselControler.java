package org.prathame.malavi.features.carousel;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.prathame.malavi.common.S3StorageService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/carousel")
public class CaraoselControler {

    @Inject
    CarouselRepository carouselRepository;


    @Inject
    S3StorageService s3StorageService;


    // 🟢 Get all carousels
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Carousel> getAll() {
        return carouselRepository.listAll();
    }

    // 🟢 Add new carousel
    @POST
    @Transactional
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(MultipartFormDataInput input) {
        Carousel carousel = null;
        try {
            Map<String, List<InputPart>> formParts = input.getFormDataMap();
            System.out.println("Multipart parts test: " + input.getFormDataMap().keySet());


//            // 📝 Extract form fields
//            String url = formParts.get("url").get(0).getBodyAsString();
//            String description = formParts.get("description").get(0).getBodyAsString();

            // 📸 Extract image file
            String imgUrl = null;

            List<InputPart> fileParts = formParts.get("file");
            if (fileParts != null && !fileParts.isEmpty()) {
                InputPart filePart = fileParts.get(0);
                // Upload the file to S3 and get URL
                imgUrl = s3StorageService.uploadSingleImage(filePart, "Ecommerce");
                System.out.println("Uploaded file URL: " + imgUrl);
            }

            System.out.println("Called Null Img URL " + imgUrl);

            // Get Carousel part
            List<InputPart> carouselParts = formParts.get("caraousel");
            formParts.forEach((key, value) -> System.out.println(key + " => " + value.size()));

            if (!carouselParts.isEmpty()) {
                String carouselJson = carouselParts.get(0).getBodyAsString();
                carousel = JsonbBuilder.create().fromJson(carouselJson, Carousel.class);
                System.out.println("Called " + carouselJson);
            }
            Carousel temp;
            if(carousel.getId() != null){
                if(imgUrl != null){
                    carousel.setImageUrl(imgUrl);
                }
                carouselRepository.updateCarouse(carousel.getId().longValue(), carousel);
                temp = carousel;
            }else{
                carousel.setImageUrl(imgUrl);
                carouselRepository.persist(carousel);
                temp = carousel;
                System.out.println("Called " + temp);
            }

            return Response.status(Response.Status.CREATED).entity(carousel).build();
        }catch (Exception e){
            System.out.println("Exception : " + e.toString());
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(carousel).build();

        }
//        return Response.status(Response.Status.CREATED).entity(carousel).build();
    }

    // 🟡 Update existing carousel
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Carousel updatedCarousel) {
        Carousel existing = carouselRepository.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existing.setImageUrl(updatedCarousel.getImageUrl());
        existing.setUrl(updatedCarousel.getUrl());
        existing.setDescription(updatedCarousel.getDescription());
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = carouselRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}