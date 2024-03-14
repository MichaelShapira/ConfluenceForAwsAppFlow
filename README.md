# App

This is a custom connector for Amazon AppFlow. This connector can work with the Confluence Cloud. Note that it was created for POC purposes only and provided as is. 

Only the "Blog" entity is supported. 

The authentication is based on a username and token. 

Based on this AWS blog : https://aws.amazon.com/blogs/compute/building-custom-connectors-using-the-amazon-appflow-custom-connector-sdk/ 

## Prerequisites
- Java 1.8+
- Apache Maven
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
- Docker


#### Building the project
```
mvn clean compile assembly:single
```



#### Adding more SDK clients
To add more service clients, you need to add the specific services modules in `pom.xml` and create the clients in `DependencyFactory` following the same 
pattern as appflowClient.

## Deployment

The generated project contains a default [SAM template](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html) file `template.yaml` where you can 
configure different properties of your lambda function such as memory size and timeout. You might also need to add specific policies to the lambda function
so that it can access other AWS resources.

To deploy the application, you can run the following command:

```
sam deploy --guided
```

See [Deploying Serverless Applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) for more info.



