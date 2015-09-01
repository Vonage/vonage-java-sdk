package com.nexmo.messaging.sdk.callback;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nexmo.common.util.HexUtil;
import com.nexmo.messaging.sdk.callback.messages.MO;
import com.nexmo.security.RequestSigning;

/**
 * AbstractMOServlet.java
 *
 * An abstract callback servlet that receives and parses an incoming callback request for an MO message.
 * This class parses and validates the request, optionally checks any signature supplied or credentials,
 * and constructs an MO object for your subclass implementation to consume.
 *
 * Note, this servlet will immediately ack the callback as soon as it is validated, your subclass will
 * consume the callback object asynchronously to this. This is because it is important to keep latency of
 * the acknowledgement to a minimum in order to maintain throughput when operating at any sort of volume.
 * You are responsible for persisting this object in the event of any failure whilst processing
 *
 * @author  Paul Cook
 * @version
 */
public abstract class AbstractMOServlet extends HttpServlet {

    static final long serialVersionUID = 8745764381059238419L;

    public static final int MAX_CONSUMER_THREADS = 10;

    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final boolean validateSignature;
    private final String signatureSharedSecret;
    private final boolean validateUsernamePassword;
    private final String expectedUsername;
    private final String expectedPassword;

    private final ThreadPoolExecutor consumer;

    public AbstractMOServlet(final boolean validateSignature,
                             final String signatureSharedSecret,
                             final boolean validateUsernamePassword,
                             final String expectedUsername,
                             final String expectedPassword) {
        this.validateSignature = validateSignature;
        this.signatureSharedSecret = signatureSharedSecret;
        this.validateUsernamePassword = validateUsernamePassword;
        this.expectedUsername = expectedUsername;
        this.expectedPassword = expectedPassword;

        this.consumer = (ThreadPoolExecutor)Executors.newFixedThreadPool(MAX_CONSUMER_THREADS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        if (this.validateUsernamePassword) {
            boolean failed = false;
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (this.expectedUsername != null)
                if (username == null || !this.expectedUsername.equals(username))
                    failed = true;
            if (this.expectedPassword != null)
                if (password == null || !this.expectedPassword.equals(password))
                    failed = true;
            if (failed) {
                response.sendError(400, "Bad Credentials");
                return;
            }
        }

        if (this.validateSignature) {
            if (!RequestSigning.verifyRequestSignature(request, this.signatureSharedSecret)) {
                response.sendError(400, "Bad Signature");
                return;
            }
        }

        String sender = request.getParameter("msisdn");
        String destination = request.getParameter("to");
        String networkCode = request.getParameter("network-code");
        String messageId = request.getParameter("messageId");
        String sessionId = request.getParameter("sessionId");
        String keyword = request.getParameter("keyword");

        if (sender == null ||
            destination == null ||
            messageId == null) {
            response.sendError(400, "Missing mandatory fields");
            return;
        }

        MO.MESSAGE_TYPE messageType = null;
        String str = request.getParameter("type");
        if (str != null)
            for (MO.MESSAGE_TYPE type: MO.MESSAGE_TYPE.values())
                if (type.getType().equals(str))
                    messageType = type;
        if (messageType == null) {
            response.sendError(400, "Unrecognized message type");
            return;
        }

        String messageBody = null;
        byte[] binaryMessageBody = null;
        byte[] userDataHeader = null;

        if (messageType == MO.MESSAGE_TYPE.TEXT || messageType == MO.MESSAGE_TYPE.UNICODE) {
            messageBody = request.getParameter("text");
            if (messageBody == null) {
                response.sendError(400, "Missing text fied");
                return;
            }
        } else if (messageType == MO.MESSAGE_TYPE.BINARY) {
            String binaryBodyStr = request.getParameter("data");
            if (binaryBodyStr == null) {
                response.sendError(400, "Missing data field");
                return;
            }
            binaryMessageBody = HexUtil.hexToBytes(binaryBodyStr);
        }

        String binaryBodyUdh = request.getParameter("udh");
        if (binaryBodyUdh != null)
            userDataHeader = HexUtil.hexToBytes(binaryBodyUdh);

        BigDecimal price = null;
        str = request.getParameter("price");
        if (str != null) {
            try {
                price = new BigDecimal(str);
            } catch (Exception e) {
                response.sendError(400, "Bad price field");
                return;
            }
        }

        Date timeStamp = null;
        str = request.getParameter("message-timestamp");
        if (str != null) {
            try {
                timeStamp = TIMESTAMP_DATE_FORMAT.get().parse(str);
            } catch (ParseException e) {
                response.sendError(400, "Bad message-timestamp format");
                return;
            }
        }

        boolean concat = false;
        String concatReferenceNumber = null;
        int concatTotalParts = 0;
        int concatPartNumber = 0;

        str = request.getParameter("concat");
        if (str != null && str.equals("true")) {
            concatReferenceNumber = request.getParameter("concat-ref");
            try {
                concatTotalParts = Integer.parseInt(request.getParameter("concat-total"));
                concatPartNumber = Integer.parseInt(request.getParameter("concat-part"));
            } catch (Exception e) {
                response.sendError(400, "bad concat fields");
                return;
            }
        }

        // Now we have validated all of the request parameters, construct an MO POJO and push it to the consumer thread

        MO mo = new MO(messageId,
                       messageType,
                       sender,
                       destination,
                       networkCode,
                       keyword,
                       messageBody,
                       binaryMessageBody,
                       userDataHeader,
                       price,
                       sessionId,
                       concat,
                       concatReferenceNumber,
                       concatTotalParts,
                       concatPartNumber,
                       timeStamp);

        // Push the task to an async consumption thread
        ConsumeTask task = new ConsumeTask(this, mo);
        this.consumer.execute(task);

        // immediately ack the receipt
        try (PrintWriter out = response.getWriter()) {
            out.print("OK");
            out.flush();
        }
    }

    /**
     * This is the task that is pushed to the thread pool upon receipt of an incoming MO callback
     * It detatches the consumption of the MO from the acknowledgement of the incoming http request
     */
    private static final class ConsumeTask implements Runnable, java.io.Serializable {

        private static final long serialVersionUID = -5270583545977374866L;

        private final AbstractMOServlet parent;
        private final MO mo;

        public ConsumeTask(final AbstractMOServlet parent,
                           final MO mo) {
            this.parent = parent;
            this.mo = mo;
        }

        @Override
        public void run() {
            this.parent.consume(this.mo);
        }
    }

    /**
     * This method is asynchronosly passed a complete MO instance to be dealt with by your application logic
     *
     * @param mo
     */
    public abstract void consume(MO mo);

}
