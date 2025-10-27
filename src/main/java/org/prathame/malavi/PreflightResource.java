//package org.prathame.malavi;
//
//import jakarta.ws.rs.OPTIONS;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.core.Response;
//
//@Path("/")
//public class PreflightResource {
//
//    @OPTIONS
//    public Response preflight() {
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "http://localhost:4200")
//                .header("Access-Control-Allow-Credentials", "true")
//                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS")
//                .build();
//    }
//}