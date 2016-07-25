# Mocking server (based on Wiremock)

## Intro

This is universal mocking server that can contain multiple different mocking definitions.

Usage for this server is inspired by microservice development and need to have lots of mocking services.

Using this code you can setup multiple servers with their mocking rules simply by changing properties file and adding JSON properties.


## How to setup mock server

### 1. Update properties file

Properties file is located in `src/main/resources/mappings/mappings.properties`

File format is in simple key-value pair format:

```
NAME_OF_SERVICE=folder_name
```

This `folder_name` is mapped by default to folder inside `src/main/resources/mappings`

So, if you want to make mock to `AuthenticationService` you should place that name in properties file and create proper folder:

```
AuthenticationService=authentication-mappings
```

### 2. Update JSON files with definitions

As described above, in `mappings.properties` file you provide name of the service and folder name. In sample above, in properties file there is folder `authentication-mappings`

This means that you have to create folder: `src/main/resources/mappings/authentication-mappings`.

Inside that folder you can place many JSON files (namings doesn't matter). These JSON files are in format `WireMock` recognizes.

For example, file `src/main/resources/mappings/authenticaion-mappings/good-token-200.json` can have contents:

```
{
  "request": {
	"url": "/verify",
	"method": "POST",
	"headers": {
	  "Token": {
		"equalTo": "good-token"
	  }
	}
  },
  "response": {
	"status": 200
  }
}
``` 

This means that it will accept `POST` on url `/verify` with header variable `Token` that must have value `good-token`. If all of these conditions are met - it will return status 200.

You can take a look at official WireMock page for more details on how to structure your JSON files: [WireMock - stubbing](http://wiremock.org/docs/stubbing/).

## How to build

Build is done using simple maven command:

`mvn clean install`

it will execute tests and create .jar file.

## How to use

This is intended to be used as maven dependency. You can create .jar file and then place it on your internal maven repository and then reuse in different projects as maven dependency.

### How to initialize mock in JAVA code (JUnit)

```
MockService mockService = new MockService();

// Strtup mock server
mockService.StartMockServer();

// Load properties file:
mockService.LoadMappingOptions();

// Show list of possible mock services
ArrayList<String> options = mockService.ShowMappingOptions();

// Load specific mock service
mockService.SetMapping("SOME_SERVICE_NAME_FROM_PROPERTIES_FILE");

// Stop mock service
mockService.StopMockServer();
```

This default constructor of `MockService` is using dynamic port binding, which means that on run time it will randomly select some open port.

In order to get information about this port, you can use method:

```
mockService.GetHttpPort()
```

Host is always `localhost`.

### Defaults and how to override them

As mentioned above, default constructor will use dynamic ports

Default method `LoadMappingOptions()` will use default properties file location and default root folder for mocking definitions.

If you want to use different options you can use:

```
String fileProperties = "src/main/resources/mappings/mappings.properties";
	
String mappingRootFolder = "src/main/resources/mappings/";

mockService.LoadMappingOptions(fileProperties,mappingRootFolder);
```


## Code created with

Code is created using:

* Eclipse
* Java JDK 1.8
* Maven 3.3
* SureFire for UnitTests
* WireMock library