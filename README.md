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

## API Usage Examples

### Succes case

Let's call the API to execute a simple javascript declaration of a variable :

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
The result is expected to be empty as we are declaring a variable. The sessionId can be used in the next request to reload the previous state of the interpreter as shown bellow :

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

The result field contains the value of the declared variable in the first request.

### Failure cases

#### Invalid input request : 

Let's call the API with wrong or empty code field : 

```
{
	"code":"%js"
}
```
```
{
    "error": "code should respect the following format : %<interpreter-name><whitespace><code>"
}
```

#### Unknown interpreter : 

Let's call the API using a language that is not supported by the application : 

```
{
	"code":"%php $a=2"
}
```
```
{
    "error": "A language with id \"php\" is not supported. Supported languages are [R, python, llvm, js, sql]."
}
```

#### Unsupported operation : 

Let's call the API to execute an Sql query : 

```
{
	"code":"%sql select * from tab"
}
```
```
{
    "error": "Sql interpretor not yet implemented"
}
```

#### Concurrent acces : 

Let's call the API with two parallel requests (A and B) using the same sessionId and executing different instructions : 

- Request A :

```
{
	"code":"%js a=3;for(i=1;i<3;i++){a=a+i};print(a);",
	"sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```
```
{
    "result": "6\n",
    "sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```

- Request B :

```
{
	"code":"%js a=0;print(a);",
	"sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```
```
{
    "error": "Concurrent access with the same sessionId 2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e.",
    "sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```

#### Execution times out : 

Let's call the API to execute an infinite loop : 

```
{
	"code":"%js while(true);",
	"sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```
```
{
    "error": "Execution request taking more than 5,000(ms).",
    "sessionId": "2af0395f-4c77-4c8b-a0bc-b6bad9afbc4e"
}
```

### Built With

* [Spring boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)