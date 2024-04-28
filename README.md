# Goal 

A very common use case for generative AI is the creation of organization-specific domains of knowledge. The general idea is to store the data in a vector database and use the RAG approach to query the data. Once you have the data, use a large language model to process the result and format it into polite and human-readable text. 

The challenging part is: how exactly do we get all sources of information? Some data is located in the SQL database, some in the NoSql database, some in CRM, like Saleforce, and some in ERP, like SAP.

The goal is to have the ability to quickly connect to the sources, extract the data, optionally enrich the data, and then store it in a vector database. 

# Approach

The ability to integrate data from different sources has existed for years, and it even has its own name for the Saas offering: Ipaas (Integration Platform as a Service). Another service is Amazon AppFlow, which has built-in connectors to other systems and, more importantly, the ability to write custom connectors if you need one.
This is exactly what we need. Get data from different sources with the ability to define customization.

# Architecture
<img width="897" alt="image" src="https://github.com/MichaelShapira/ConfluenceForAwsAppFlow/assets/135519473/d572b1f1-98b9-4e05-ac25-12318dea0eb1">

1. Use Amazon AppFlow to connect to external sources.

2. Store the data in Amazon S3.

2A. Optionally, query the data and enrich it with additional data.

3. Store the final data from S3 into one of the vactor databases of Amazon Bedrock Knowledgebases.

5. Query the data from the Amazon Bedrock Knowledgebase and return the result to the user.

# Application

This is a custom connector for Amazon AppFlow. This connector can work with the Confluence Cloud. Note that it was created for POC purposes only and provided as is. 

Only the "Blog" entity of Confunece is supported. 

The authentication is based on a username and token. 

Based on this AWS blog : https://aws.amazon.com/blogs/compute/building-custom-connectors-using-the-amazon-appflow-custom-connector-sdk/ 

The deployment will deploy a new Lambda function. Lambda represents the custom connector for the Confluence cloud. Modify its policy for secret managers before deploying (template.yaml) file

## Prerequisites
- Java 1.8+
- Apache Maven
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
- Docker


#### Building the project
```
mvn clean compile assembly:single
```

## Deployment

The generated project contains a default [SAM template](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html) file `template.yaml` where you can 
configure different properties of your lambda function such as memory size and timeout. You might also need to add specific policies to the lambda function
so that it can access other AWS resources.

To deploy the application, you can run the following command:

```
sam deploy --guided
```

See [Deploying Serverless Applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html) for more info.

# Demo

I created a new blog entry in Confluence Cloud. The blog contains a textual version of the famous Moby Dick book.

<img width="1269" alt="image" src="https://github.com/MichaelShapira/ConfluenceForAwsAppFlow/assets/135519473/f9c3246c-63a2-480e-a3d1-0179061f5400">

I created the connection on Amazon AppFlow by using the connector from this repository and defined the process that moves the data from the Confluence blog entity to Amazon S3.

<img width="1345" alt="image" src="https://github.com/MichaelShapira/ConfluenceForAwsAppFlow/assets/135519473/154fd31c-4e3e-4675-9dc3-d462c1279946">

I created the knowledge base in Amazon Bedrock based on the OpenSearch database and syncronized the data from S3 into the database.

<img width="1184" alt="image" src="https://github.com/MichaelShapira/ConfluenceForAwsAppFlow/assets/135519473/4d5f80d4-cf39-48fa-ae35-03727b2ba119">

Lastly, I tested the data by running the Amazon Bedrock Knowledge Base API. The advantage of this API is the ability to use a single API for all underlying databases. You don't need to know SQL for Aurora or OpenSearch query language.
The very big advantage of this API is its ability to preserve context. All you need to do is get the session ID on the first request and pass it on to all subsequent requests.


```
import boto3
import pprint
from botocore.client import Config

pp = pprint.PrettyPrinter(indent=2)

bedrock_config = Config(connect_timeout=120, read_timeout=120, retries={'max_attempts': 0})
bedrock_client = boto3.client('bedrock-runtime')
bedrock_agent_client = boto3.client("bedrock-agent-runtime",
                              config=bedrock_config)

model_id = "anthropic.claude-3-sonnet-20240229-v1:0" # try with both claude instant as well as claude-v2. for claude v2 - "anthropic.claude-v2"
region_id = "us-east-1" # replace it with the region you're running sagemaker notebook
kb_id = "XXXXXXXXX" # replace it with the Knowledge base id.
```

Note that you need to use knowledge base ID that you own

```
def retrieveAndGenerate(input, kbId, sessionId=None, model_id = "anthropic.claude-3-sonnet-20240229-v1:0", region_id = "us-east-1"):
    model_arn = f'arn:aws:bedrock:{region_id}::foundation-model/{model_id}'
    if sessionId:
        return bedrock_agent_client.retrieve_and_generate(
            input={
                'text': input
            },
            retrieveAndGenerateConfiguration={
                'type': 'KNOWLEDGE_BASE',
                'knowledgeBaseConfiguration': {
                    'knowledgeBaseId': kbId,
                    'modelArn': model_arn
                }
            },
            sessionId=sessionId
        )
    else:
        return bedrock_agent_client.retrieve_and_generate(
            input={
                'text': input
            },
            retrieveAndGenerateConfiguration={
                'type': 'KNOWLEDGE_BASE',
                'knowledgeBaseConfiguration': {
                    'knowledgeBaseId': kbId,
                    'modelArn': model_arn
                }
            }
        )
```

This is the function that queries the knowledge bases and reuses the session ID.

<img width="861" alt="image" src="https://github.com/MichaelShapira/ConfluenceForAwsAppFlow/assets/135519473/feb3ba23-5df1-45c8-80a7-b2cf9bef67ff">

Below is an example of querying the knowledge base with subsequent questions.


