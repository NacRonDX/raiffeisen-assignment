# Payment Processing Service

## Project Overview
This project is a payment processing application built with Java and Spring Boot. It provides RESTful APIs for managing payments, including creating, retrieving, and deleting payment records. The application uses JWT for security and role-based access control.

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.13.0 or higher
- Docker

### Clone the Repository
```sh
git clone https://github.com/NacRonDX/raiffeisen-assignment.git
cd raiffeisen-assignment
```

### Configure the Application
Update the `src/main/resources/application-dev.yaml` file with your specific configurations if needed.

### Build the Application
```sh
mvn clean install
```

## Running the Application

### Locally
1. Navigate to the project directory:
    ```sh
    cd raiffeisen-assignment
    ```

2. Run the application:
    ```sh
    mvn spring-boot:run
    ```

3. The application will start on `http://localhost:8080`.

4. You'll also need the keycloak server running on `http://localhost:8081` to authenticate the user. You can start the keycloak server by running the following command:
    ```sh
    docker run keycloak -p 8081:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -e KC_PROXY=edge -e KEYCLOAK_IMPORT=dev-realm.json start-dev --import-realm
   ```

### Via Docker Compose
1. Build and run the Docker containers:
    ```sh
    docker-compose up --build
    ```

2. The application will be accessible on `http://localhost:8080`.

## API Endpoints
- `GET /payments`: Retrieve a list of payments (requires `ROLE_VIEWER`).
- `POST /payments`: Create a new payment (requires `ROLE_EDITOR`).
- `DELETE /payments/{id}`: Delete a payment by ID (requires `ROLE_EDITOR`).

## Security
The application uses JWT for authentication. Ensure your JWT issuer URI is correctly configured in the `application-dev.yaml` file.

## Future Improvements
- Configure CORS.
- Add a correlation ID for tracking requests.
- Manage secrets securely.
- Generate openapi specification for the API.
- Configure optimistic locking for concurrent updates.
- Find usages for the payment_audit table.