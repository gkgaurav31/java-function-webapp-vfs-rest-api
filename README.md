# Sample App

This is a simple Java Function to call the [VFS REST API](https://github.com/projectkudu/kudu/wiki/REST-API#vfs) on Azure WebApp.

## TO TEST

- Update the username and password in Function.java
- Update the file that needs to be deleted
- Execute ```mvn clean package -DskipTests```
- Execute ```mvn azure-functions:run```
  
```curl
curl http://localhost:7071/api/HttpExample
```

__NOTE:__ This is a sample app and will not be maintained.
