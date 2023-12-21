# Vonage Server SDK for Java

[![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.vonage/server-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.vonage/server-sdk)
[![Build Status](https://github.com/Vonage/vonage-java-sdk/actions/workflows/build.yml/badge.svg)](https://github.com/Vonage/vonage-java-sdk/actions/workflows/build.yml?query=workflow%3A"Build+Java+CI")
[![codecov](https://codecov.io/gh/vonage/vonage-java-sdk/branch/main/graph/badge.svg)](https://codecov.io/gh/vonage/vonage-java-sdk)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE.txt)

<img src="https://developer.nexmo.com/assets/images/Vonage_Nexmo.svg" height="48px" alt="Nexmo is now known as Vonage" />

You can use this Java Server SDK to add [Vonage APIs](https://developer.vonage.com/api) to your application. To use this, you'll
need a Vonage account. You'll need to have [created a Vonage account](https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=java-client-library).

* [Supported APIs](#supported-apis)
* [Other SDKs](#other-sdks)
* [Installation](#installation)
* [Usage](#usage)
* [Configuration](#configuration)
* [FAQ](#frequently-asked-questions)
* [Contribute!](#contribute)

## Supported APIs

- [Account](https://developer.vonage.com/en/account/overview)
- [Application](https://developer.vonage.com/en/application/overview)
- [Conversion](https://developer.vonage.com/messaging/conversion-api/overview)
- [Meetings](https://developer.vonage.com/en/meetings/overview)
- [Messages](https://developer.vonage.com/en/messages/overview)
- [Number Insight](https://developer.vonage.com/en/number-insight/overview)
- [Number Management](https://developer.vonage.com/en/numbers/overview)
- [Proactive Connect](https://developer.vonage.com/en/proactive-connect/overview)
- [Redact](https://developer.vonage.com/en/redact/overview)
- [SMS](https://developer.vonage.com/en/sms/overview)
- [Subaccounts](https://developer.vonage.com/en/account/subaccounts/overview)
- [Verify](https://developer.vonage.com/en/verify/overview)
- [Video](https://developer.vonage.com/en/video/overview)
- [Voice](https://developer.vonage.com/en/voice/overview)

## Other SDKs

We also provide server SDKs in other languages:

- [.NET](https://github.com/Vonage/vonage-dotnet-sdk)
- [PHP](https://github.com/Vonage/vonage-php-sdk)
- [Python](https://github.com/Vonage/vonage-python-sdk)
- [Ruby](https://github.com/Vonage/vonage-ruby-sdk)
- [NodeJS](https://github.com/Vonage/vonage-node-sdk)

We also offer [client-side SDKs](https://developer.vonage.com/en/vonage-client-sdk/overview) for iOS, Android and JavaScript.
See all of our SDKs and integrations on the [Vonage Developer portal](https://developer.vonage.com/en/tools).

## Installation

Releases are published to [Maven Central](https://central.sonatype.com/artifact/com.vonage/server-sdk/8.0.0/snippets).
Instructions for your build system can be found in the snippets section.
They're also available from [here](https://mvnrepository.com/artifact/com.vonage/server-sdk/8.0.0).
Release notes can be found in the [changelog](CHANGELOG.md).

### Build It Yourself

Alternatively you can clone the repo and build the JAR file yourself:

```bash
git clone git@github.com:vonage/vonage-java-sdk.git
gradle build
```

### Download everything in a ZIP file

**Note**: We *strongly recommend* that you use a tool that supports dependency management,
such as [Gradle](https://gradle.org/), [Maven](https://maven.apache.org/) or [Ivy](http://ant.apache.org/ivy/).

We provide a [ZIP file for each release](https://github.com/Vonage/vonage-java-sdk/releases/),
containing the Java Server SDK JAR, along with all the dependencies. Download the file, unzip it, and add the JAR files
to your project's classpath.

## Usage

* For help understanding our APIs, check out our awesome [developer portal](https://developer.vonage.com/).
* Check the [Javadoc](https://www.javadoc.io/doc/com.vonage/server-sdk/latest/index.html) for full reference documentation.
* There are also **many useful code samples** in our [Vonage/vonage-java-code-snippets](https://github.com/Vonage/vonage-java-code-snippets) repository.
* For Video API usage instructions, see [the guide on our developer portal](https://developer.vonage.com/en/video/server-sdks/java).

## Configuration

## Typical Instantiation
For default configuration, you just need to specify your Vonage account credentials using API key and secret, private
key and application ID or both. For maximum compatibility with all APIs, it is recommended that you specify both
authentication methods, like so:

```java
VonageClient client = VonageClient.builder()
        .applicationId(APPLICATION_ID)
        .privateKeyPath(PRIVATE_KEY_PATH)
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
```

### Customize the Base URI
By default, the client will use https://api.nexmo.com, https://rest.nexmo.com,  https://api-eu.vonage.com and https://video.api.vonage.com as base URIs for the various endpoints. To customize these you can instantiate `VonageClient` with an `HttpConfig` object.

`HttpConfig.Builder` has been created to assist in building this object. Usage is as follows:

```java
HttpConfig httpConfig = HttpConfig.builder()
        .apiBaseUri("https://api.example.com")
        .restBaseUri("https://rest.example.com")
        .apiEuBaseUri("https://api-eu.example.com")
        .videoBaseUri("https://video.example.com")
        .build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY).apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

If you do not specify a property, it will take on whatever the default value is. You can also set all three with a single method:

```java
HttpConfig httpConfig = HttpConfig.builder().baseUri("http://example.com").build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY).apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

### Custom Timeout

By default, the SDK has a 1-minute timeout for requests.
You can change this to be longer or shorter using `HttpConfig`. The following example sets this to 12 seconds:

```java
VonageClient client = VonageClient.builder()
        .applicationId(APPLICATION_ID)
        .privateKeyPath(PRIVATE_KEY_PATH)
        .httpConfig(HttpConfig.builder().timeoutMillis(12_000).build())
        .build();
```

## Frequently Asked Questions

**Q: What happened to [`com.vonage:client`](https://search.maven.org/artifact/com.vonage/client)?**

**A:** To avoid confusion with our various client-side SDKs, this server-side SDK has been moved from
the `com.vonage:client` coordinates to `com.vonage:server-sdk`. The old artifactId (`com.vonage:client`) will
not receive further updates. All users should migrate to the new artifactId. Please note that the SDK is functionally
the same, it is just a namespace change on Maven Central.

**Q: What is your policy on thread safety?**

**A:** The current architecture of the SDK means that only one thread should use the client at a time.
If you would like to use the SDK in a multithreaded environment, create a separate instance of
`VonageClient` for each thread.

**Q: Does this SDK support asynchronous request / response processing?**

**A:** Currently no, but it is on the roadmap.

**Q: How do I migrate from TokBox to Vonage?**

**A:** See [the OpenTok migration guide](src/main/java/com/vonage/client/video/OPENTOK_TO_VONAGE_MIGRATION.md).

## Contribute!

_We :heart: contributions to this library!_

It is a good idea to [talk to us](https://developer.vonage.com/community/slack)
first if you plan to add any new functionality.
Otherwise, [bug reports](https://github.com/Vonage/vonage-java-sdk/issues),
[bug fixes](https://github.com/Vonage/vonage-java-sdk/pulls) and feedback on the
library are always appreciated.
