# breizhcamp-2018-tia

## Step 1
> git clone https://github.com/gravitee-io/breizhcamp-2018-tia.git

## Step 2
> docker-compose pull

> docker-compose build

> docker-compose up

## Step 3: Declare your API
### Go to the Gravitee.io Portal: http://localhost:18080/
![Alt text](assets/screenshot1.png?raw=true "Portal")

### Login as admin:admin
![Alt text](assets/screenshot2.png?raw=true "Login")

### Click on the avatar (top right) > Administration > click on (+) buttom (bottom right)
![Alt text](assets/screenshot3.png?raw=true "Administration")

### Create an API from scratch
![Alt text](assets/screenshot4.png?raw=true "Create an API from scratch")

### Fill the form
![Alt text](assets/screenshot5.png?raw=true "Create API")

### Define the backend API
![Alt text](assets/screenshot6.png?raw=true "Define Backend")

### Create a Keyless (pass-trough) consumer plan
![Alt text](assets/screenshot7.png?raw=true "Define pass-trough plan")

### Create and deploy the API
![Alt text](assets/screenshot8.png?raw=true "Create and deploy API")

## Step 4: Configure Keycloak

Create a new realm: `brasserie`

Create a first client: `beer-service`
![Alt text](assets/screenshot9.png?raw=true "Create beer-service client")
![Alt text](assets/screenshot11.png?raw=true "Configure beer-service client")

Create a `resource` client for the Gravitee Gateway: `gravitee`
![Alt text](assets/screenshot10.png?raw=true "Create gravitee resource")
![Alt text](assets/screenshot12.png?raw=true "Configure gravitee resource")

Get the Keycloak OID configuration file and create a Keycloak Resource in Gravitee
![Alt text](assets/screenshot13.png?raw=true "Get resource configuration")
![Alt text](assets/screenshot14.png?raw=true "Create keycloak resource")