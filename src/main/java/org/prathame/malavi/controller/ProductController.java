package org.prathame.malavi.controller;

import io.netty.handler.codec.http.multipart.FileUpload;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.prathame.malavi.Common.S3StorageService;
import org.prathame.malavi.entity.ImageModel;
import org.prathame.malavi.entity.Product;
import org.prathame.malavi.service.ProductService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.StandardCopyOption;
import java.util.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/")
//@Authenticated()
public class ProductController {

    @Inject
    ProductService productService;

    @Inject
    S3StorageService s3StorageService;

    //    @RolesAllowed("Admin")
    @POST
    @Path("/addNewProduct")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Product addNewProduct(MultipartFormDataInput input) {

        try {
            // Extract product JSON
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

            // Get product part
            List<InputPart> productParts = uploadForm.get("product");
            uploadForm.forEach((key, value) -> System.out.println(key + " => " + value.size()));


            Product product = null;
            if (!productParts.isEmpty()) {
//                product = productParts.get(0).getBody(Product.class, (Type) new Product());
                String productJson = productParts.get(0).getBodyAsString();
                product = JsonbBuilder.create().fromJson(productJson, Product.class);
                System.out.println("Line 1.0      : " + product.toString());
            }

            // Get image files
            List<InputPart> imageParts = uploadForm.get("imageFile");

            Set<String> imgUrls = uploadImageS3(imageParts);
            product.setImageUrls(imgUrls);

            Set<ImageModel> images = uploadImage(imageParts);
            product.setProductImages(images);
            return productService.addNewProduct(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Set<String> uploadImageS3(List<InputPart> inputParts) throws IOException {
        Set<String> imgUrls = new HashSet<>();
        if (inputParts != null) {
            imgUrls = s3StorageService.uploadImage(inputParts, "Ecommerce");
//            for (InputPart inputPart : inputParts){
//                String keyName = "products/" + System.currentTimeMillis() + "_" + inputPart.getFileName();
//
//                for (ProductUploadForm.FileUpload fileUpload : form.images) {
//                    if (fileUpload.fileName == null) continue;
//
//                    java.nio.file.Path tempFile = Files.createTempFile("upload_", "_" + fileUpload.fileName);
//                    Files.copy(fileUpload.file, tempFile, StandardCopyOption.REPLACE_EXISTING);
//
//                    String keyName = "products/" + System.currentTimeMillis() + "_" + fileUpload.fileName;
//                    String uploadedUrl = s3StorageService.uploadFile("Ecommerce", keyName, tempFile.toString());
//
//                    imageUrls.add(uploadedUrl);
//                    Files.deleteIfExists(tempFile);
//                }
//            }
        }
        return imgUrls;
    }


    public Set<ImageModel> uploadImage(List<InputPart> inputParts) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        if (inputParts != null) {
            for (InputPart inputPart : inputParts) {
                // Get filename from content-disposition header
                String[] contentDispositionHeader = inputPart.getHeaders()
                        .getFirst("Content-Disposition").split(";");
                String filename = null;
                for (String name : contentDispositionHeader) {
                    if (name.trim().startsWith("filename")) {
                        filename = name.substring(name.indexOf('=') + 1).trim().replace("\"", "");
                    }
                }

                // Get content type
                String contentType = inputPart.getMediaType().toString();

                // Read file bytes
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = inputStream.readAllBytes();

                ImageModel imageModel = new ImageModel(filename, contentType, bytes);
                imageModels.add(imageModel);
            }
        }

        return imageModels;
    }

    @GET
    @Path("/getAllProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts(
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("searchKey") @DefaultValue("") String searchKey) {
        List<Product> result = productService.getAllProducts(pageNumber, searchKey);
        System.out.println("Result size is " + result.size());
        return result;
    }

    @GET
    @Path("/getProductDetailsById/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProductDetailsById(@PathParam("productId") Integer productId) {
        return productService.getProductDetailsById(productId);
    }

    @RolesAllowed("Admin")
    @DELETE
    @Path("/deleteProductDetails/{productId}")
    public void deleteProductDetails(@PathParam("productId") Integer productId) {
        productService.deleteProductDetails(productId);
    }

    @RolesAllowed("User")
    @GET
    @Path("/getProductDetails/{isSingleProductCheckout}/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProductDetails(
            @PathParam("isSingleProductCheckout") boolean isSingleProductCheckout,
            @PathParam("productId") Integer productId) {
        return productService.getProductDetails(isSingleProductCheckout, productId);
    }
}