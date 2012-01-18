
Quick Start
-----------

to compile the sdk and execute the examples, issue the following command

ant example.

This will construct a message request and use the SDK to attempt to submit it.

The code can be seen in this class "src/com/nexmo/messaging/sdk/examples/SendTextMessage.java"

You can modify the constants at the top of the class to inject your own account credentials and message parameters.

    public static final String USERNAME = "account-id";
    public static final String PASSWORD = "password";

    public static final String SMS_FROM = "12345";
    public static final String SMS_TO = "447777111222";
    public static final String SMS_TEXT = "Hello World!";


ant example2 will execute an example wap-push submission.
The code can be found here "src/com/nexmo/messaging/sdk/examples/SendWapPush.java"

Installation
------------

You need to copy the nexmo-sdk.jar to your application and ensure that it is in your classpath. Additionally, there are some library dependancies under the /lib dir that must also be copied to your applications classpath if they are not already there.

The Nexmo sdk is then available for use. All configuration is passed as parameters on the constructors of the various objects.

Usage
-----

To submit a message, first you should instanciate a NexmoSmsClient, passing the credentials for your Nexmo account on the constructor.
Then, you should instanciate the appropriate Message subclass depending on which type of message you are going to submit.
The following subclasses are available ...

com.nexmo.messaging.sdk.messages.TextMessage;
com.nexmo.messaging.sdk.messages.BinaryMessage;
com.nexmo.messaging.sdk.messages.WapPushMessage;
com.nexmo.messaging.sdk.messages.UnicodeMessage;

Each of these subclasses requires different message parameters to be passed on the constructor.
See the included javadocs for further details.

Once you have a Message object, you simply pass this to the submitMessage() method in the NexmoSmsClient instance.
This will construct and post the request to the Nexmo REST service.
This method will return an array of SmsSubmissionResult[], with 1 entry for every sms message that was sent.
Certain messages, for example, long text messages greater than 160 characters, will require multiple sms's to be submitted.
Each entry in this array will contain an individual messageId as well as an individual status detailing the success or reason for failure of each message.

The list of possible status codes is listed below ..

// 0 = success  -- the message was successfully accepted for delivery by nexmo
// 1 = throttled -- You have exceeded the submission capacity allowed on this account, please back-off and re-try
// 2 = missing params -- Your request is incomplete and missing some mandatory parameters
// 3 = invalid params -- The value of 1 or more parameters is invalid
// 4 = invalid credentials -- The username / password you supplied is either invalid or disabled
// 5 = internal error -- An error has occurred in the nexmo platform whilst processing this message
// 6 = invalid message -- The Nexmo platform was unable to process this message, for example, an un-recognized number prefix
// 7 = number barred -- The number you are trying to submit to is blacklisted and may not receive messages
// 8 = partner account barred -- The username you supplied is for an account that has been barred from submitting messages
// 9 = partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message
// 10 = too many existing binds -- The number of simultaneous connections to the platform exceeds the capabilities of your account
// 11 = account not enabled for http -- This account is not provisioned for http / rest submission, you should use SMPP instead
// 12 = message too long -- The message length exceeds the maximum allowed

