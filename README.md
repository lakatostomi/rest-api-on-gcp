This REST API displays statistical data about the population of the countries and areas of World.
The API is deployed on Google Cloud Platform using its APIs and Services. 

Version 1:

In the first version the app has used a file handler module which reads the data from .json or .xml
file depends on a property value. The input data is stored in H2 inMemoryDatabase. Only 3 endpoint has been implemented to handle requests:
 - find all with pagination,
 - find by Country Code,
 - and find by Year.

I used Google's JIB plugin to build and push the image to Google Container Registry than I used Cloud Run to deploy the container and the input files were stored in a public bucket on GCP.

Version 2:

The file input was replaced by BigQuery, I have created a dataset from the .json file and the REST API queries the data on startup. H2 inMemoryDatabase was kept on in order to handle pagination easier. I did not remove the file handler module...
The GCP infrastructure has changed a lot from the previous one:
 - custom VPC and Firewall rules
 - container runs on a VM instance
 - Managed VM Instance Group is created from instance template
 - Global HTTP Load Balancer has implemented
 - Cloud Domain and DNS recordset has been set up

I have configured a pipeline on GitLab that push the image to GCP Container Registry.

The related Terraform code and GitLab CI pipeline configuration is available on GitLab:
https://gitlab.com/terraform_projects2/web_app_on_gcp

In this project I was focusing on to successfully set up a GCP infrastructure with Terraform, not the REST API itself! 
 