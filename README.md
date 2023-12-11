# iti0302-2023-backend

Welcome to the official repository for the TalDate backend!

Here's a quickstart. See the [Wiki](https://github.com/Robsukas/iti0302-2023-backend/wiki) for more in-depth information.

### Quickstart

If you just want to run & test the app locally, run
```bash
docker compose up -d
```

This will start both the database and the application server (Spring Boot with Tomcat).

We recommend you to test the backend in conjunction with our [frontend](https://github.com/Robsukas/iti0302-2023-frontend), see for more in Wiki.

### Development

#### Required:

- Java JDK 17
- Docker & Docker Compose 2.22.0

#### 1. Clone the repository

HTTPS:
  ```bash
  git clone https://github.com/Robsukas/iti0302-2023-backend.git
  ```
or SSH:
  ```bash
  git clone git@github.com:Robsukas/iti0302-2023-backend.git
  ```

#### 2. Start the PostgreSQL database 

```bash
docker compose up db -d
```

#### 3. Compile and run the Spring Boot project

a) either use the provided Dockerfile:
```bash
docker build -t taldate-backend
docker run --network=host --rm taldate-backend
```

b) or compile and run locally:
```bash
cd TalDate
./gradlew bootRun
```
(`gradle.bat` if using Windows PowerShell)


c) or if using IntelliJ IDEA GUI:

- Setup JDK, Load Gradle
- Run `TalDateApplication.java`

The local server should now be up and running on `localhost:8080`.

#### 4. Test the API

Open
http://localhost:8080 in your browser: you should see a blank page. That's because of the authorization controls.

Try registering a new user:
```bash
curl http://localhost:8080/api/auth/register \
-H 'Content-Type: application/json' \
-d '{"firstName":"first","lastName":"last","email":"first.last@mail.com","password":"pass123","dateOfBirth":"2001-01-01","isGenderMale":true}' \
 -D -
```

You should receive a 200 OK response.
If you repeat the request, you should receive error 400 with the message `{"error":"Account with this email already exists."}`.

You can uncomment `.anyRequest().permitAll()` in SecurityConfig.java 

#### 5. Wind down

1. Kill the app server<br>
<kbd>Ctrl</kbd> + <kbd>C</kbd>
2. Kill the database
    ```bash
    docker compose down
    ```

### Authors:
- Robin Nook
- Uku Sõrmus
- Henry Ohak
