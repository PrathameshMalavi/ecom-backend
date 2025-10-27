package org.prathame.malavi.common;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.*;

@ApplicationScoped
public class S3StorageService {

//    private static final String API_URL = "https://Ecommerce.supabase.co/storage/v1/object/images/";
    private static final String API_URL = "https://koxigrrbqfegjofxegck.storage.supabase.co/storage/v1/s3";
    private static final String SECRET_KEY = "589a6c44e0b7b27d6b73d3ef24f02386d3503954f2afcbfe94fd90e8134650fc"; // server-side key
    private static final String ACESS_KEY = "a85c239f141584e5738b07cd7f7fffdf"; // server-side key
    private final HttpClient client = HttpClient.newHttpClient();


    private final S3Client s3;
    private String imageUrl = "";

    public S3StorageService() {
        s3 = S3Client.builder()
                .endpointOverride(URI.create(API_URL))
                .region(Region.of("ap-south-1"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACESS_KEY, SECRET_KEY)
                ))
                .build();
    }

//    public String uploadFile(String bucketName, String keyName, String filePath) {
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(keyName)
//                .acl("public-read") // make file public
//                .build();
//
//        s3.putObject(request, RequestBody.fromFile(Paths.get(filePath)));
//
//        // Public URL
//        return "https://" + bucketName + ".storage.supabase.co/" + keyName;
//    }
//
//    public Set<String> uploadImage(List<InputPart> inputParts) {
//        Set<String> urls = new HashSet<>();
//
//        try {
//            for (InputPart inputPart : inputParts) {
//                InputStream inputStream = inputPart.getBody(InputStream.class, null);
//                byte[] imageBytes = inputStream.readAllBytes();
//
//                String fileName = "uploads/image-" + System.currentTimeMillis() + ".png";
//
//
//                boolean uploaded = supabaseClient.storage()
//                        .from("your-bucket-name")
//                        .upload(fileName, imageBytes);
//
//                if (uploaded) {
//                    String imageUrl = supabaseClient.storage()
//                            .from("your-bucket-name")
//                            .getPublicUrl(fileName);
//                    urls.add(imageUrl);
//                } else {
//                    // Handle upload failure per your logic
//                    // Optionally throw exception or skip
//                    System.err.println("Failed to upload " + fileName);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Optionally rethrow or handle error
//        }
//
//        return urls;
//    }


    // Upload single file from bytes, returns public URL
    public void uploadFile(String bucketName, String keyName, byte[] fileBytes) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .acl("public-read") // Make file publicly readable
                .build();

        s3.putObject(request, RequestBody.fromBytes(fileBytes));
    }

    public Set<String> uploadImage(List<InputPart> inputParts, String bucketName) {
        Set<String> urls = new HashSet<>();

        try {
            for (InputPart inputPart : inputParts) {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] imageBytes = inputStream.readAllBytes();

                String keyName = "uploads/category/products/image-" + System.currentTimeMillis() + ".png"; // or generate unique file name
                String imageUrl = "https://koxigrrbqfegjofxegck.supabase.co/storage/v1/object/public/" + bucketName + "/" + keyName;
                uploadFile(bucketName, keyName, imageBytes);
                urls.add(imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle errors appropriately in your context
        }

        return urls;
    }

    public String uploadSingleImage(InputPart inputPart, String bucketName) {
        try{
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] imageBytes = inputStream.readAllBytes();

            String keyName = "uploads/category/products/image-" + System.currentTimeMillis() + ".png"; // or generate unique file name
            String imageUrl = "https://koxigrrbqfegjofxegck.supabase.co/storage/v1/object/public/" + bucketName + "/" + keyName;
            uploadFile(bucketName, keyName, imageBytes);
            return  imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle errors appropriately in your context
        }
        return null;
    }



    public void deleteFile(String fileUrl) {
        Map<String, String> parsed = parseSupabaseUrl(fileUrl);
        String bucket = parsed.get("bucket");
        String key = parsed.get("key");

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3.deleteObject(deleteRequest);

        System.out.println("Deleted: " + bucket + "/" + key);
    }

    public static Map<String, String> parseSupabaseUrl(String fileUrl) {
        // Example: https://.../storage/v1/object/public/Ecommerce/uploads/category/products/image.png
        String[] parts = fileUrl.split("/public/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid Supabase storage URL: " + fileUrl);
        }

        String path = parts[1]; // Ecommerce/uploads/category/products/image.png
        int firstSlash = path.indexOf("/");
        if (firstSlash == -1) {
            throw new IllegalArgumentException("Invalid Supabase storage URL structure.");
        }

        String bucketName = path.substring(0, firstSlash);
        String keyName = path.substring(firstSlash + 1);

        Map<String, String> map = new HashMap<>();
        map.put("bucket", bucketName);
        map.put("key", keyName);
        return map;
    }

//For Public Acess
//    public boolean uploadFile(InputStream fileStream, String fileName, String contentType) {
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(API_URL + fileName))
//                    .header("apikey", API_KEY)
//                    .header("Authorization", "Bearer " + API_KEY)
//                    .header("Content-Type", contentType)
//                    .PUT(HttpRequest.BodyPublishers.ofInputStream(() -> fileStream))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.statusCode() == 200 || response.statusCode() == 201;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public String getFileUrl(String fileName) {
//        // public bucket URL
//        return "https://YOUR_PROJECT.supabase.co/storage/v1/object/public/images/" + fileName;
//    }
}