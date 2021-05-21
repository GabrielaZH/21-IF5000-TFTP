package com.example.client.model;


import com.sun.istack.Nullable;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Client")
@NamedStoredProcedureQuery(
        name = "Client.SaveClient",
        procedureName = "SaveClient",
        parameters = {
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "name",
                        type = String.class),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "password",
                        type = String.class)})
@NamedStoredProcedureQuery(
        name = "Client.Login",
        procedureName = "Login",
        parameters = {
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "name",
                        type = String.class),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "password",
                        type = String.class)})
public class Client {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;
    private String name;
    private String password;

    public Client(int clientId, String name,  String password) {
        this.clientId= clientId;
        this.name = name;
        this.password = password;

    }

    public Client() {
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
