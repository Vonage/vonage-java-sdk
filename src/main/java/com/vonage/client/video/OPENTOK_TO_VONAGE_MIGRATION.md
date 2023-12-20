# Migration guide from OpenTok Java SDK to Vonage Java SDK

## Installation
See [README.md](https://github.com/Vonage/vonage-java-sdk?tab=readme-ov-file#installation) for how to add the SDK as a dependency to your project.
You need to use the `com.vonage:server-sdk:[8.0.0,9.0.0)` co-ordinates from [Maven Central](https://search.maven.org/artifact/com.vonage/server-sdk).

## Client Initialisation
See [README.md](https://github.com/Vonage/vonage-java-sdk?tab=readme-ov-file#typical-instantiation) for setup instructions.

Instead of this:
```java
OpenTok client = new OpenTok.Builder(apiKey, apiSecret).build();
```

Do the following:

```java
import com.vonage.client.video.*;
import com.vonage.client.VonageClient;

// Inside a constructor or method body:
VonageClient vonage = VonageClient.builder()
        .applicationId(VONAGE_APP_ID)
        .privateKeyPath(VONAGE_PRIVATE_KEY_PATH)
        .build();
VideoClient video = vonage.getVideoClient();
```

Note that unlike OpenTok, you will need an application ID and private key (typically stored in a file on your local machine).
You can create an application from the [Vonage Dashboard](https://dashboard.nexmo.com/applications).
Please ensure that Video capabilities are enabled on the application.
Please see [the documentation for more general guidance on getting started with Vonage](https://developer.vonage.com/en/getting-started/overview?source=video).

## Usage
Once you have instantiated the `VonageClient`, you can use the [Video API](https://developer.vonage.com/en/video/overview) by using the `VideoClient`, as obtained from the `VonageClient` (see above).

The API methods on `VideoClient` are documented using Javadocs, and are roughly analogous to the methods found on the `OpenTok` class.

For more detailed usage instructions, see the [Java Sever SDK video guide](https://developer.vonage.com/en/video/server-sdks/java).

## `OpenTok` to `VideoClient` notable changes
There are a few small changes to be aware of when migrating to Vonage from OpenTok.
Many of these are straightforward and your IDE will help you with auto-completion, but for clarity, consider the following:

- `sendDTMF` renamed to `playDtmf`
- `OpenTok#disableForceMute(String)` replaced by `VideoClient#muteSession(String, boolean, String...)`. You need to set the `active` boolean parameter to `false` to achieve the same effect.
- The `MuteAllProperties` class and parameter in `OpenTok` has been replaced by using the `excludedStreamIds` directly in the method parameter of `VideoClient#muteSession(String, boolean, Collection<String>)` (or `VideoClient#muteSession(String, boolean, String...) for convenience`). These methods replace `OpenTok#forceMuteAll(String, MuteAllProperties)`.
- `ArchiveProperties` and `BroadcastProperties` - as used in request parameters in OpenTok - have been replaced by `Archive` and `Broadcast` respectively. Both use the builder pattern for construction.
  - `Archive` and `Broadcast` in Vonage also represent the responses in a similar way to their OpenTok counterparts.
  - Thus, the request and response objects representing `Archive` and `Broadcast` have been unified in the Vonage implementation.
- `OpenTok#setBroadcastLayout(String, BroadcastProperties)` replaced by `VideoClient#updateBroadcastLayout(String, StreamCompositionLayout)`.
- `OpenTok#setArchiveLayout(String, ArchiveProperties)` replaced by `VideoClient#updateArchiveLayout(String, StreamCompositionLayout)`.
- `OpenTok#dial(String, String, SipProperties)` replaced by `VideoClient#sipDial(SipDialRequest)`.
  - `Sip` replaced with `SipResponse`.
- The `listArchives` methods with various parameters in OpenTok have been replaced by `VideoClient#listArchives(ListStreamCompositionsRequest)` for controlling the options.
  - The response is a plain `List<Archive>` instead of `ArchiveList`. Use `Collection#size()` instead of `ArchiveList#getTotalCount()` to obtain the number of elements.
- `OpenTok#setStreamLayouts(String, StreamListProperties)` replaced by `VideoClient#setStreamLayout(String, List<SessionStream>)` (or `VideoClient#setStreamLayout(String, SessionStream...)` for convenience).
- `OpenTok#signal(String, String, SignalProperties)` and `OpenTok#signal(String, SignalProperties)` replaced by `VideoClient#signal(String, String, SignalRequest)` and `VideoClient#signalAll(String, SignalRequest)`, respectively.
- The structure of tokens obtained used the `generateToken` methods in `OpenTok` and `VideoClient` are different. Vonage uses JWTs, whereas OpenTok uses a custom solution.
- Not all features in `OpenTok` are supported in `VideoClient` yet - see below.

## Supported Features
The following is a list of Vonage Video API features and whether the Vonage Java SDK currently supports them:

| API   |  Supported?
|----------|:-------------:|
| Session Creation | ✅ |
| Stream Management | ✅ |
| Signaling | ✅ |
| Moderation | ✅ |
| Archiving | ✅ |
| Live Streaming Broadcasts | ✅ |
| SIP Interconnect | ✅ |
| Account Management | ❌ |
| Experience Composer | ❌ |
| Audio Connector | ❌ |
| Live Captions | ❌ |
| Custom S3/Azure buckets | ❌ |
