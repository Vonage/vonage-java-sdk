package com.nexmo.sns.sdk.request;

/**
 * SubscribeRequest.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the details of a subscribe service request that is to be submitted via the http api.<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class SubscribeRequest extends Request {

    public SubscribeRequest(final String topicArn,
                            final String msisdn) throws Exception {
        super(COMMAND_SUBSCRIBE,
              "topic-arn", topicArn,
              "msisdn", msisdn);
    }

}
