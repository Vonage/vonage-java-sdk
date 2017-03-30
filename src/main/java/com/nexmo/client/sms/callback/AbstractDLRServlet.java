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


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * An abstract Servlet that receives and parses an incoming callback request for a Delivery Receipt.
 * This class parses and validates the request, optionally checks any provided signature or credentials,
 * and constructs an MO object for your subclass to consume.
 * <p>
 * Note: This servlet will immediately ack the callback as soon as it is validated. Your subclass will
 * consume the callback object asynchronously. This is because it is important to keep latency of
 * the acknowledgement to a minimum in order to maintain throughput when operating at any sort of volume.
 * You are responsible for persisting this object in the event of any failure whilst processing
 *
 */
public abstract class AbstractDLRServlet extends AbstractNexmoServlet<DeliveryReceiptRequest> {

    private static final long serialVersionUID = -781240891967430458L;

    private static final ThreadLocal<SimpleDateFormat> SCTS_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyMMddHHmm");
        }
    };

    public AbstractDLRServlet(boolean validateSignature,
                              String signatureSharedSecret,
                              boolean validateUsernamePassword,
                              String expectedUsername,
                              String expectedPassword) {
        super(validateSignature, signatureSharedSecret, validateUsernamePassword, expectedUsername, expectedPassword);
    }

    @Override
    protected DeliveryReceiptRequest parseRequest(HttpServletRequest request) throws NexmoCallbackRequestValidationException {
        String sender = request.getParameter("to");
        String destination = request.getParameter("msisdn");
        String messageId = request.getParameter("messageId");
        if (sender == null || destination == null || messageId == null) {
            throw new NexmoCallbackRequestValidationException("Missing mandatory fields");
        }
        DeliveryReceiptRequest.DELIVERY_STATUS status = parseStatus(request.getParameter("status"));

        Integer errorCode = parseErrorCode(request.getParameter("err-code"));

        String networkCode = request.getParameter("network-code");
        BigDecimal price = parsePrice(request.getParameter("price"));
        Date scts = parseTimeStamp(request, SCTS_DATE_FORMAT.get(), "scts");
        Date timeStamp = parseTimeStamp(request, "message-timestamp");
        String clientRef = request.getParameter("client-ref");

        return new DeliveryReceiptRequest(sender, destination, messageId, networkCode, status, errorCode, price, scts,
                                          timeStamp, clientRef);
    }

    private static DeliveryReceiptRequest.DELIVERY_STATUS parseStatus(String str)
            throws NexmoCallbackRequestValidationException {
        if (str != null) {
            for (DeliveryReceiptRequest.DELIVERY_STATUS status : DeliveryReceiptRequest.DELIVERY_STATUS.values()) {
                if (Objects.equals(status.getStatus(), str)) {
                    return status;
                }
            }
            throw new NexmoCallbackRequestValidationException("Unrecognized delivery status: " + str);
        }
        throw new NexmoCallbackRequestValidationException("Missing required delivery status");
    }

    private static Integer parseErrorCode(String str) throws NexmoCallbackRequestValidationException {
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ex) {
                throw new NexmoCallbackRequestValidationException("Un-parsable error code: " + str, ex);
            }
        }
        return null;
    }


}
