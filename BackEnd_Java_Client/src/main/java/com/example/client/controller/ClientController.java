package com.example.client.controller;



//import com.example.client.clientTFTP.TFTPClient;
import com.example.client.clientTFTP.TFTPClient;
import com.example.client.model.Client;
import com.example.client.service.ClientService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;





@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/client")
@RestController
public class ClientController {
    private String userName  ="";
    private String path= "C:\\21-IF5000-TFTP\\BackEnd_Java_Client\\src\\main\\java\\com\\example\\client\\images"+"\\";

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
        }catch(DataAccessException e){
            response.put("message","Server Error");
            response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(NoSuchElementException n){
            response.put("message","Client with ID: ".concat(id.toString().concat("does not exist in the database!")));
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
            response.put("message:","Client saved");

        }catch(DataAccessException e) {
            response.put("message", "Save error");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>( response, HttpStatus.CREATED);
    }



    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Client client)
    {
        userName = client.getName();
        Client clientRequest = null;
        Map<String,Object> response = new HashMap<>();
        try{
            clientRequest = service.login(client);
            if(clientRequest==null){
                response.put("message:","does not exist in the database!");
                return new ResponseEntity<Map<String, Object>>( response, HttpStatus.NOT_FOUND);
            }
        }catch(DataAccessException e){//ServerErrorException
            response.put("message","Server error");
            response.put("error",e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(clientRequest, HttpStatus.OK);
    }


    @RequestMapping(value = "/uploadImage")
    public  ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws FileNotFoundException {

        Map<String,Object> response = new HashMap<>();
        try {

            //send file to server using TFTP
            TFTPClient tftpClient = new TFTPClient();
            saveImage(file);
            tftpClient.executeClient("127.0.0.1", userName+"_"+file.getOriginalFilename(), "octet", "W");

            //delete file
            File image = new File(path+userName+"_"+file.getOriginalFilename());
            FileUtils.forceDelete(image);

        } catch (IOException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<Map<String, Object>>( response, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/receiveImage")
    public  ResponseEntity<?> receiveImage() throws FileNotFoundException {

        userName="pepe";
        Map<String,Object> response = new HashMap<>();
        //try {

            //send file to server using TFTP
            TFTPClient tftpClient = new TFTPClient();

            //tftpClient.executeClient("127.0.0.1","server.jpg","octet", "R");
            //+"\\"+"prueba2.jpg"
            tftpClient.executeClient("127.0.0.1",userName,"octet", "R");

            //delete file
            /*File images = new File(path+userName);
            FileUtils.forceDelete(images);

        } catch (IOException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_MODIFIED);
        }*/

        return new ResponseEntity<Map<String, Object>>( response, HttpStatus.CREATED);
    }





    public void saveImage(MultipartFile file)  {

        File image = new File(path+userName+"_"+file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(image)) {
            InputStream initialStream = file.getInputStream();
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);
            os.write(buffer);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }



    }


}
