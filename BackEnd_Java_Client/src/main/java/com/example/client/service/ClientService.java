package com.example.client.service;

import com.example.client.model.Client;
import com.example.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository repository;


    public void save(Client client){
       repository.SaveClient(client.getName(), client.getPassword());

    }
    public Client login(Client client) {
        return repository.Login(client.getName(),client.getPassword());
    }
    public List<Client> listAll(){return repository.findAll();}
    public Client get (int id){ return repository.findById(id).get();}
    public void delete(int id){repository.deleteById(id);}


}
