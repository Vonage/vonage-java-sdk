# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [2.0] - In Development
### Added
- Nexmo Voice API
- Automated testing and code quality with Travis CI, Codecov and Codacy
- Publishing to Maven Central

### Changed
- Migrate to Gradle
- Internal refactoring for unit testing

## [1.5] - 2014-08-28
### Added
- Support for USSD submission

### Changed
- Include LICENSE.txt file and license in all source files
- Updated signature generation/verification in line with updates to Nexmo Service
- Include the 'network' field in the SmsSubmissionResult object

## [1.4] - 2012-10-30
### Changed
- Migrate to apache httpclient 4.1
- Switch from use of username/password to 'api_key' / 'api_secret'

## [1.3] - 2012-04-17
### Added
- Add support for 'signed' requests (using `NexmoSmsClientSignedRequests`)
- Message submissions can now specify a protocol-id value where required
- Add support for supplying a ValidityPeriod along with a Message when submitting a request
- Add support for performing reachability checks if the feature is enabled on your account
- Message submissions can now specify a message-class using the enum `com.nexmo.messaging.sdk.messages.parameters.MessageClass`
- Add support for supplying a network code to override the Nexmo automatic network detection (Feature must be enabled on your account)
- Adding constants for all current response codes
- Adding 'destination' and 'reachability' values to response object

### Changed
- Correcting some comments and log statements
- Fix some small things reported by findbugs
- Re-factor NexmoSmsClient. There are now sub-classes of this client class depending on the level of security required
    -    `com.nexmo.messaging.sdk.NexmoSmsClient`: The default client
    -    `com.nexmo.messaging.sdk.NexmoSmsClientSSL`: Uses SSL / HTTPS to encrypt the requests
    -    `com.nexmo.messaging.sdk.NexmoSmsClientSignedRequests`: Signs the requests using the secret key assigned to your account
    -    `com.nexmo.messaging.sdk.NexmoSmsClientSignedRequestsSSL`: Signs the requests and uses SSL / HTTPS to encrypt

## [1.2] - 2011-07-18
### Added
- Add https support to message submissions
- Add 'ant example-https'  example task
- Add support for 'messagePrice' in the api response
- Add reporting of remaining-balance and message-price in the example scripts

## [1.1]
### Added
- Add support for nexmo SNS service
- Ams submission - report comms-failure as an error response, not as an exception
- Ams submission - add support for 'remainingBalance' in the api response
