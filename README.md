# Sentinel Eater
![Sentinel](https://openclipart.org/image/200px/svg_to_png/196091/satellite-icon.png)

![Travis Build](https://travis-ci.org/LinkedList/sentinel-eater.svg?branch=master)

(Micro) Service for automatic download of Sentinel data.

## Capabilities

- [x] Downloading tiles to FS
- [x] Transforming from lat/long to UTM code
- [x] Search by UTM/year/month/day through tiles
- [x] Extracting info about concrete tile (e.g. cloudiness)
- [x] Caching available dates to local DB
- [ ] Search by parameters (cloudiness, intersecting geometry etc.)
- [ ] Continuous checking of desired tiles for new updates (SNS notifications, cron polling)
- [ ] API for actually doing all these things :smile:
- [ ] (Optional) Downloading tiles to supplied S3 bucket

## Settings
Sentinel Eater can run in two modes currently HTTP and Amazon.

HTTP mode uses plain http for downloading and listing tiles and is the default. Amazon uses Amazon SDK and needs credentials to work. For now
there is no difference in them, but in the future amazon mode will allow for tiles to be downloaded straight to your own S3 bucket, or be sent 
right to lambda function for processing.

If you want to use the amazon mode, please set your amazon sdk credentials as in [SDK credentials configuration](http://cloud.spring.io/spring-cloud-aws/spring-cloud-aws.html#_sdk_credentials_configuration)

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
