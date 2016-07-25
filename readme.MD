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

### Bundle plugin setup

In order to use (share) resources among other projects (JSON and properties file) you need to configure maven plugin.

```
<build>
  	<resources>            
            <resource>
                <directory>${basedir}/src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-remote-resources-plugin</artifactId>
                <version>1.5</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>bundle</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <resourcesDirectory>${basedir}/src/main/resources</resourcesDirectory>
                    <includes>
                        <include>**/*</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>
  </build>
```

This plugin makes a _bundle_ with all files and folders that are in `src/main/resources/` folder.

### Build

Build is done using simple maven command:

`mvn clean install`

it will execute tests and create .jar file.

## How to use

`JAR` file that is generated can be used:

* locally (if `mvn clean install` command is execute in machine, then output jar file is stored in local `.m2` folder and it can be reused as dependency)
* globally - if it is stored in hosted maven repository

In either way, you must configure _remote-resources_ plugin on target project.

```
<build>
  <plugins>
  <plugin>        
    <artifactId>maven-remote-resources-plugin</artifactId>
    <version>1.5</version>
    <executions>
        <execution>
            <goals>
                <goal>process</goal>
            </goals>
            <configuration>
                <resourceBundles>
                    <resourceBundle>lukastosic.mocks:MockService:0.2.3</resourceBundle>
                </resourceBundles>
            </configuration>
        </execution>
    </executions>
</plugin>
</plugins>
  </build>
```

This will place everything that was bundled in mocservice project (`scr/main/resources`) and it will place it under `target/maven-shared-archive-resources` (also it will be placed inside `target/classes`).

So in order to startup mock server we must provide it with path to `target/maven-shared-archive-resources` to find properties and JSON files.

That is why in MockService method `LoadMappingOptions(boolean defaultPluginUsage)` has that boolean parameter. If set to true - mock server will look into that folder.

### How to initialize mock in JAVA code (JUnit)

```
MockService mockService = new MockService();

// Strtup mock server
mockService.StartMockServer();

// Load properties file:
mockService.LoadMappingOptions(true);

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

Default method `LoadMappingOptions(true)` will use default properties file location and default root folder for mocking definitions.

If you want to use different options you can use:

```
String fileProperties = "target/maven-shared-archive-resources/some_other_path";
	
String mappingRootFolder = "target/maven-shared-archive-resources/some_other_path";

mockService.LoadMappingOptions(fileProperties,mappingRootFolder);
```


## Code created with

Code is created using:

* Eclipse
* Java JDK 1.8
* Maven 3.3
* SureFire for UnitTests
* WireMock library