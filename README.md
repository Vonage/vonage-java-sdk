# Vonage Server SDK for Java

[![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.vonage/client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.vonage/client)
[![Build Status](https://github.com/vonage/vonage-java-sdk/workflows/build/badge.svg)](https://github.com/Vonage/vonage-java-sdk/actions?query=workflow%3A"Build+Java+CI")
[![codecov](https://codecov.io/gh/vonage/vonage-java-sdk/branch/master/graph/badge.svg)](https://codecov.io/gh/vonage/vonage-java-sdk)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)][license]

<img src="https://developer.nexmo.com/assets/images/Vonage_Nexmo.svg" height="48px" alt="Nexmo is now known as Vonage" />

You can use this Java Server SDK to add [Vonage APIs](https://developer.nexmo.com/api) to your application. To use this, you'll
need a Vonage account. Sign up [for free at nexmo.com][signup].

 * [Installation](#installation)
 * [Usage](#usage)
 * [Tips And Tricks](#tips-and-tricks)
 * [Contribute!](#contribute)

## Installation

To use the Java Server SDK you'll need to have [created a Vonage account][signup].

### Gradle

To install the Java Server SDK using Gradle, add the following to `build.gradle`:

```groovy
repositories {
    mavenCentral()
}
```

For Gradle 3.4 or Higher:

```groovy
dependencies {
    implementation 'com.vonage:client:5.6.0'
}
```

For older versions:

```groovy
dependencies {
    compile 'com.vonage:client:5.6.0'
}
```

### Maven

Add the following to the correct place in your project's POM file:

```xml
<dependency>
      <groupId>com.vonage</groupId>
      <artifactId>client</artifactId>
      <version>5.6.0</version>
</dependency>
```

### Build It Yourself

Alternatively you can clone the repo and build the JAR file yourself:

```bash
git clone git@github.com:vonage/vonage-java-sdk.git
gradle build
```

### Download everything in a ZIP file

**Note**: We *strongly recommend* that you use a tool that supports dependency management, such as [Gradle], [Maven],
or [Ivy]

We provide a [ZIP file for each release](https://github.com/Vonage/vonage-java-sdk/releases/),
containing the Java Server SDK JAR, along with all the dependencies. Download the file, unzip it, and add the JAR files
to your project's classpath.

[Gradle]: https://gradle.org/
[Maven]: https://maven.apache.org/
[Ivy]: http://ant.apache.org/ivy/


## Usage

* For help understanding our APIs, check out our awesome [developer portal](https://developer.nexmo.com/)
* Check the [Javadoc](http://vonage.github.io/vonage-java-sdk/) for full reference documentation.
* There are also **many useful code samples** in our [nexmo-community/nexmo-java-quickstart](https://github.com/nexmo-community/nexmo-java-quickstart) repository.

### Customize the Base URI
By default, the client will use https://api.nexmo.com, https://rest.nexmo.com, and https://sns.nexmo.com as base URIs for the various endpoints. To customize these you can instantiate `VonageClient` with an `HttpConfig` object.

`HttpConfig.Builder` has been created to assist in building this object. Usage is as follows:

```java
HttpConfig httpConfig = HttpConfig.builder()
        .apiBaseUri("https://api.example.com")
        .restBaseUri("https://rest.example.com")
        .snsBaseUri("https://sns.example.com")
        .build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

If you do not specify a property, it will take on whatever the default value is. You can also set all three with a single method:

```java
HttpConfig httpConfig = HttpConfig.builder().baseUri("http://example.com").build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

To keep the default values, you can use `HttpConfig.defaultConfig()`:

```java
HttpConfig httpConfig = HttpConfig.defaultConfig();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();;
```

You can also instantiate without the parameter:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
```

### Send an SMS

Send an SMS with the Vonage SMS API:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();

SmsSubmissionResponse responses = client.getSmsClient().submitMessage(new TextMessage(
        FROM_NUMBER,
        TO_NUMBER,
        "Hello from Vonage!"));
for (SmsSubmissionResponseMessage response : responses.getMessages()) {
    System.out.println(response);
}
```

### Make Phone Calls

The following code initiates an outbound call which then reads the user [a message](https://nexmo-community.github.io/ncco-examples/first_call_talk.json):

```java
VonageClient client = VonageClient.builder()
        .applicationId(APP_ID)
        .privateKeyPath("application_key.pem")
        .build();

Call call = new Call(to, from,
                     "https://nexmo-community.github.io/ncco-examples/first_call_talk.json");
CallEvent event = client.getVoiceClient().createCall(call);
```

After the call is answered, you can get more information about it, including
the amount it cost with:

```java
CallInfo info = client.getVoiceClient().getCallDetails(event.getUuid());
System.out.println("This cost: " + info.getPrice() + " EUR");
```

You can modify an existing call in progress, for example by hanging up on the current call:

```java
ModifyCallResponse modifyResponse = client.getVoiceClient().modifyCall(event.getUuid(), "hangup");
System.out.println(modifyResponse.getMessage());
```

While a call is in progress, you can send Dual-tone multi-frequency(DTMF) tones like so:

```java
DtmfResponse dtmfResponse = client.getVoiceClient().sendDtmf(event.getUuid(), "332393");
System.out.println("Success! " + dtmfResponse.getMessage());
```

To stream an audio file to an active call, simply use the following method:

```java
StreamResponse startStreamResponse = client.getVoiceClient().startStream(event.getUuid(), "https://nexmo-community.github.io/ncco-examples/assets/voice_api_audio_streaming.mp3");
System.out.println("Success! " + startStreamResponse.getMessage());
```

If you'd like to stop streaming an audio file to an active call, you can do so with:
```java
StreamResponse stopStreamResponse = client.getVoiceClient().stopStream(event.getUuid());
System.out.println("Alright. " + stopStreamResponse.getMessage());
```

To send a synthesized speech message to an active call, just use the following method:

```java
TalkResponse startTalkResponse = client.getVoiceClient().startTalk(event.getUuid(), "Hello World");
System.out.println("Success! " + startTalkResponse.getMessage());
```

If you'd like to stop sending a synthesized speech message to an active call, you can do so with:

```java
TalkResponse stopTalkResponse = client.getVoiceClient().stopTalk(event.getUuid());
System.out.println("Alright. " + stopTalkResponse.getMessage());
```

### Generating NCCO Responses

Our library contains a `com.vonage.client.voice.ncco` package, providing JSON-serializable objects for your NCCO webhook endpoints. 

Each of the `Action` items contain a `Builder` class that can be used for constructing various actions. These actions are then added to an `Ncco` object for serialization into JSON:

```java
TalkAction intro = TalkAction.builder("At the tone, record your response and press #.")
                        .voiceName(VoiceName.KIMBERLY)
                        .build();

TalkAction outro = TalkAction.builder("Thanks, goodbye!")
                        .voiceName(VoiceName.KIMBERLY)
                        .build();

RecordAction record = RecordAction.builder()
                        .beepStart(true)
                        .endOnKey('#')
                        .build()
                        
res.type("application/json");
return new Ncco(intro, record, outro).toJson();
```

### Make a Phone Call with an NCCO

You can combine the above examples to send an NCCO to the Voice API:

```java
The following code initiates an outbound call which then reads the user [a message](https://nexmo-community.github.io/ncco-examples/first_call_talk.json):

```java
VonageClient client = VonageClient.builder()
        .applicationId(APP_ID)
        .privateKeyPath("application_key.pem")
        .build();

Ncco ncco = new Ncco(
    TalkAction.builder("Hi, this is Russell. You are listening to a text-to-speech Call made with Vonage's Voice API")
        .voiceName(VoiceName.RUSSELL)
        .build()
);

Call call = new Call(to, from, ncco);
CallEvent event = client.getVoiceClient().createCall(call);
```

### Send a 2FA Code

Send a 2FA code to a phone number with:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
VerifyResponse ongoingVerify = client.getVerifyClient().verify(TO_NUMBER, "NEXMO");
```

### Check the 2FA Code

When the user enters the code they received, you can check it like this:

```java
client.getVerifyClient().check(ongoingVerify.getRequestId(), CODE)
```

### Send a PSD2 Payment Verification Code

Send a PSD2 code to a phone number with:

````java
NexmoClient client = NexmoClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
VerifyResponse verifyPayment = client.getVerifyClient().psd2Verify(TO_NUMBER, 103.33, "Michelle");
````

### Get a List of SMS Prices for a Country

Get a list of SMS prices for a country with:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
PricingResponse response = client.getAccountClient().getSmsPrice("GB");
System.out.println(response.getDefaultPrice());
```

### Get a List of Voice Prices for a Country

Get a list of voice prices for a country with:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
PricingResponse response = client.getAccountClient().getVoicePrice("US");
System.out.println(response.getDefaultPrice());
```

### Get a List of SMS Prices for a Prefix

Get a list of SMS prices for a country with:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.SMS, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Get a List of Voice Prices for a Prefix

Get a list of voice prices for a country with:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.VOICE, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Top-up Account

Top-up your account that has auto-reload enabled with:
```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
client.getAccountClient().topUp("TRANSACTION_NUMBER");
```

### Submit Conversion

Submit a request to the Conversion API when it has been enabled on your account with:
```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
client.getConversionClient().submitConversion(ConversionRequest.Type.VOICE,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
```

### Redact Inbound SMS

Submit a request to the Redact API when it has been enabled on your account with:
```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
client.getRedactClient().redactTransaction(SMS_ID, RedactRequest.Product.SMS, RedactRequest.Type.INBOUND);
```

### Redact Voice

Submit a request to the Redact API when it has been enabled on your account with:
```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
client.getRedactClient().redactTransaction(VOICE_ID, RedactRequest.Product.VOICE);
```

### Create Secret

Create a secret associated with your account id:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
SecretResponse response = client.getAccountClient().createSecret(API_KEY, "Foo84RSecret");
```

### List Secrets

List the secret id (but not content) associated with your account id:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
ListSecretsResponse response = client.getAccountClient().listSecrets(API_KEY);

Collection<SecretResponse> secrets = response.getSecrets();
```

### Revoke Secret

Revoke a secret associated with your account id:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
client.getAccountClient().revokeSecret(API_KEY, SECRET_ID);
```

### Retrieve Secret

Get information about a specific secret associated with your account id:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
SecretResponse response = client.getAccountClient().getSecret(API_KEY, SECRET_ID);
```

### Custom HTTP Configuration

If you need to configure the Apache HttpClient used for making requests, you can
call `VonageClient.Builder.httpClient()` to supply your custom configured object. This
can be useful, for example, if you must use an HTTP proxy to make requests or to configure SSL Certificates.

## Tips And Tricks

### Phone Calls And WebSockets
Our [Voice API](https://developer.nexmo.com/voice/voice-api/overview) can connect a voice call to a websocket! An example using `javax.websocket` for accepting websocket connections can be found on the [Oracle website](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html#section4).
[Another  example](http://sparkjava.com/documentation#embedded-web-server) using the Spark framework

## Frequently Asked Questions

Q: Does this SDK support thread safety? 
A: No, it currently does not. 

### Supported APIs

The following is a list of Vonage APIs and whether the Java SDK provides support for them:

| API                   |  API Release Status  | Supported? |
| --------------------- | :------------------: | :--------: |
| Account API           | General Availability |     ✅     |
| Alerts API            | General Availability |     ✅     |
| Application API       | General Availability |     ✅     |
| Audit API             |         Beta         |     ❌     |
| Conversation API      |         Beta         |     ❌     |
| Dispatch API          |         Beta         |     ❌     |
| External Accounts API |         Beta         |     ❌     |
| Media API             |         Beta         |     ❌     |
| Messages API          |         Beta         |     ❌     |
| Number Insight API    | General Availability |     ✅     |
| Number Management API | General Availability |     ✅     |
| Pricing API           | General Availability |     ✅     |
| Redact API            | General Availability |     ✅     |
| Reports API           |         Beta         |     ❌     |
| SMS API               | General Availability |     ✅     |
| Verify API            | General Availability |     ✅     |
| Voice API             | General Availability |     ✅     |


## License

This library is released under the [Apache 2.0 License][license]


## Contribute!

_We :heart: contributions to this library!_

It is a good idea to [talk to us](https://developer.nexmo.com/community/slack)
first if you plan to add any new functionality.
Otherwise, [bug reports](https://github.com/Vonage/vonage-java-sdk/issues),
[bug fixes](https://github.com/Vonage/vonage-java-sdk/pulls) and feedback on the
library are always appreciated.


[create_account]: https://docs.nexmo.com/tools/dashboard#setting-up-your-nexmo-account
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://docs.nexmo.com/api-ref/sms-api?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[license]: LICENSE.txt
