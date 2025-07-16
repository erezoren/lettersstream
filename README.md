# Letters Stream Service

A simple Spring Boot REST API for streaming and matching letters against a predefined word.  
The service demonstrates event-driven design using Spring's event system.
it uses a sliding window approach to check if a letter is a subsequence of the set word.

## Answer to question 5

To support multiple instances of this microservice the following action should be taken:

1. **Statelessness:**  
   Service must be stateless and not store `word` data in memory. Instead, we can use an external store (Redis, database, External configuration) for shared word.
   All instances should read/write from this store to stay in sync, and consistency is a must, hence writing will be transactional and isolated, or at least use Read after
   Write to verify the update is done.

2. **Event Handling:**  
   Instead of `WordChangedEvent` which notifies the service of a word change, we will use a distributed event broker(e.g., Kafka, RabbitMQ) so all instances receive updates.

3. **Load Balancing:**  
   All pods will be behind a load balancer to distribute requests evenly across instances.

4. **Security:**  
   Implement security measures to protect the API endpoints, such as authentication and authorization.

5. **Monitoring and Logging:**  
   Integrate monitoring and logging to track the instamces health and performance across instances (e.g., using Spring Actuator, Prometheus, Grafana).

## Endpoints

### 1. Set the Word

**`GET /v1/word/{word}`**

- **Description:** Sets the word to be used for letter matching.
- **Request:**
    - Path variable: `word` (string, required)
- **Response:**
    - `200 OK` with a success message if the word is set.
    - `400 Bad Request` if the word is missing or empty.

**_Note: You must set a word using this endpoint before using the letter matching endpoint!_**

---

### 2. Match a Letter

**`POST /v1/letters`**

- **Description:** Checks if the provided letter is a subsequence of the set word.
- **Request:**
    - Body: Plain text, a single letter (e.g., `a`)
- **Response:**
    - `200 OK` with a JSON body indicating if the letter matches.
    - `500 Internal Server Error` if:
        - No word has been set.
        - The letter is missing.
        - The input is not a single alphabetic character.

**Example Response:**

```json
{
  "matching": true
}
```

or

```json
{
  "matching": false,
  "reason": "No word has been set"
}
```

---

## Event Listener

The service includes a listener (`WordChangeListener`) that listens for `WordChangedEvent` events.  
Whenever the word is changed via the `/v1/word/{word}` endpoint, a `WordChangedEvent` is published.  
The listener reacts to this event and updates the internal state of the letter matching logic accordingly.

---

## Usage Flow

1. **Set a word** using `GET /v1/word/{word}`.
2. **Send letters** to `POST /v1/letters` to check if they match the set word.
3. The service will only process letters if a word has been set first.

---
Here’s how to add the requested sections to your `README.md`:

---

## Running the Application

### With Maven

You are correct. To build the application JAR before running or creating a Docker image, add the `mvn clean package` step.

Add this to the "Running the Application" section:

---

### Build with Maven

To build the application JAR:

```sh
mvn clean package
```

This will create a JAR file in the `target` directory.

---

You can then run the JAR with:

```sh
java -jar target/letters-stream-service.jar
```

### With IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Locate the main class (usually `LettersStreamApplication.java`).
3. Right-click the file and select `Run 'LettersStreamApplication.main()'`.
4. The application will start on port 8080 by default.

---

## Running with Docker

1. Build the Docker image:

```sh
docker build -t letters-stream-service .
```

2. Run the container:

```sh
docker run -p 8080:8080 letters-stream-service
```

The service will be available at `http://localhost:8080`.

## Technologies

- Java
- Spring Boot
- REST API
- Event-driven architecture (Spring Events)

---

Add a section to your `README.md` describing the React webapp, its location, and how to run/build it. Here’s a concise addition:

---

<h2 style="background: linear-gradient(90deg, #ff6a00, #ee0979, #50c9c3); -webkit-background-clip: text; color: transparent; background-clip: text;">
  Bonus - Web Application (React)
</h2>
The `letter-web-app` directory contains a React frontend for interacting with the Letters Stream Service API.

### Running Locally

1. Navigate to the frontend directory:
   ```sh
   cd letter-web-app
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm start
   ```
   The app will be available at [http://localhost:3000](http://localhost:3000).

### Building for Production

To build the static files for production:

```sh
npm run build
```

The output will be in the `letter-web-app/build` directory.

### Docker Integration

When building the Docker image for the backend, the React app is automatically built and served by the Spring Boot application
at [http://localhost:8080](http://localhost:8080).

---

## License

MIT (or your chosen license)