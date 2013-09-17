package com.nexmo.sns.sdk.request;

/**
 * PublishRequest.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the details of a publish service request that is to be submitted via the http api.<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class PublishRequest extends Request {

    public PublishRequest(final String topicArn,
                          final String from,
                          final String message) throws Exception {
        super(COMMAND_PUBLISH,
              "topic-arn", topicArn,
              "from", from,
              "message", message);
    }

}
