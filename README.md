
# 21-IF500-TFTP
*** 
This project is based on the operation of the TFTP protocol.
The client sends images to the server by means of random byte packets
the server receives them, sends a confirmation and rebuilds the image.
The image is stored on the server side in a folder with the client's name. 
If the client needs to see the saved images, in this case the server does 
the sending in the same way.
*** 
### Application functionalities:

**Angular cli (FrontEnd_Angular_Client):** 

- Allows to verify customer data
- Register new client
- Save images
- To list images

**Springboot Api (BackEnd_Java_Client):**

- Allows save clients in the SQL data base
- Communicates with the server to send and receive images


**Java application (BackEnd_Java_Server):**

- It receives the client's requests which are to send and receive images.


**SQL (DataBase):**

- Stores customer data.


*** 
### Execution requirements:

- Save proyect in "C:" disk.
- SQL Management Studio 15.0.18369.0
- IntelliJ IDEA 2020.3.3 
- Configured a server (Tomcat preferably)
- Environment variables configured for Java (jre1.8.0_231 y jdk1.8.0_231)
- Java configured jdk1.8.0_261
- Angular cli 12.0.1
- Visual Studio Code 1.56.2


*** 
### To run proyect:

**SQL:**

- Open SQL Management Studio and execute ScriptDB.sql file


**Springboot Api (BackEnd_Java_Client):**

- Open proyect using IntelliJ IDEA 2020.3.3 
- Run using the server Tomcat configurations

**Java application (BackEnd_Java_Server):**

- Open proyect using IntelliJ IDEA 2020.3.3 
- Run main class

**Angular cli (FrontEnd_Angular_Client):**

- Open proyect in Visual Studio Code. Use the terminal and add modules with this commands and
the last command run the proyect:
```
$ npm install -g @angular-devkit/schematics-cli

$ npm install --save-dev @angular-devkit/build-angular

$ ng serve
```
- Open proyect in browser
- Add new client in register option
- Login with the credentials using in register
- Choose send option
- Choose see images option

***
support material used in this project: [TFTP](https://github.com/JamieMZhang/TFTP).

***
