# breizhcamp-2018-tia

## Step 1
> git clone https://github.com/gravitee-io/breizhcamp-2018-tia.git

## Step 2
Pull images and run
> docker-compose pull
> docker-compose build
> docker-compose up

## Step 3: Declare your API
Go to the Gravitee.io Portal: http://localhost:18080/
![Alt text](assets/screenshot1.png?raw=true "Portal")

Login as admin:admin
![Alt text](assets/screenshot2.png?raw=true "Login")

Click on the avatar (top right) > Administration > click on (+) buttom (bottom right)
![Alt text](assets/screenshot3.png?raw=true "Administration")

Create an API from scratch
![Alt text](assets/screenshot4.png?raw=true "Create an API from scratch")

Fill the form
![Alt text](assets/screenshot5.png?raw=true "Create API")

Set the backend to `http://beerapi:8080/api/beers`
![Alt text](assets/screenshot6.png?raw=true "Define Backend")

Create a Keyless (pass-trough) consumer plan
![Alt text](assets/screenshot7.png?raw=true "Define pass-trough plan")

Create and deploy the API
![Alt text](assets/screenshot8.png?raw=true "Create and deploy API")