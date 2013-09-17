package com.nexmo.messaging.sdk.messages.parameters;
/**
 * MessageClass.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * An enum of the valid values that may be supplied to as the message-class parameter of a rest submission
 *
 * @author  Paul Cook
 * @version 1.0
 */
public enum MessageClass {

    /**
     * Message Class 0
     */
    MESSAGE_CLASS_0(0),

    /**
     * Message Class 1
     */
    MESSAGE_CLASS_1(1),

    /**
     * Message Class 2
     */
    MESSAGE_CLASS_2(2),

    /**
     * Message Class 3
     */
    MESSAGE_CLASS_3(3);
 
    private final int messageClass;
 
    private MessageClass(int messageClass) {
        this.messageClass = messageClass;
    }

    public int getMessageClass() {
        return this.messageClass;
    }

}
