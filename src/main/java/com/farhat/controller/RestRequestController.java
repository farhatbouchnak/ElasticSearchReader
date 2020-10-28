package com.farhat.controller;
/*
 * Created by farhat
 */

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farhat.dao.QueryDAO;
import com.farhat.entity.PayloadDocument;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
public class RestRequestController {

    private final QueryDAO dao;

    /* Dependency injection by constructor of the DAO layer*/
    public RestRequestController(final QueryDAO dao){
        this.dao = dao;
    }

    /**
     * AN End point for inserting documents into the created index 
     *  of the elasticsearch cluster
     * @param document
     * @return
     */
    @PostMapping(value = "/api/create", headers = "Accept=application/json")
    public String create(@RequestBody PayloadDocument document) {
        return dao.indexRequest(document);
    }

    /**
     * A Rest End Point for retriving all documents into the elasticsearch cluster
     * @author farhat
     * @return List<Document>
     */
    @GetMapping(value = "/api/all", produces = "application/json")
    public List<PayloadDocument> getAllDocuments() {
        return dao.matchAllQuery();
    }

    /**
     * REST End point to search for list of documents using a query
     * @param query
     * @return
     */
    @GetMapping("/api/search")
    public List<PayloadDocument> search(@RequestParam("query") String query) throws IOException{
        return dao.wildcardQuery(query);
    }
    
    /**
     * REST End point to search for list of documents using a query
     * @param query
     * @return
     */
    @GetMapping("/api/searchByField")
    public List<PayloadDocument> searchByField(@RequestBody JsonNode after) 
    		throws IOException{
        return dao.wildcardQueryByField(after);
    }
    
    /**
    * REST End Point to delete a document by id from the
    * elasticsearch cluster
    * @param id
    */
   @GetMapping(value = "/api/delete/{id}")
   public void delete(String id){
       dao.deleteDocument(id);
   }
   
   
   
   @GetMapping(value = "/api/deleteIndex")
   public boolean deleteIndex() throws IOException{
	   return dao.deleteIndex();
   }
}
