# Project Title

Spring boot notebook server for executing pieces of code based on a specific language. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Install [GraalVm](https://www.graalvm.org/docs/getting-started/) by following the instructions. Once the GraalVM is installed and configured, you can check the list of available languages by runing Graal Update command :

```
$ gu available
```

To install python run the following command : 

```
$ gu install python
```

### Installation

Clone the project from the repository : 

```
$ git clone https://github.com/yassereljanahi/notebook-server.git
```

### Build and Run project

Once the project cloned from the repository, Build and Run the application : 

```
$ mvn spring-boot:run

or

$ mvn clean package -DskipTests
$ java -jar ./target/notebook-0.0.1-SNAPSHOT.jar
``` 

Now the server is ready to serve requests : 

```
$ http://localhost:8080/
```

## API

The interpreter API is exposed via Http POST method : 

```
/execute
```

### API request body

The API JSON request body : 

```
{
  "code": "string",
  "sessionId": "string"
}
```

- code : instruction to be executed by the API. It should respect the following format %<interpreter-name><whitespace><code>
- sessionId : session id


### API response body

The API JSON response body :

```
{
  "result": "string",
  "error": "string",
  "sessionId": "string"
}
```

- result : the result of the interpretation.
- error : description of the error if any.
- sessionId : session id
- Http status code : 
	- 200 (OK): returned in case of succes.
	- 400 (BAD_REQUEST): returned in case of interpretation error, unsupported interpreter.
	- 408 (REQUEST_TIMEOUT): returned in case of execution times out.
	- 409 (CONFLICT): returned in case of concurrent acces using the same sessionId.
	- 422 (UNPROCESSABLE_ENTITY): returned in case of invalid request body.
	- 500 (INTERNAL_SERVER_ERROR): returned in case of any unexpected error.
	- 501 (NOT_IMPLEMENTED): returned in case of unimplemented interpreter.

### API Usage Examples



## Built With

* [Spring boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)