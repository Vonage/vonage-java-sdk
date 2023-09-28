# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

# [7.9.0] - 2023-??-??
- Added `get-full-pricing` implementation of Pricing API in `AccountClient`
- Added master API key default overloads for secret management in Account API
- Deprecated public internal request classes in Account API
- Internal refactoring of Verify v1 and Account API implementations

# [7.8.0] - 2023-09-07
- Added capability to configure request timeouts (default is 60 seconds)
- Deprecated custom HTTP client implementation setting
- Internal refactoring of Numbers, Conversion and Number Insight API implementations

# [7.7.0] - 2023-08-10
- Added Users API implementation
- Major refactoring of how endpoints are implemented internally
  - SMS, SNS, Redact, Verify v2, Subaccounts, Messages, Application have been refactored
- Introduced `Jsonable` and `QueryParams` in addition to `DynamicEndpoint` to reduce boilerplate
- Added missing fields to Application, capabilities and webhooks
- Removed `PageList` (replaced by `HalPageResponse`)
- Improved documentation for Application API implementation
- Relaxed UUID validation in `VoiceClient`

# [7.6.0] - 2023-06-30
- Added Proactive Connect API implementation
- Added Meetings API implementation
- Updated Subaccounts name & secret validation logic

# [7.5.0] - 2023-06-14
- Added Subaccounts API implementation
- Added custom PIN functionality to Verify v1
- Fixed Silent Auth action URL webhook deserialization issue

# [7.4.0] - 2023-05-18
- Added Verify v2 API implementation
- Added Advanced Machine Detection to Voice API
- Fixed VbcEndpoint NCCO
- Removed dependency on `jakarta.xml.bind`
- Made `jakarta.servlet` an optional dependency
- Deprecated all methods and classes that use `javax.servlet.HttpServletRequest`

# [7.3.0] - 2023-04-14
- Viber video message now requires setting duration and file size
- Numbers API now uses Basic auth in header rather than query params
- Made `jakarta.servlet-api` a required dependency (replacing `javax.servlet` as compileOnly)
- Use `jakarta.xml.bind-api` instead of `javax.xml.bind`
- Added `premium` and `level` fields to Start Talk request
- Refactored `TalkPayload` to use Builder pattern
- Added `vbc` and `app` endpoint types for voice calls
- Fixed incorrect serialisation of `random_from_number`
- Added Builder for constructing `Call` request
- Added validation for UUIDs, URLs and request objects in `VoiceClient`
- Made internal request classes for Voice API package-private
- Deprecated public-facing usages of `CallModifer` & `ModifyCallPayload`
- Deprecated setters on classes in Voice API in favour of builders / constructors
- `com.vonage.client.voice.WebSocketEndpoint` now uses Map for headers

# [7.2.0] - 2023-03-08
- Updates to Messages v1:
  - Added `InboundMessage` webhook class
  - Fixed timestamp format deserialization for MessageStatus (now returns an Instant)
  - Added Viber file and video message types
  - Added Action button support to Viber image and text messages 
  - Added WhatsApp Sticker message type
  - Added WhatsApp Location custom message type
  - Added WhatsApp Product custom message types
  - `client_ref` character limit increased to 100
  - Expanded valid message types to accommodate webhooks (MMS text, WhatsApp order, reply & unsupported)
- Deprecated Pay NCCO action (removed in 8.x)
- Deprecated WAPPush SMS (removed in 8.x)
- Bumped dependency versions

## [8.0.0-beta4] - 2023-02-24
- Removed Payments over the Phone NCCO
- Added SIP functionality to video API
- Added Broadcast functionality to video API
- Refactored Archives for consistency with Broadcast
- Use UUID as return type for IDs where applicable

## [8.0.0-beta3] - 2023-01-11
- Removed `WAPPush` SMS message type
- Fixed token generation (added missing claim & role as string)
- Fixed incorrect responses for `muteSession` and `muteStream` (they don't return anything)
- Improved handling of 200 / 204 responses for video endpoints which don't return anything

## [8.0.0-beta2] - 2022-12-15
- Added token generation capability to `VideoClient`
- Added varargs for `SetStreamLayoutRequest`
- `listArchives` endpoint takes as input `ListArchivesRequest` using builder pattern
- Simplified `muteSession` and `muteStream` invocation

# [7.1.1] - 2022-11-16
- Bumped Jackson version to 2.14

## [8.0.0-beta1] - 2022-11-15
- Maven artifact moved to `com.vonage:server-sdk`
- Initial implementation of [Vonage Video API](https://developer.vonage.com/api/video), with following functionality:
  - Create sessions
  - Retrieve or update stream layouts in a session
  - Create, retrieve, delete archives
  - Add / remove streams from archive
  - Change archive layout
  - Mute streams or individual participants in a session
  - Disconnect participants from a session
  - Send signal to participants in a session

## [7.1.0] - 2022-08-25
- Fixed parsing `MessageResponseException` when entity body is empty
- Added toggle for using Messages API Sandbox to `MessagesClient`
- Deprecated `request_type` in `VerifyRequest`
- Deprecated `ip_address` in `CheckRequest`
- Fixed incorrect `Content-Type` header in `VerifyEndpoint` (issue #405) and `CheckEndpoint`
- Added `network` field to `VerifyResponse`
- Added `estimated_price_messages_sent` to `CheckResponse` and `VerifyDetails`
- Updated `VerifyStatus` enum values
- Added missing documentation and validation to requests and responses in Verify API

## [7.0.0] - 2022-08-11
- Removed SMS Search API
- Deprecated Redact client
- Use `vonage-jwt-jdk:1.0.2` library instead of `nexmo-jwt-jdk:1.0.1`
- Ensure `User-Agent` is set in request headers
- Allow alphanumeric characters for SMS and MMS sender fields in Messages API
- `WhatsappRequest` sender must now be an E164 number
- Fixed incorrect restrictions on `WhatsappTemplateRequest`
  - Policy is now optional
  - Default locale is now `en`
  - Locale is now an enum rather than String
  - `parameters` is now `List<String>`
- Removed dependency on `commons-io` and `commons-lang3`
- Ensured User-Agent is set in request headers
- Added Premium text-to-speech flag in `TalkAction` NCCO
- Removed support for legacy `voiceName` parameter
- Updated SMS error status codes
- Added support for voice payments using NCCO action
- Updated NCCO classes
  - Fixed Action deserialization (issue #373)
  - Permit Object values for `WebSocketEndpoint` headers
  - Removed varargs `headers` method in `WebSocketEndpoint.Builder`
  - Made Builder constructors package-private
  - Added `headers` field to `SipEndpoint`
  - Use `java.net.URI` instead of String for URI fields
- Updated Number Insight to be consistent with API specification
  - Deprecated IP Address
  - Added missing `InsightStatus` codes
  - Added documentation to Insight Response fields
  - Moved AdvancedInsight enums to separate files
  - Added `real_time_data` to AdvancedInsightResponse

## [6.5.0] - 2022-06-20
- Added Messages v1 API implementation
- Added `get-full-pricing` endpoint
- Added `Content-Type` and `Accept` headers in requests where applicable
- Substantial internal refactoring

## [6.4.2] - 2022-04-14
- Added `call_uuid` to webhook call event.
- Bumped dependency versions

## [6.4.0] - 2021-05-28
- Adding Random From Number Feature for the Voice API, if set to `true`, the from number will be randomly selected from the pool of numbers available to the application making the call.
- adjusting operator used to check json payloads
- Adding extra parsing for top level Roaming Status in Advanced Number Insights

## [6.3.0] - 2021-05-20
- Added parsing for JSON payloads when reading inbound SMS signatures

## [6.2.0] - 2021-03-11
- Adding ContentId and EntityId to message class for DLT
- Adding Detail enum and string for certain voice webhooks

## [6.1.0] - 2020-12-17
- Adding Language and Style to the Voice Talk Action and the Talk Request
- Marking VoiceName as Deprecated

## [6.0.0] - 2020-11-16
## Added
- ASR (Automatic Speech Recognition)

### Changed
- Refactored Application v2 requests to remove duplicated code
- Deprecated Product.MESSAGE from RedactRequest. Use Product.MESSAGES instead
- Deprecated InsightClient#getStandardNumberInsight(number, country, cnam). Use InsightClient#getStandardNumberInsight(StandardInsightRequest) instead
- Deprecated InsightClient.getAdvancedNumberInsight(number, country, ipAddress, cnam). Use InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest) instead
- Deprecated InsightClient.getAdvancedNumberInsight(number, country, ipAddress). Use InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest) instead
- Deprecated public constructors and setters in VerifyRequest use VerifyRequest.Builder instead
- Deprecated MD5Util use HashUtil instead
- Removed setters from BaseRequest. Set fields in the builders of Psd2Request or VerifyRequest instead.

## [5.6.0] - 2020-11-10
### Added
- NotifyEvent structure for Notify Actions
- SHA256 hashing option

### Changed
- Changed application requests to use basic auth in header for authentication

### Fixed
- Fixed error throw when trying to log No Content responses

## [5.5.0] - 2020-09-08
- Nexmo rebranded to Vonage
### Added
- Added support for PSD2 verification

## [5.4.0] - 2020-08-20
### Added
- Added AppEndpoint support for ConnectAction.
- Added the ability to log the API response in when logger is set to debug mode.
- Added ability to override base url for:
  - Modify Call
  - Send DTMF
  - Stop Talk

## [5.3.0] - 2020-07-27
### Added
- Added support for new voice names added to the Voice API for [Text to Speech](https://developer.nexmo.com/voice/voice-api/guides/text-to-speech)

### Fixed
- Improved error handling when there are apparent networking issues

## [5.2.1] - 2019-12-18
### Added
- Added new workflows in `VerifyRequest`

## [5.2.0] - 2019-10-18
### Added
- Added support for verification workflows inside of `VerifyRequest`.
- Added support for transfer to NCCO via `VoiceClient#transferCall`.

### Fixed
- `VerifyClient` had methods that were still claiming to throw an `IOException`. This has been removed as it is not
  possible for it to be thrown.

## [5.1.0] - 2019-08-18

### Added
- Added support for setting an application id as a messages callback value in updating a number.
- Added support for specifying a number type when searching for a number to purchase.

## [5.0.0] - 2019-08-01

### Added
- Added support for updating account settings (delivery receipt URL and inbound SMS URL)
- Added support for [Async Number Insights](https://developer.nexmo.com/api/number-insight#getNumberInsightAsync) by setting the property `async` and `callback` on the `AdvancedInsightRequest` object.
- Add `ringback` support to the `onAnswer` part of the `PhoneEndpoint`.

### Changed
- Changed from Auth0 to the [Nexmo JWT Library](https://github.com/nexmo/nexmo-jwt-jdk).
- Renamed the `com.nexmo.client.applications` package to `com.nexmo.client.application`
- `ApplicationClient` now supports the [Applications v2](https://developer.nexmo.com/api/application.v2) API. This change has resulted in some backwards incompatibility.
- `NexmoClientException` is now a `RuntimeException`. The various sub client methods will still declare that it is being thrown, but it is no longer a requirement to catch the exception. Additionally, the `IOException` that was being thrown in each method has been converted to a `NexmoResponseParseException` to more accurately reflect when it is thrown. This is also an unchecked exception and catching is no longer required.
- The `NexmoUnacceptableAuthException` will now add which parameters are missing instead of which internal auth methods.

## [4.4.0] - 2019-06-10

### Added
- Added new voice 'MATTHEW' to `VoiceName` enum.

## [4.3.1] - 2019-05-24

### Fixed
- Fix UTF-8 Encoding for endpoints using JSON bodies.

## [4.3.0] - 2019-04-25

### Added
- Added `NotifyAction` for use in `NCCO`.

### Fixed
- Added header (application/json) for Redact client.
- Fixed Application Update to send json body as it is what the API now expects.

## [4.2.1] - 2019-04-02

### Fixed
- Fixed the product name in Redact API for `messages`.

## [4.2.0] - 2019-03-20

### Added
- Missing builders for `StreamAction`, `AdvancedInsightRequest`, `StandardInsightRequest`, and `BasicInsightRequest`.

### Fixed
- `AbstractMethod` will force UTF-8 charset for requests using `StringEntity`.
- Tests will now run using static builder methods for each of the classes that use builders for construction.

## [4.1.0] - 2019-03-15

### Added
- Added static `builder` methods to `Action` classes to reduce some of the verbosity around using them. You can now do `TalkAction action = TalkAction.builder("Hello World!").build();` instead of just `TalkAction action = new TalkAction.Builder("Hello World).build();`.
- Added static `builder` methods to `NexmoClient` to reduce some of the verbosity around instantiating the client.
- Added `ncco` property to the `Call` object for use in the `VoiceClient.createCall` method. You can now send an `Ncco` directly to the API instead of defining an `answer_url`.
## [4.0.1] - 2018-11-21

### Added
- `channels` property to `RecordAction` as it was unintentionally removed during the version 4 release.

## [4.0.0] - 2018-11-16

### Added
- Added `Ncco` class to wrap `Collection<Action>` and handle the serialization of NCCO json through `toJson`.
- Added the following `Builder` classes:
  - `ConnectAction.Builder`
  - `ConversationAction.Builder`
  - `InputAction.Builder`
  - `RecordAction.Builder`
  - `StreamAction.Builder`
  - `TalkAction.Builder`
- Added `PhoneEndpoint` to `com.nexmo.voice.ncco` package for use with `ConnectAction`.
- Added `WebSocketEndpoint` to `com.nexmo.voice.ncco` package for use with `ConnectAction`.
- Added `SipEndpoint` to `com.nexmo.voice.ncco` package for use with `ConnectAction`.
- Added `EventMethod` enumeration to replace the usage of strings.
- Added `EventType` enumeration and the ability to specify the `eventType` property of `ConnectAction`.
- Added `NexmoClient.Builder` to allow for fluent creation of `NexmoClient`.
- Added `getBasicNumberInsight`, `getStandardNumberInsight`, and `getAdvancedNumberInsight` to `InsightClient` which takes a respective `*InsightRequest`.
- Added `CallerIdentity` data object to be used in number insight.
- Added `Builder` to `CallsFilter` class to make filtering search results more intuative.

### Changed
- Renamed all `Ncco` classes to `Action` classes:
  - Renamed `Ncco` interface to `Action`
  - Renamed `ConnectNcco` to `ConnectAction`
  - Renamed `ConversationNcco` to `ConversationAction`
  - Renamed `InputNcco` to `InputAction`
  - Renamed `RecordNcco` to `RecordAction`
  - Renamed `StreamNcco` to `StreamAction`
  - Renamed `TalkNcco` to `TalkAction`
- All `Action` classes now must be constructed through the provided `Builder` class.
- All `Action` classes are now immutable.
- The `eventUrl` property of `ConnectAction`, `ConversationAction`, `InputAction`, and `RecordAction` is now a `Collection<String>` instead of an array.
- The `eventMethod` property of `ConnectAction`, `ConversationAction`, `InputAction`, and `RecordAction` is now an `EventMethod` enum instead of a String.
- Removed `ConnectWebSocketNcco` as it is built into `ConnectAction`.
- `VerifyClient` will now return `*Response` instead of `*Result` objects.
- Refactored `com.nexmo.client.voice.endpoints` to be part of the `com.nexmo.client.voice` package.
- Made all `*Endpoint` and `*Method` classes package scoped. Users should always go through the appropriate `Client` classes.
- Moved `AbstractMethod` to the root package.
- Removed legacy `setUri` methods from the various `VoiceClient` endpoints. This should be done through `HttpConfig`.
- Changed `BasicInsightRequest`, `StandardInsightRequest`, and `AdvancedInsightRequest` to use builders as constructor
  telescoping is clunky. Added some static factory methods to these classes to allow shortcutting through the builder for "simple" requests.
- Updated `BasicInsightEndpoint`, `StandardInsightEndpoint`, and `AdvancedInsightEndpoint` to a more restrictive scope and moved them to the `com.nexmo.client.insight` package.
- Changed `status` property of `BasicInsightResponse` to an `InsightStatus` enumeration.
- Changed `SendMessageEndpoint` from XML to JSON. It now returns a `SmsSubmissionResponse` instead of an array result.

## [3.10.0] - 2018-11-02

### Added
- Added the ability to specify the number of channels to record in `RecordNcco`

### Changed
- Update base uri to always have the version string appended to it.

## [3.9.0] - 2018-10-18
### Added
- Added missing voices to `VoiceName` enum.
- Added configuration object to be used with `HttpWrapper` to allow for customization.
- Added the ability to customize the base URI used for the various endpoints.
- Added `ConnectWebSocketNcco` to handle connecting to WebSocket endpoints similar to `ConnectNcco` to maintain backwards compatibility.
- Added `getDisplayName` method to `VoiceName` to represent the name that is used in serialization.

### Changed
- `TalkNcco` to use `VoiceName` object instead of `String`
- `HttpWrapper` now uses system properties by default.

## [3.8.0] - 2018-09-19
### Added
- Added `com.nexmo.client.incoming.MessageEvent` to assist with the deserialization of the JSON payload used for incoming messages.
- Added `com.nexmo.client.incoming.CallEvent` to assist with the deserialization of the JSON payload used for call events.
- Added `com.nexmo.client.incoming.InputEvent` to assist with the deserialization of the JSON payload used for input events.
- Added `com.nexmo.client.incoming.RecordEvent` to assist with the deserialization of the JSON payload used for record events.
- Added secret management methods to `AccountClient` in the form of the following methods:
  - `listSecrets` for listing all secrets.
  - `getSecret` for getting information on a specific secret.
  - `revokeSecret` for revoking a secret.
  - `createSecret` for creating a new secret.

### Changed
- User Agent String now includes the Java version in addition to the client version.
- `enum` classes that are used to deserialize JSON have been updated to return an `UNKNOWN` value instead of throwing an `IllegalArgumentException` when the value cannot be deserialized. These `enum`s are:
  - `RecordingFormat`
  - `MachineDetection`
  - `ModifyCallAction`
  - `CallDirection`
  - `CallStatus`
  - `RoamingDetails.RoamingStatus`
  - `AdvancedInsightResponse.PortedStatus`
  - `AdvancedInsightResponse.Validity`
  - `AdvancedInsightResponse.Reachability`

### Fixed
- Updated `StreamNcco`'s `streamUrl` to serialize into an array for use in the Voice API.

## [3.7.0] - 2018-08-10
### Added
- Added `RedactClient` and the ability to interact with the Nexmo Redact API.

### Changed
- `TalkNcco` now uses `VoiceName` internally instead of `String` for the `voiceName` property.

## [3.6.0] - 2018-07-06
### Added
- Added `getSmsPrice` to `AccountClient` for getting SMS pricing for a country.
- Added `getVoicePrice` to `AccountClient` for getting voice pricing for a country.
- Added `getPrefixPrice` to `AccountClient` for getting SMS and voice pricing for a prefix.
- Added `topUp` to `AccountClient` for topping up your account which has auto-reload enabled.
- Added `getSms` to `SmsClient` for searching for a single message by id.
- Added `ConversionClient` and the ability to interact with the Nexmo Conversion API.

## [3.5.0] - 2018-05-29
### Changed
- Updated `VerifyClient` to use the JSON endpoints instead of XML.
- Updated endpoints which are used by `VerifyClient` from public to package scope in order to encourage usage through `VerifyClient`.
- Deprecated XML version of the following endpoints:
  - `VerifyEndpoint`
  - `CheckEndpoint`
  - `SearchEndpoint`

- Deprecated the following XML results:
  - `VerifyResult` should use `VerifyResponse`
  - `CheckResult` should use `CheckResponse`
  - `SearchResult` should use `SearchVerifyResponse`

- Deprecated the following XML methods:
  - `VerifyCheckMethod`

### Added
- Added `VerifyStatus` enumeration to use for statuses coming back from the verify endpoint.
- Added `VerifyResponse`, `CheckResponse`, and `SearchVerifyResponse` for JSON responses to match other JSON using endpoints.
- Added `VerifyMethod`, `CheckMethod`, and `SearchMethod` for better segregation between endpoint and method classes.
- Add `split` attribute to the `RecordNcco` object.

### Fixed
- Updated `ConversationNcco`'s `musicOnHoldUrl` to serialize into an array for use in the Voice API.

## [3.4.1] - 2018-05-11

### Changed
- Updated Jackson Databind dependency to newer version.

## [3.4.0] - 2018-05-08

### Changed
- Modify call now works with the API again, returning `null` (because the API now returns 204 No Content)
- VerifyRequest now supports all supported parameters.
- Client-side validation for the `from` parameter's length on the `Message` class has been removed.

### Added
- Add `level` attribute to the `TalkNcco` object.

## [3.2.0] - 2017-11-30

### Changed
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