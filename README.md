
# Quick Start

To compile the SDK and execute the examples, issue the following command

```bash
ant example
```

This will construct a message request and use the SDK to attempt to submit it.

The code can be seen in this class [SendTextMessage.java](src/main/java/com/nexmo/messaging/sdk/examples/SendTextMessage.java)

You can modify the constants at the top of the class to inject your own account credentials and message parameters.

```java
public static final String USERNAME = "account-id";
public static final String PASSWORD = "password";

public static final String SMS_FROM = "12345";
public static final String SMS_TO = "447777111222";
public static final String SMS_TEXT = "Hello World!";
```

`ant example2` will execute an example wap-push submission.
The code can be found here [SendWapPush.java](src/main/java/com/nexmo/messaging/sdk/examples/SendWapPush.java)

# Installation

You need to copy the nexmo-sdk.jar to your application and ensure that it is in your classpath. Additionally, there are some library dependancies under the /lib dir that must also be copied to your applications classpath if they are not already there.

The Nexmo SDK is then available for use. All configuration is passed as parameters on the constructors of the various objects.

# Usage

To submit a message, first you should instantiate a NexmoSmsClient, passing the credentials for your Nexmo account on the constructor.
Then, you should instantiate the appropriate Message subclass depending on which type of message you are going to submit.
The following subclasses are available:

* com.nexmo.messaging.sdk.messages.TextMessage;
* com.nexmo.messaging.sdk.messages.BinaryMessage;
* com.nexmo.messaging.sdk.messages.WapPushMessage;
* com.nexmo.messaging.sdk.messages.UnicodeMessage;

Each of these subclasses requires different message parameters to be passed on the constructor.
See the included Javadocs for further details.

Once you have a Message object, you simply pass this to the `submitMessage()` method in the `NexmoSmsClient` instance.
This will construct and post the request to the Nexmo REST service.
This method will return an array of `SmsSubmissionResult[]`, with 1 entry for every sms message that was sent.
Certain messages, for example, long text messages greater than 160 characters, will require multiple SMS messages to be submitted.
Each entry in this array will contain an individual `messageId` as well as an individual status detailing the success or reason for failure of each message.

The list of possible status codes is listed below:

| Code | Summary                      | Description                                                                                        |
|-----:|------------------------------|----------------------------------------------------------------------------------------------------|
|    0 | Success                      | The message was successfully accepted for delivery by Nexmo                                        |
|    1 | Throttled                    | You have exceeded the submission capacity allowed on this account, please back-off and retry       |
|    2 | Missing params               | Your request is incomplete and missing some mandatory parameters                                   |
|    3 | Invalid params               | The value of 1 or more parameters is invalid                                                       |
|    4 | Invalid credentials          | The api key / secret you supplied is either invalid or disabled                                    |
|    5 | Internal error               | An error has occurred in the Nexmo platform whilst processing this message                         |
|    6 | Invalid message              | The Nexmo platform was unable to process this message, for example, an un-recognized number prefix |
|    7 | Number barred                | The number you are trying to submit to is blacklisted and may not receive messages                 |
|    8 | Partner account barred       | The api key you supplied is for an account that has been barred from submitting messages           |
|    9 | Partner quota exceeded       | Your pre-pay account does not have sufficient credit to process this message                       |
|   10 | Too many existing binds      | The number of simultaneous connections to the platform exceeds the capabilities of your account    |
|   11 | Account not enabled for http | This account is not provisioned for http / rest submission, you should use SMPP instead            |
|   12 | Message too long             | The message length exceeds the maximum allowed                                                     |
|   13 | Comms failure                | There was a network failure attempting to contact Nexmo                                            |
|   14 | Invalid Signature            | The signature supplied with this request was not verified successfully                             |
|   15 | Invalid sender address       | The sender address was not allowed for this message                                                |
|   16 | Invalid TTL                  | The `ttl` parameter values, or combination of parameters is invalid                                |
|   17 | Number unreachable           | This destination cannot be delivered to at this time (if reachable=true is specified)              |
|   18 | Too many destinations        | There are more than the maximum allowed number of destinations in this request                     |
|   19 | Facility Not Allowed         | Your request makes use of a facility that is not enabled on your account                           |
|   20 | Invalid Message Class        | The message class value supplied was out of range (0 - 3)                                          |
