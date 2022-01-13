package com.example.FortuneCookieAPi2.controller;

import java.util.List;

import com.example.FortuneCookieAPi2.service.ServiceFortuneCookie;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path="/cookie", produces= MediaType.APPLICATION_JSON_VALUE)
public class RestControllerCookie {
    
    @Autowired
    private ServiceFortuneCookie svcFortuneCookie;

    @GetMapping
    public ResponseEntity<String>getCookies(
        @RequestParam(defaultValue = "1") Integer count){

        //if count is outside range, execute an error
        if((count < 1) || (count > 10)){
            //builder object for error
            JsonObjectBuilder jsonErrorBuilder 
                = Json.createObjectBuilder();
            //add error msg to builder object
            jsonErrorBuilder.add("Error","number is out of range");   

            //build the errorbuilder object
            JsonObject error =jsonErrorBuilder.build();
            //to execute the http error request by inserting the Json error object
            ResponseEntity<String> errorMsg =
                     new ResponseEntity<>(error.toString(), (HttpStatus.BAD_REQUEST));
        
        return errorMsg;
        }
    //arraybuilder to array of cookies
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    //list to iterate through the cookies from service
    List<String> cookieList = svcFortuneCookie.getCookies(count);
    for(int i =0; i <cookieList.size(); i++){
        String fortune = cookieList.get(i);
        //adding the cookies into the arraybuilder
        arrayBuilder.add(fortune);
    }
    //build the arraybuilder object
    JsonArray array = arrayBuilder.build();

    //timestamp
    Long timeStamp = System.currentTimeMillis();
    //creating jsonBuilder object
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    //adding the array of cookies into the builder
    jsonBuilder.add("Cookies", array);
    jsonBuilder.add("TimeStamp", timeStamp);
    
    //build the builder object of cookies
    JsonObject cookies = jsonBuilder.build();



    return ResponseEntity.ok(cookies.toString());
    }
           
}

