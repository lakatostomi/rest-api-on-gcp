This REST API displays statistical data about the population of the countries and areas of World.
The API is deployed on Google Cloud Platform using its APIs and Services. 

Version 1:

In the first version, the app used a file handler module that reads the data from a `.json` or `.xml`
file depends on a property value. The input data is stored in the H2 in-memory database. Only 3 endpoints have been implemented to handle requests:
 - find all with pagination,
 - find by Country Code,
 - and find by Year.

I used Google's JIB plugin to build and push the image to the Google Artifact Registry then I used Cloud Run to deploy the container and the input files were stored in a public bucket on GCP.

Version 2:

The file input was replaced by BigQuery, I have created a dataset from the data source file and the REST API queries the data on startup. The H2 in-memory database was kept on to handle pagination easier. I did not remove the file handler module...
The GCP infrastructure has changed a lot from the previous one:
 - custom VPC and Firewall rules
 - container runs on a VM instance
 - Managed VM Instance Group is created from an instance template
 - Global HTTP Load Balancer is implemented
 - Cloud Domain and DNS recordset is set up

I have configured a pipeline on GitLab that pushes the image to the GCP Artifact Registry.
The pipeline runs on the custom image I have built with Dockerfile which can be found in the `\pipeline_image` folder.
I have installed Maven and Google Cloud SDK on Linux OS.

The related Terraform code and GitLab CI pipeline configuration is available on GitLab:
https://gitlab.com/terraform_projects2/web_app_on_gcp
 