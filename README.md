# Sentinel Eater
![Sentinel](https://openclipart.org/image/200px/svg_to_png/196091/satellite-icon.png)

Service for automatic download of Sentinel data.

## Settings
Set your amazon sdk credentials as in [SDK credentials configuration](http://cloud.spring.io/spring-cloud-aws/spring-cloud-aws.html#_sdk_credentials_configuration)

The easiest method is to create a credentials file `~/.aws/credentials` with your access and secret keys:
```
[default]
aws_access_key_id = ACCESS_KEY
aws_secret_access_key = SECRET_KEY
```
## How to build
```sh
./mvnw package
```
## How to run
```
java -jar target/eater.jar
```
