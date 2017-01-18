# Nexmo Client Library for Java

[![Build Status](https://travis-ci.org/Nexmo/nexmo-java.svg?branch=version-2)](https://travis-ci.org/Nexmo/nexmo-java)
[![codecov](https://codecov.io/gh/Nexmo/nexmo-java/branch/version-2/graph/badge.svg)](https://codecov.io/gh/Nexmo/nexmo-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9888a9f2ec0d4599a11762e5d946da17)](https://www.codacy.com/app/mark-smith/nexmo-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nexmo/nexmo-java&amp;utm_campaign=Badge_Grade)


You can use this Java client library to add [Nexmo's API](#api-coverage) to your application. To use this, you'll
need a Nexmo account. Sign up [for free at nexmo.com][signup].

 * [Installation](#installation)
 * [Configuration](#configuration)
 * [Usage](#usage)
 * [Examples](#examples)
 * [API Coverage](#api-coverage)
 * [Contributing](#contributing)


## Installation

To use the client library you'll need to have [created a Nexmo account][signup].

To install the Java client library using Gradle, add the following to `build.gradle`:

```groovy
compile 'com.nexmo:nexmo-java:2.0'
```

Alternatively you can clone the repo and build the jar yourself:

```bash
git clone git@github.com:nexmo/nexmo-java.git
gradle build
```

## Usage

Check the Javadoc for full documentation.

### Examples

The following code initiates an outbound call which then reads the user [a message](https://nexmo-community.github.io/ncco-examples/first_call_talk.json):

```java
import java.nio.file.Paths;

import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.NexmoClient;
import com.nexmo.client.voice.Call;

JWTAuthMethod auth = new JWTAuthMethod(application_id, Paths.get("application_key.pem"));
NexmoClient client = new NexmoClient(auth);
Call call = new Call(to, from,
                     "https://nexmo-community.github.io/ncco-examples/first_call_talk.json");
CallEvent event = client.getVoiceClient().createCall(call);
```

After the call is answered, you can get more information about it, including
the amount it cost with:

```java
CallRecord info = client.getVoiceClient().getCallDetails(event.getUuid());
System.out.println("This cost: " + info.getPrice() + " EUR");
```

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
    * [ ] Basic
    * [x] Standard (Currently uses the deprecated API)
    * [x] Advanced (Currently uses the deprecated API)
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
    * [x] Outbound Calls
    * [ ] Inbound Call
    * [ ] Text-To-Speech Call
    * [ ] Text-To-Speech Prompt


## License

This library is released under the [MIT License][license]

[create_account]: https://docs.nexmo.com/tools/dashboard#setting-up-your-nexmo-account
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://docs.nexmo.com/api-ref/sms-api?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[license]: LICENSE.txt
