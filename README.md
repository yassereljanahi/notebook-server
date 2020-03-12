# Notebook Server

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

Once the project is cloned from the repository, build and run the application : 

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
POST /execute
```

### API request body

The API JSON request body : 

```
{
  "code": "string",
  "sessionId": "string"
}
```

- code : instruction to be executed by the API. It should respect the following format
```
%<interpreter-name><whitespace><code>
```
- sessionId : interpreter session id.


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
- sessionId : interpreter session id.
- Http status code : 
	- 200 (OK): returned in case of succes.
	- 400 (BAD_REQUEST): returned in case of interpretation error or unsupported interpreter.
	- 408 (REQUEST_TIMEOUT): returned in case of execution times out.
	- 409 (CONFLICT): returned in case of concurrent acces using the same sessionId.
	- 422 (UNPROCESSABLE_ENTITY): returned in case of invalid request body.
	- 500 (INTERNAL_SERVER_ERROR): returned in case of any unexpected error.
	- 501 (NOT_IMPLEMENTED): returned in case of unimplemented interpreter.

### API Usage Examples

#### Succes case

Request body : 

```
{
	"code":"%js a=2"
}
```

Response body : 

```
{
    "result": "",
    "sessionId": "43043fad-a3b1-4477-9303-7091b85f57cf"
}
```
The sessionId can be used in the next request to reload previous state of the interpreter :

```
{
	"code":"%js print(a)",
	"sessionId": "43043fad-a3b1-4477-9303-7091b85f57cf"
}
```

```
{
    "result": "2\n",
    "sessionId": "43043fad-a3b1-4477-9303-7091b85f57cf"
}
```

#### Failure cases



### Built With

* [Spring boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)