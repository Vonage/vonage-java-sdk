# Vonage Server SDK for Java

[![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.vonage/client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.vonage/client)
[![Build Status](https://github.com/vonage/vonage-java-sdk/workflows/build/badge.svg)](https://github.com/Vonage/vonage-java-sdk/actions?query=workflow%3A"Build+Java+CI")
[![codecov](https://codecov.io/gh/vonage/vonage-java-sdk/branch/main/graph/badge.svg)](https://codecov.io/gh/vonage/vonage-java-sdk)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)][license]

<img src="https://developer.nexmo.com/assets/images/Vonage_Nexmo.svg" height="48px" alt="Nexmo is now known as Vonage" />

You can use this Java Server SDK to add [Vonage APIs](https://developer.vonage.com/api) to your application. To use this, you'll
need a Vonage account. Sign up [for free at vonage.com][signup].

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
    implementation 'com.vonage:client:7.5.0'
}
```

For older versions:

```groovy
dependencies {
    compile 'com.vonage:client:7.5.0'
}
```

### Maven

Add the following to the correct place in your project's POM file:

```xml
<dependency>
    <groupId>com.vonage</groupId>
    <artifactId>client</artifactId>
    <version>7.5.0</version>
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

* For help understanding our APIs, check out our awesome [developer portal](https://developer.vonage.com/)
* Check the [Javadoc](https://www.javadoc.io/doc/com.vonage/client/latest/index.html) for full reference documentation.
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
        .build();
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

### Send an MMS

Send an MMS with the Vonage Messages API:
```java
MessageRequest request = MmsImageRequest.builder()
        .from(FROM_NUMBER)
        .to(TO_NUMBER)
        .url(IMAGE_URL)
        .caption(IMAGE_CAPTION)
        .build();
MessageResponse response = client.getMessagesClient().sendMessage(request);
```

### Send a file via Messenger

Send a file over Facebook Messenger using the Vonage Messages API:
```java
MessageRequest request = MessengerFileRequest.builder()
        .from(SENDER_ID)
        .to(RECIPIENT_ID)
        .url(FILE_URL)
        .build();
MessageResponse response = client.getMessagesClient().sendMessage(request);
```

### Send an image via Viber

Send a picture over Viber using the Vonage Messages API:
```java
MessageRequest request = ViberImageRequest.builder()
        .from(SENDER_ID)
        .to(TO_NUMBER)
        .url(IMAGE_URL)
        .build();
MessageResponse response = client.getMessagesClient().sendMessage(request);
```

### Send a video via WhatsApp

Send a video over WhatsApp using the Vonage Messages API:
```java
MessageRequest request = WhatsappVideoRequest.builder()
        .from(FROM_NUMBER)
        .to(TO_NUMBER)
        .url(VIDEO_URL)
        .caption(VIDEO_DESCRIPTION)
        .build();
MessageResponse response = client.getMessagesClient().sendMessage(request);
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
ModifyCallResponse modifyResponse = client.getVoiceClient().modifyCall(event.getUuid(), ModifyCallAction.HANGUP);
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
TalkResponse startTalkResponse = client.getVoiceClient().startTalk(
		event.getUuid(), 
        TalkPayload.builder("Hello, world!").language(TextToSpeechLanguage.SWEDISH).build()
);
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
                        .build();

TalkAction outro = TalkAction.builder("Thanks, goodbye!")
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
        .build()
);

Call call = new Call(to, from, ncco);
CallEvent event = client.getVoiceClient().createCall(call);
```

### Send a 2FA Code

Send a 2FA code to a phone number with:

```java
VerifyResponse ongoingVerify = client.getVerifyClient().verify(TO_NUMBER, "Vonage");
```

### Check the 2FA Code

When the user enters the code they received, you can check it like this:

```java
client.getVerifyClient().check(ongoingVerify.getRequestId(), CODE)
```

### Send a PSD2 Payment Verification Code

Send a PSD2 code to a phone number with:

````java
VerifyResponse verifyPayment = client.getVerifyClient().psd2Verify(TO_NUMBER, 103.33, "Michelle");
````

### Prompt a WhatsApp user for verification, with Voice call and e-mail as fallback

```java
VerificationResponse ongoingVerify = client.getVerify2Client().sendVerification(
    VerificationRequest.builder()
        .addWorkflow(new WhatsappCodelessWorkflow(TO_NUMBER))
        .addWorkflow(new EmailWorkflow(TO_EMAIL))
        .addWorkflow(new VoiceWorkflow(TO_NUMBER))
        .codeLength(6).brand("ACME Inc.").build()
);
```

If the codeless verification fails, you can check the user-entered code (sent via backup contact methods) like this:

```java
client.getVerify2Client().checkVerificationCode(ongoingVerify.getRequestId(), CODE);
```

### Cancel a pending verification

Abort a verification workflow like this:

```java
client.getVerify2Client().cancelVerification(ongoingVerify.getRequestId());
```

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
PricingResponse response = client.getAccountClient().getVoicePrice("US");
System.out.println(response.getDefaultPrice());
```

### Get a List of SMS Prices for a Prefix

Get a list of SMS prices for a country with:
```java
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.SMS, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Get a List of Voice Prices for a Prefix

Get a list of voice prices for a country with:
```java
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.VOICE, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Top-up Account

Top-up your account that has auto-reload enabled with:
```java
client.getAccountClient().topUp("TRANSACTION_NUMBER");
```

### Submit Conversion

Submit a request to the Conversion API when it has been enabled on your account with:
```java
client.getConversionClient().submitConversion(ConversionRequest.Type.VOICE,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
```

### Create Secret

Create a secret associated with your account id:
```java
SecretResponse response = client.getAccountClient().createSecret(API_KEY, "Foo84RSecret");
```

### List Secrets

List the secret id (but not content) associated with your account id:
```java
ListSecretsResponse response = client.getAccountClient().listSecrets(API_KEY);
Collection<SecretResponse> secrets = response.getSecrets();
```

### Revoke Secret

Revoke a secret associated with your account id:
```java
client.getAccountClient().revokeSecret(API_KEY, SECRET_ID);
```

### Retrieve Secret

Get information about a specific secret associated with your account id:
```java
SecretResponse response = client.getAccountClient().getSecret(API_KEY, SECRET_ID);
```

### Video API

The Vonage Video API (formerly OpenTok) is currently in beta. You can try it out by using a beta version.
Usage instructions can be found on the [8.x-beta branch](https://github.com/Vonage/vonage-java-sdk/tree/8.x-beta#video-api).
See the [Releases page](https://github.com/Vonage/vonage-java-sdk/releases) for more information.


### Custom HTTP Configuration

If you need to configure the Apache HttpClient used for making requests, you can
call `VonageClient.Builder.httpClient()` to supply your custom configured object. This
can be useful, for example, if you must use an HTTP proxy to make requests or to configure SSL Certificates.

## Tips And Tricks

### Phone Calls And WebSockets
Our [Voice API](https://developer.vonage.com/voice/voice-api/overview) can connect a voice call to a websocket! An example using `javax.websocket` for accepting websocket connections can be found on the [Oracle website](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html#section4).
[Another  example](http://sparkjava.com/documentation#embedded-web-server) using the Spark framework

## Frequently Asked Questions

Q: Does this SDK support thread safety?
A: No, it currently does not.

Q: Does this SDK support asynchronous request / response processing?
A: Currently no, but it is on the roadmap.

### Supported APIs

The following is a list of Vonage APIs and whether the Java SDK provides support for them:

| API                |  API Release Status  | Supported? |
|--------------------|:--------------------:|:----------:|
| Account            | General Availability |     ✅      |
| Alerts             | General Availability |     ✅      |
| Application        | General Availability |     ✅      |
| Audit              |         Beta         |     ❌      |
| Conversation       |         Beta         |     ❌      |
| Dispatch           |         Beta         |     ❌      |
| External Accounts  |         Beta         |     ❌      |
| Media              |         Beta         |     ❌      |
| Messages           | General Availability |     ✅      |
| Number Insight     | General Availability |     ✅      |
| Number Management  | General Availability |     ✅      |
| Pricing            | General Availability |     ✅      |
| Redact             |  Developer Preview   |     ✅      |
| Reports            |         Beta         |     ❌      |
| SMS                | General Availability |     ✅      |
| Verify             | General Availability |     ✅      |
| Voice              | General Availability |     ✅      |
| Video              |         Beta         |     ☑️     |


## License

This library is released under the [Apache 2.0 License][license]


## Contribute!

_We :heart: contributions to this library!_

It is a good idea to [talk to us](https://developer.vonage.com/community/slack)
first if you plan to add any new functionality.
Otherwise, [bug reports](https://github.com/Vonage/vonage-java-sdk/issues),
[bug fixes](https://github.com/Vonage/vonage-java-sdk/pulls) and feedback on the
library are always appreciated.


[create_account]: https://developer.vonage.com/account/overview
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://developer.vonage.com/messaging/sms/overview
[license]: LICENSE.txt
