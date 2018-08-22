# Nexmo Client Library for Java

[![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.nexmo/client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nexmo/client)
[![Build Status](https://travis-ci.org/Nexmo/nexmo-java.svg?branch=version-2)](https://travis-ci.org/Nexmo/nexmo-java)
[![codecov](https://codecov.io/gh/Nexmo/nexmo-java/branch/version-2/graph/badge.svg)](https://codecov.io/gh/Nexmo/nexmo-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0049e762c00b4ce999f45492795ba50a)](https://www.codacy.com/app/cr0wst/nexmo-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nexmo/nexmo-java&amp;utm_campaign=Badge_Grade)


You can use this Java client library to add [Nexmo's API](#api-coverage) to your application. To use this, you'll
need a Nexmo account. Sign up [for free at nexmo.com][signup].

 * [Installation](#installation)
 * [Usage](#usage)
 * [Tips And Tricks](#tips-and-tricks)
 * [Contribute!](#contribute)

## Installation

To use the client library you'll need to have [created a Nexmo account][signup].

### Gradle

To install the Java client library using Gradle, add the following to `build.gradle`:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.nexmo:client:3.8.0'
}
```

### Maven

Add the following to the correct place in your project's POM file:

```xml
<dependency>
      <groupId>com.nexmo</groupId>
      <artifactId>client</artifactId>
      <version>3.8.0</version>
</dependency>
```

### Build It Yourself

Alternatively you can clone the repo and build the JAR file yourself:

```bash
git clone git@github.com:nexmo/nexmo-java.git
gradle build
```

### Download everything in a ZIP file

**Note**: We *strongly recommend* that you use a tool that supports dependency management, such as [Gradle], [Maven],
or [Ivy]

We provide a [ZIP file for each release](https://github.com/Nexmo/nexmo-java/releases/),
containing the client library JAR, along with all the dependencies. Download the file, unzip it, and add the JAR files
to your project's classpath.

[Gradle]: https://gradle.org/
[Maven]: https://maven.apache.org/
[Ivy]: http://ant.apache.org/ivy/


## Usage

* For help understanding our APIs, check out our awesome [developer portal](https://developer.nexmo.com/)
* Check the [Javadoc](http://nexmo.github.io/nexmo-java/) for full reference documentation.
* There are also **many useful code samples** in our [nexmo-community/nexmo-java-quickstart](https://github.com/nexmo-community/nexmo-java-quickstart) repository.

### Send an SMS

Send an SMS with the Nexmo SMS API:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
System.out.println(FROM_NUMBER);

SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(new TextMessage(
        FROM_NUMBER,
        TO_NUMBER,
        "Hello from Nexmo!"));
for (SmsSubmissionResult response : responses) {
    System.out.println(response);
}
```

### Make Phone Calls

The following code initiates an outbound call which then reads the user [a message](https://nexmo-community.github.io/ncco-examples/first_call_talk.json):

```java
import java.nio.file.Paths;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.CallEvent;

JWTAuthMethod auth = new JWTAuthMethod(application_id, Paths.get("application_key.pem"));
NexmoClient client = new NexmoClient(auth);
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

Our library contains a `com.nexmo.client.voice.ncco` package, providing JSON-serializable objects for your NCCO webhook endpoints. 

Each of the `Action` items contain a `Builder` class that can be used for constructing various actions. These actions are then added to an `Ncco` object for serialization into JSON:

```java
TalkAction.Builder builder = new TalkAction.Builder("").voiceName(VoiceName.KIMBERLY);
TalkAction intro = builder.text("At the tone, record your response and press #.").build();
TalkAction outro = builder.text("Thanks, goodbye!").build();

RecordAction record = new RecordAction.Builder()
                        .beepStart(true)
                        .endOnKey('#')
                        .build()
                        

res.type("application/json");
return new Ncco(intro, record, outro).toJson();
```

### Send a 2FA Code

Send a 2FA code to a phone number with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
VerifyResult ongoingVerify = client.getVerifyClient().verify(TO_NUMBER, "NEXMO");
```

### Check the 2FA Code

When the user enters the code they received, you can check it like this:

```java
client.getVerifyClient().check(ongoingVerify.getRequestId(), CODE)
```

### Get a List of SMS Prices for a Country

Get a list of SMS prices for a country with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
PricingResponse response = client.getAccountClient().getSmsPrice("GB");
System.out.println(response.getDefaultPrice());
```

### Get a List of Voice Prices for a Country

Get a list of voice prices for a country with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
PricingResponse response = client.getAccountClient().getVoicePrice("US");
System.out.println(response.getDefaultPrice());
```

### Get a List of SMS Prices for a Prefix

Get a list of SMS prices for a country with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.SMS, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Get a List of Voice Prices for a Prefix

Get a list of voice prices for a country with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
PrefixPricingResponse response = client.getAccountClient().getPrefixPrice(ServiceType.VOICE, "1");
System.out.println(response.getCountries().get(0).getDefaultPrice());
```

### Top-up Account

Top-up your account that has auto-reload enabled with:
```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
client.getAccountClient().topUp("TRANSACTION_NUMBER");
```

### Submit Conversion

Submit a request to the Conversion API when it has been enabled on your account with:
```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
client.getConversionClient().submitConversion(ConversionRequest.Type.VOICE,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
```

### Redact Inbound SMS

Submit a request to the Redact API when it has been enabled on your account with:
```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
client.getRedactClient().redactTransaction(SMS_ID, RedactRequest.Product.SMS, RedactRequest.Type.INBOUND);
```

### Redact Voice

Submit a request to the Redact API when it has been enabled on your account with:
```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
client.getRedactClient().redactTransaction(VOICE_ID, RedactRequest.Product.VOICE);
```

### Create Secret

Create a secret associated to your account id:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
SecretResponse response = client.getAccountClient().createSecret(API_KEY, "Foo84RSecret");
```

### List Secrets

List the secret id (but not content) associated to your account id:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
ListSecretsResponse response = client.getAccountClient().listSecrets(API_KEY);

Collection<SecretResponse> secrets = response.getSecrets();
```

### Revoke Secret

Revoke a secret associated to your account id:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
client.getAccountClient().revokeSecret(API_KEY, SECRET_ID);
```

### Retrieve Secret

Get information about a specific secret associated to your account id:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
SecretResponse response = client.getAccountClient().getSecret(API_KEY, SECRET_ID);
```

### Custom HTTP Configuration

If you need to configure the Apache HttpClient used for making requests, you can
call `NexmoClient.setHttpClient()` to supply your custom configured object. This
can be useful, for example, if you must use an HTTP proxy to make requests.

## Tips And Tricks

### Phone Calls And WebSockets
Our [Voice API](https://developer.nexmo.com/voice/voice-api/overview) can connect a voice call to a websocket! An example using `javax.websocket` for accepting websocket connections can be found on the [Oracle website](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html#section4).
[Another  example](http://sparkjava.com/documentation#embedded-web-server) using the Spark framework


## License

This library is released under the [MIT License][license]


## Contribute!

_We :heart: contributions to this library!_

It is a good idea to [talk to us](https://nexmo-community-invite.herokuapp.com/)
first if you plan to add any new functionality.
Otherwise, [bug reports](https://github.com/Nexmo/nexmo-java/issues),
[bug fixes](https://github.com/Nexmo/nexmo-java/pulls) and feedback on the
library is always appreciated.


[create_account]: https://docs.nexmo.com/tools/dashboard#setting-up-your-nexmo-account
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://docs.nexmo.com/api-ref/sms-api?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[license]: LICENSE.txt
