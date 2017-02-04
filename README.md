# Sentinel Eater
Service for automatic download of Sentinel data.

## Settings
Crate file `~/.aws/credentials` with your acess and secret keys:
```
[default]
aws_access_key_id = ACCESS_KEY
aws_secret_access_key = SECRET_KEY
```

Or append command line parameters before every run:
```
--cloud.aws.credentials.accessKey=ACCESS_KEY --cloud.aws.credentials.secretKey=SECRET_KEY 
```
## How to build
```sh
./mvnw package
```
## How to run
```
java -jar target/eater.jar
```
