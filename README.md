# Goal 

A very common use case for generative AI is the creation of organization-specific domains of knowledge. The general idea is to store the data in a vector database and use the RAG approach to query the data. Once you have the data, use a large language model to process the result and format it into polite and human-readable text. 

The challenging part is: how exactly do we get all sources of information? Some data is located in the SQL database, some in the NoSql database, some in CRM, like Saleforce, and some in ERP, like SAP.

The goal is to have the ability to quickly connect to the sources, extract the data, optionally enrich the data, and then store it in a vector database. 

# Approach

The ability to integrate data from different sources has existed for years, and it even has its own name for the Saas offering: Ipaas (Integration Platform as a Service). Another service is Amazon AppFlow, which has built-in connectors to other systems and, more importantly, the ability to write custom connectors if you need one.
This is exactly what we need. Get data from different sources with the ability to define customization.

# Architecture

# Application

This is a custom connector for Amazon AppFlow. This connector can work with the Confluence Cloud. Note that it was created for POC purposes only and provided as is. 

Only the "Blog" entity is supported. 

The authentication is based on a username and token. 

Based on this AWS blog : https://aws.amazon.com/blogs/compute/building-custom-connectors-using-the-amazon-appflow-custom-connector-sdk/ 

The deployment will deploy a new Lambda function. Modify its policy for secret managers before deploying (template.yaml) file

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



