# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [3.2.0] - 2017-11-30

### Modified

- Add signature authentication support to the API endpoints which support it.

## [3.1.0] - 2017-10-24

### Added
- Add `NexmoClient.generateJwt`
- Add `VoiceClient.downloadRecording`

### Changed

- Made exceptions from `AuthCollection`'s `getAuth` and `getAcceptableAuthMethod` more specific. (Backwards compatible change)


## [3.0.0] - 2017-10-19

### Changed
- `ModifyCallPayload.action` is now a value of the enum `ModifyCallPayload.Action`.
- All calls now throw `HttpResponseException` if an abnormal HTTP status code is returned from the Nexmo API.

### Fixed
- Added missing values for CallStatus: FAILED, REJECTED, BUSY & CANCELLED

### Added
- Add missing setter for the Message.statusReportRequired property.
- Add `AccountClient.getBalance` method for getting an account balance.
- Add `NumbersClient.listNumbers` method.
- Add `NumbersClient.searchNumbers` method.
- Add `NumbersClient.cancelNumber` method.
- Add `NumbersClient.buyNumber` method.
- Add `NumbersClient.updateNumber` and `NumbersClient.linkNumber`.
- Add the ability to mute, unmute, earmuff, unearmuff and transfer an ongoing call.
- Add `VerifyClient.advanceVerification` and `VerifyClient.cancelVerification`
- Add `ApplicationClient` end methods for creating, updating, deleting, listing and getting configured applications. 
- Add extra endpoints for searching SMS messages.

## [2.0.2] - 2017-05-04
### Fixed
- All URL-encoded PUT and POST requests are now UTF-8 instead of ISO-8859-1.
- The event url is properly set on a `Call`

### Added
- Number Insight now supports the `cnam` parameter for Standard and Advanced requests.

## [2.0.1] - 2017-03-18
### Changed
- Made servlet-api an optional dependency so it isn't bundled in war files. (This
  could result in class conflicts in Tomcat.)

## [2.0.0] - 2017-03-16
### Added
- Nexmo Voice API
- Automated testing and code quality with Travis CI, Codecov and Codacy
- Publishing to Maven Central
- Complete refactoring and reorganisation of the library. All classes are now
  under the `com.nexmo.client` package.

### Changed
- New Insight API using non-deprecated endpoints.
- Migrate to Gradle
- Internal refactoring for unit testing
- Removed example code. See [nexmo-community/nexmo-java-quickstart](https://github.com/nexmo-community/nexmo-java-quickstart) instead.

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
- Add support for supplying a ValidityPeriod along with a Message when submitting a snsRequest
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
