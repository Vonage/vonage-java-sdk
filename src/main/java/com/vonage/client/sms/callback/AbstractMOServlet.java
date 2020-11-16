/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.sms.callback;


import com.vonage.client.auth.RequestSigning;
import com.vonage.client.auth.hashutils.HashUtil;
import com.vonage.client.sms.HexUtil;
import com.vonage.client.sms.callback.messages.MO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
 *
 */
public abstract class AbstractMOServlet extends HttpServlet {

    private static final long serialVersionUID = 8745764381059238419L;

    private static final int MAX_CONSUMER_THREADS = 10;

    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss"));

    private final boolean validateSignature;
    private final String signatureSharedSecret;
    private final boolean validateUsernamePassword;
    private final String expectedUsername;
    private final String expectedPassword;
    private final HashUtil.HashType hashType;

    protected Executor consumer;

    public AbstractMOServlet(final boolean validateSignature, final String signatureSharedSecret, final boolean validateUsernamePassword, final String expectedUsername, final String expectedPassword) {
        hashType = HashUtil.HashType.MD5;
        consumer = Executors.newFixedThreadPool(MAX_CONSUMER_THREADS);

        this.validateSignature = validateSignature;
        this.signatureSharedSecret = signatureSharedSecret;
        this.validateUsernamePassword = validateUsernamePassword;
        this.expectedUsername = expectedUsername;
        this.expectedPassword = expectedPassword;

    }

    public AbstractMOServlet(final boolean validateSignature,
                             final String signatureSharedSecret,
                             final boolean validateUsernamePassword,
                             final String expectedUsername,
                             final String expectedPassword,
                             HashUtil.HashType hashType) {
        this.validateSignature = validateSignature;
        this.signatureSharedSecret = signatureSharedSecret;
        this.validateUsernamePassword = validateUsernamePassword;
        this.expectedUsername = expectedUsername;
        this.expectedPassword = expectedPassword;
        this.hashType = hashType;

        consumer = Executors.newFixedThreadPool(MAX_CONSUMER_THREADS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void validateRequest(HttpServletRequest request) throws VonageCallbackRequestValidationException {
        boolean passed = true;
        if (validateUsernamePassword) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (expectedUsername != null) if (!expectedUsername.equals(username)) passed = false;
            if (expectedPassword != null) if (!expectedPassword.equals(password)) passed = false;
        }

        if (!passed) {
            throw new VonageCallbackRequestValidationException("Bad Credentials");
        }

        if (validateSignature) {
            if (!RequestSigning.verifyRequestSignature(request, signatureSharedSecret, hashType)) {
                throw new VonageCallbackRequestValidationException("Bad Signature");
            }
        }
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try {
            validateRequest(request);

            String messageId = request.getParameter("messageId");
            String sender = request.getParameter("msisdn");
            String destination = request.getParameter("to");
            if (sender == null || destination == null || messageId == null) {
                throw new VonageCallbackRequestValidationException("Missing mandatory fields");
            }

            MO.MESSAGE_TYPE messageType = parseMessageType(request.getParameter("type"));

            BigDecimal price = parsePrice(request.getParameter("price"));
            Date timeStamp = parseTimeStamp(request.getParameter("message-timestamp"));

            MO mo = new MO(messageId, messageType, sender, destination, price, timeStamp);
            if (messageType == MO.MESSAGE_TYPE.TEXT || messageType == MO.MESSAGE_TYPE.UNICODE) {
                String messageBody = request.getParameter("text");
                if (messageBody == null) {
                    throw new VonageCallbackRequestValidationException("Missing text field");
                }
                mo.setTextData(messageBody, request.getParameter("keyword"));
            } else if (messageType == MO.MESSAGE_TYPE.BINARY) {
                byte[] data = parseBinaryData(request.getParameter("data"));
                if (data == null) {
                    throw new VonageCallbackRequestValidationException("Missing data field");
                }
                mo.setBinaryData(data, parseBinaryData(request.getParameter("udh")));
            }
            extractConcatenationData(request, mo);

            // TODO: These are undocumented:
            mo.setNetworkCode(request.getParameter("network-code"));
            mo.setSessionId(request.getParameter("sessionId"));

            // Push the task to an async consumption thread
            ConsumeTask task = new ConsumeTask(this, mo);
            consumer.execute(task);

            // immediately ack the receipt
            try (PrintWriter out = response.getWriter()) {
                out.print("OK");
                out.flush();
            }
        } catch (VonageCallbackRequestValidationException exc) {
            // TODO: Log this - it's mainly for our own use!
            response.sendError(400, exc.getMessage());
        }
    }

    private static void extractConcatenationData(HttpServletRequest request, MO mo) throws VonageCallbackRequestValidationException {
        String concatString = request.getParameter("concat");
        if (concatString != null && concatString.equals("true")) {
            int totalParts;
            int partNumber;
            String reference = request.getParameter("concat-ref");
            try {
                totalParts = Integer.parseInt(request.getParameter("concat-total"));
                partNumber = Integer.parseInt(request.getParameter("concat-part"));
            } catch (Exception e) {
                throw new VonageCallbackRequestValidationException("bad concat fields");
            }
            mo.setConcatenationData(reference, totalParts, partNumber);
        }
    }

    private static MO.MESSAGE_TYPE parseMessageType(String str) throws VonageCallbackRequestValidationException {
        if (str != null) for (MO.MESSAGE_TYPE type : MO.MESSAGE_TYPE.values())
            if (type.getType().equals(str)) return type;
        throw new VonageCallbackRequestValidationException("Unrecognized message type: " + str);
    }

    private static Date parseTimeStamp(String str) throws VonageCallbackRequestValidationException {
        if (str != null) {
            try {
                return TIMESTAMP_DATE_FORMAT.get().parse(str);
            } catch (ParseException e) {
                throw new VonageCallbackRequestValidationException("Bad message-timestamp format", e);
            }
        }
        return null;
    }

    private static BigDecimal parsePrice(String str) throws VonageCallbackRequestValidationException {
        if (str != null) {
            try {
                return new BigDecimal(str);
            } catch (Exception e) {
                throw new VonageCallbackRequestValidationException("Bad price field", e);
            }
        }
        return null;
    }

    private static byte[] parseBinaryData(String str) {
        if (str != null) return HexUtil.hexToBytes(str);
        return null;
    }

    /**
     * This is the task that is pushed to the thread pool upon receipt of an incoming MO callback
     * It detaches the consumption of the MO from the acknowledgement of the incoming http request
     */
    private static final class ConsumeTask implements Runnable, java.io.Serializable {

        private static final long serialVersionUID = -5270583545977374866L;

        private final AbstractMOServlet parent;
        private final MO mo;

        public ConsumeTask(final AbstractMOServlet parent, final MO mo) {
            this.parent = parent;
            this.mo = mo;
        }

        @Override
        public void run() {
            parent.consume(mo);
        }
    }

    /**
     * This method is asynchronously passed a complete MO instance to be dealt with by your application logic
     *
     * @param mo The message object that was provided in the HTTP request.
     */
    public abstract void consume(MO mo);

}
