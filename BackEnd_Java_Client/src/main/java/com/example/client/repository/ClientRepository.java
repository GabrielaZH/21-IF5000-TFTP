package com.example.client.repository;

import com.example.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {


    @Procedure(name ="SaveClient")
    void SaveClient(@Param("name") String name,@Param("password") String password);

    @Query(value = "{ call Login(:name,:password)}", nativeQuery = true)
    Client Login(@Param("name") String name,@Param("password") String password);




}
