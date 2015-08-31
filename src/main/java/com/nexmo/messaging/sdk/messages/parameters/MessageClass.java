package com.nexmo.messaging.sdk.messages.parameters;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
