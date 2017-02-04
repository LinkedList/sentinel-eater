# Sentinel Eater
Service for automatic download of Sentinel data.

## How to build
Package with skipTests for now
```sh
./mvnw package -DskipTests
```
## How to run
```
java -jar target/sentinel-downloader-0.0.1-SNAPSHOT.jar --cloud.aws.credentials.accessKey=ACCESS_KEY --cloud.aws.credentials.secretKey=SECRET_KEY 
```
