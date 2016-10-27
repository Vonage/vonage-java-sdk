# Nexmo Client Library for Java

[![Build Status](https://travis-ci.org/Nexmo/nexmo-java.svg?branch=version-2)](https://travis-ci.org/Nexmo/nexmo-java)
[![codecov](https://codecov.io/gh/Nexmo/nexmo-java/branch/version-2/graph/badge.svg)](https://codecov.io/gh/Nexmo/nexmo-java)


You can use this [LANGUAGE] client library to add [Nexmo's API](#api-coverage) to your application. To use this, you'll
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
compile 'com.nexmo:nexmo-java:1.6'
```

Alternatively you can clone the repo and build the jar yourself:

```bash
git clone git@github.com:nexmo/nexmo-java.git
gradle build
```

## Usage

Check the Javadoc for full documentation.


API Coverage
------------

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
    * [ ] Outbound Calls
    * [ ] Inbound Call
    * [ ] Text-To-Speech Call
    * [ ] Text-To-Speech Prompt


License
-------

This library is released under the [MIT License][license]

[create_account]: https://docs.nexmo.com/tools/dashboard#setting-up-your-nexmo-account
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://docs.nexmo.com/api-ref/sms-api?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[license]: LICENSE.txt
