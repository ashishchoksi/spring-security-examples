package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
Documentation to create spring username password security with DB

we are going use DB based username password spring boot auth login
since we are dealing with DB we need few details
-> Entity (DB table), repository for DB operation, service (to call repository)
-> to levarage spring security we need to implement few interfaces from spring

steps:
- add required dependencies
- add DB config if needed
- create entity class that impl UserDetails
- create repository to fetch user by userName
- create service that impl UserDetailService - it's main connection points, spring filters eventually call this service
- create controller to register user
- create @Bean for password encoder -> to save password in encode form
- create @Bean for securityFilterChain to permit /register endpoint to all

Now test register user with user-name, pwd and make other api call with basic auth same user-name, pwd
 */
@SpringBootApplication
public class DBUsernamePasswordSecurity {
    public static void main(String[] args) {
        SpringApplication.run(DBUsernamePasswordSecurity.class, args);
    }
}