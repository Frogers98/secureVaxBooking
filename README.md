# secureVaxBooking
A Spring Boot App to facilitate appointment booking, engineered to be secure against common exploits and infiltration techniques.

Finnian -25% - Forum Component of application

Jonathan -25% - Booking Component of application

Hassan -25% - Home page, Graphs Component and style of web application.

Eugene -25% - Security components of web app and authentication/authorisation


Application can be run by running the application class. 
Please ensure all dependencies in the pom file are resolved before running
Create a database with name ``hse`` manually before running the application.

Update the application.properties file to contain your username and password copy from below:
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
```
Any accounts created through the app will be regular accounts, but an admin account is created by the application on the backend the first time it runs. This can be accessed by logging in with <alex@admin.com> with the password 'password', to access the admin functionalities of the app.

Vaccine bookings can be accessed from the "Book Your Vaccine" button on the homepage or from the navbar.
Forum can be accessed from the "Be Stronger & Ask Anything" button on the homepage or from the navbar.
![app screenshot](https://user-images.githubusercontent.com/71187088/156617443-78173107-a35b-4118-a6ba-3eea99fd1096.png)