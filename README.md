# Sentinel Eater
![Sentinel](https://openclipart.org/image/200px/svg_to_png/196091/satellite-icon.png)

![Travis Build](https://travis-ci.org/LinkedList/sentinel-eater.svg?branch=master)
[![codecov](https://codecov.io/gh/LinkedList/sentinel-eater/branch/master/graph/badge.svg)](https://codecov.io/gh/LinkedList/sentinel-eater)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8e4cd7df22ed469492cf5aa0399e559b)](https://www.codacy.com/app/LinkedList/sentinel-eater?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LinkedList/sentinel-eater&amp;utm_campaign=Badge_Grade)

(Micro) Service for automatic download of Sentinel data.

## Capabilities

- [x] Downloading tiles to FS
- [x] Transforming from lat/long to UTM code
- [x] Search by UTM/year/month/day through tiles
- [x] Extracting info about concrete tile (e.g. cloudiness)
- [x] Caching available dates to local DB
- [ ] Search by parameters (cloudiness, intersecting geometry etc.)
- [x] Continuous checking of desired tiles for new updates (cron polling)
- [ ] API for actually doing all these things :smile:
- [ ] (Optional) Downloading tiles to supplied S3 bucket
- [ ] (Optional) Respond to SNS notifications for continuous checking of desired tiles

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
