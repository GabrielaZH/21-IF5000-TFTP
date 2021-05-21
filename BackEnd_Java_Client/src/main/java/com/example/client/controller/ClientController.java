package com.example.client.controller;

import com.example.client.model.Client;
import com.example.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/client")
@RestController
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping("/clients")
    public List<Client> getAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        Client client = null;
        Map<String,Object> response = new HashMap<>();
        try{
            client = service.get(id);
        }catch(DataAccessException e){//ServerErrorException
            response.put("mensaje","Error de servidor");
            response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(NoSuchElementException n){
            response.put("mensaje","El cliente ID: ".concat(id.toString().concat("no existe en la base de datos!")));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Client client)
    {

        Map<String,Object> response = new HashMap<>();

        try{
            service.save(client);
            response.put("mensaje:","Cliente ingresado");

        }catch(DataAccessException e) {//ServerErrorException
            response.put("mensaje", "Error al insertar");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>( response, HttpStatus.CREATED);
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Client client)
    {
        Client clientRequest = null;
        Map<String,Object> response = new HashMap<>();
        try{
            clientRequest = service.login(client);
            if(clientRequest==null){
                response.put("mensaje:","Cliente no registrado");
                return new ResponseEntity<Map<String, Object>>( response, HttpStatus.NOT_FOUND);
            }
        }catch(DataAccessException e){//ServerErrorException
            response.put("mensaje","Error de servidor");
            response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(clientRequest, HttpStatus.OK);
    }


}
