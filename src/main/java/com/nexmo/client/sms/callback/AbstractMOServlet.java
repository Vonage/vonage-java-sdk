/*
 * Copyright (c) 2011-2017 Nexmo Inc
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

package com.nexmo.client.sms.callback;


import com.nexmo.client.sms.HexUtil;
import com.nexmo.client.sms.callback.messages.MO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * An abstract Servlet that receives and parses an incoming callback request for an MO message.
 * This class parses and validates the request, optionally checks any provided signature or credentials,
 * and constructs an MO object for your subclass to consume.
 * <p>
 * Note: This servlet will immediately ack the callback as soon as it is validated. Your subclass will
 * consume the callback object asynchronously. This is because it is important to keep latency of
 * the acknowledgement to a minimum in order to maintain throughput when operating at any sort of volume.
 * You are responsible for persisting this object in the event of any failure whilst processing
 *
 * @author Paul Cook
 */
public abstract class AbstractMOServlet extends AbstractNexmoServlet<MO> {

    private static final long serialVersionUID = -4123799651425169053L;

    protected AbstractMOServlet(boolean validateSignature,
                                String signatureSharedSecret,
                                boolean validateUsernamePassword,
                                String expectedUsername,
                                String expectedPassword) {
        super(validateSignature, signatureSharedSecret, validateUsernamePassword, expectedUsername, expectedPassword);
    }

    @Override
    protected MO parseRequest(HttpServletRequest request) throws NexmoCallbackRequestValidationException {
        String messageId = request.getParameter("messageId");
        String sender = request.getParameter("msisdn");
        String destination = request.getParameter("to");
        if (sender == null ||
            destination == null ||
            messageId == null) {
            throw new NexmoCallbackRequestValidationException("Missing mandatory fields");
        }

        MO.MESSAGE_TYPE messageType = parseMessageType(request.getParameter("type"));

        BigDecimal price = parsePrice(request.getParameter("price"));
        Date timeStamp = parseTimeStamp(request, "message-timestamp");

        MO mo = new MO(messageId, messageType, sender, destination, price, timeStamp);
        if (messageType == MO.MESSAGE_TYPE.TEXT || messageType == MO.MESSAGE_TYPE.UNICODE) {
            String messageBody = request.getParameter("text");
            if (messageBody == null) {
                throw new NexmoCallbackRequestValidationException("Missing text field");
            }
            mo.setTextData(messageBody, request.getParameter("keyword"));
        } else if (messageType == MO.MESSAGE_TYPE.BINARY) {
            byte[] data = parseBinaryData(request.getParameter("data"));
            if (data == null) {
                throw new NexmoCallbackRequestValidationException("Missing data field");
            }
            mo.setBinaryData(data, parseBinaryData(request.getParameter("udh")));
        }
        extractConcatenationData(request, mo);

        // TODO: These are undocumented:
        mo.setNetworkCode(request.getParameter("network-code"));
        mo.setSessionId(request.getParameter("sessionId"));
        return mo;
    }

    private static void extractConcatenationData(HttpServletRequest request, MO mo)
            throws NexmoCallbackRequestValidationException {
        String concatString = request.getParameter("concat");
        if (concatString != null && concatString.equals("true")) {
            int totalParts;
            int partNumber;
            String reference = request.getParameter("concat-ref");
            try {
                totalParts = Integer.parseInt(request.getParameter("concat-total"));
                partNumber = Integer.parseInt(request.getParameter("concat-part"));
            } catch (Exception e) {
                throw new NexmoCallbackRequestValidationException("bad concat fields");
            }
            mo.setConcatenationData(reference, totalParts, partNumber);
        }
    }

    private static MO.MESSAGE_TYPE parseMessageType(String str) throws NexmoCallbackRequestValidationException {
        if (str != null) {
            for (MO.MESSAGE_TYPE type : MO.MESSAGE_TYPE.values()) {
                if (Objects.equals(type.getType(), str)) {
                    return type;
                }
            }
        }
        throw new NexmoCallbackRequestValidationException("Unrecognized message type: " + str);
    }

    private static byte[] parseBinaryData(String str) {
        if (str != null) {
            return HexUtil.hexToBytes(str);
        }
        return null;
    }

}
