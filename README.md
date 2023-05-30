# Metric Conversion System

This is an application that perform conversion of Metric to Imperial units and Imperial to Metric units.

## Technologies

Project is created with:
* Java 17
* SpringBoot v3.1.0
* PostgreSQL database

![springboot-badge](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot "SpringBoot")
![postgresql-badge](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white "PostgreSQL")
![docker-badge](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white "Docker")

# Running Locally

1. Start docker desktop on local machine.
2. Create a server and postgres database with the credentials below.
       Username: postgres
       Password: password
3. Open the docker folder in the project and run the docker-compose.yml file to start dependency stacks for the mirco service. Alternatively navigate to the docker folder and run the command `docker-compose up`
4. There are several ways to run a Spring Boot application on your local machine.

    * One way is to execute the `main` method in the main class `src/main/java/com/technical/assessment/metricconversionsystem/MetricConversionSystemApplication.java` from your IDE.

    * Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) as follows:

        ```bash
        # Go to the project directory
        $  cd metricconversionsystem
     
        # Execute the springboot maven command
        $ mvn spring-boot:run
        ```
5. Upon running the application, there is pre-configured data that will be loaded into the database to aid with testing as a starting point.

# Local Testing

- To test the APIs, load the postman collection file named `MetricConversionSystemAPIs.postman_collection.json` located in the project resources folder, into Postman application and call the provided endpoints

