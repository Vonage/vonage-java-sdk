# Nexmo Client Library for Java

<!-- [![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.nexmo/client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nexmo/client) -->
[![Build Status](https://travis-ci.org/Nexmo/nexmo-java.svg?branch=version-2)](https://travis-ci.org/Nexmo/nexmo-java)
[![codecov](https://codecov.io/gh/Nexmo/nexmo-java/branch/version-2/graph/badge.svg)](https://codecov.io/gh/Nexmo/nexmo-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9888a9f2ec0d4599a11762e5d946da17)](https://www.codacy.com/app/mark-smith/nexmo-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nexmo/nexmo-java&amp;utm_campaign=Badge_Grade)


You can use this Java client library to add [Nexmo's API](#api-coverage) to your application. To use this, you'll
need a Nexmo account. Sign up [for free at nexmo.com][signup].

 * [Installation](#installation)
 * [Usage](#usage)
 * [Examples](#examples)
 * [API Coverage](#api-coverage)
 * [Contribute](#contribute)

## Installation

To use the client library you'll need to have [created a Nexmo account][signup].

### Gradle

To install the Java client library using Gradle, add the following to `build.gradle`:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.nexmo:client:3.1.0'
}
```

### Maven

Add the following to the correct place in your project's POM file:

```xml
<dependency>
      <groupId>com.nexmo</groupId>
      <artifactId>client</artifactId>
      <version>3.1.0</version>
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

Check the [Javadoc](http://nexmo.github.io/nexmo-java/) for full
reference documentation. There are also many useful code samples in the [nexmo-community/nexmo-java-quickstart](https://github.com/nexmo-community/nexmo-java-quickstart) repository.

## Send an SMS

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

## Make Phone Calls

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

## Send a 2FA Code

Send a 2FA code to a phone number with:

```java
AuthMethod auth = new TokenAuthMethod(API_KEY, API_SECRET);
NexmoClient client = new NexmoClient(auth);
VerifyResult ongoingVerify = client.getVerifyClient().verify(TO_NUMBER, "NEXMO");
```

## Check the 2FA Code

When the user enters the code they received, you can check it like this:

```java
client.getVerifyClient().check(ongoingVerify.getRequestId(), CODE)
```

## Custom HTTP Configuration

If you need to configure the Apache HttpClient used for making requests, you can
call `NexmoClient.setHttpClient()` to supply your custom configured object. This
can be useful, for example, if you must use an HTTP proxy to make requests.


## API Coverage

* Account
    * [x] Balance
    * [ ] Pricing
    * [ ] Settings
    * [ ] Top Up
    * [ ] Numbers
        * [ ] Search
        * [ ] Buy
        * [ ] Cancel
        * [ ] Update
* Number Insight
    * [x] Basic
    * [x] Standard
    * [x] Advanced
    * [ ] Webhook Notification
* Verify
    * [x] Verify
    * [x] Check
    * [x] Search
    * [ ] Control
* Messaging
    * [x] Send
    * [x] Delivery Receipt (Callback can only be set in the Dashboard)
    * [ ] Inbound Messages
    * [ ] Search
        * [ ] Message
        * [ ] Messages
        * [ ] Rejections
    * [ ] US Short Codes
        * [ ] Two-Factor Authentication
        * [ ] Event Based Alerts
            * [ ] Sending Alerts
            * [ ] Campaign Subscription Management
* Voice
    * [x] Create call
    * [x] List calls
    * [x] Get call info
    * [x] Modify existing call
    * [x] Stream audio to an existing call
    * [x] Stop streaming audio to an existing call
    * [x] Send speech to an existing call
    * [x] Stop speech in an existing call
    * [x] Send DTMF to an existing call
    * [ ] eventUrl webhook support
    * [x] answerUrl webhook support


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
