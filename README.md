# secureVaxBooking
A Spring Boot App to facilitate appointment booking, engineered to be secure against common exploits and infiltration techniques.

Finnian -25% - Forum Component of application

Jonathan -25% - Booking Component of application

Hassan -25% - Home page, Graphs Component and style of web application.

Eugene -25% - Security components of web app and authentication/authorisation

Notes:

- Application can be run by running the application class. 

- Please ensure all dependencies in the pom file are resolved before running

- Create a database with name ``hse`` manually before running the application.

- Update the application.properties file to contain your username and password copy from below:
```
# if port 8080 is busy otherwise comment out
server.port=
spring.datasource.url = jdbc:mysql://localhost:3306/hse
spring.datasource.username =
spring.datasource.password =
## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.jpa.hibernate.ddl-auto = update

# enable https
server.ssl.enabled=true
# keystore format
server.ssl.key-store-type=PKCS12
# alias
server.ssl.key-alias=springboot
# keystore location
server.ssl.key-store=classpath:keystore/springboot.p12
# keystore password
server.ssl.key-store-password=password
#port for https/
server.port=8443

* # Alternatively, create your own cert with the following command and change the credentials above to correspond:
# keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 3650 -storepass password
```

- Include application.yml file, including a host, username (email address), and a password for the email address, with the following:
```
spring:
  mail:
    host: smtp.gmail.com
    username: 
    password: 
    port: 587
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
 ```
            
- Include a file named `resource.txt` in the `src/main/java/app/model` directory containing a string of 16 bytes (for instance 'power-rangers-f4'). This is used as a key source for your database encryption and decryption.

Any accounts created through the app will be regular accounts, but an admin account is created by the application on the backend the first time it runs. This can be accessed by logging in with <alex@admin.com> with the password 'password', to access the admin functionalities of the app.

Vaccine bookings can be accessed from the "Book Your Vaccine" button on the homepage or from the navbar.
Forum can be accessed from the "Be Stronger & Ask Anything" button on the homepage or from the navbar.
![Screenshot 2022-03-06 at 12-38-07 Home](https://user-images.githubusercontent.com/72608789/156923840-e55bac90-ed4d-40ec-b98a-bcd9e9dae4b9.png)
