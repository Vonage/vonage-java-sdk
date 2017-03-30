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


import com.nexmo.client.auth.RequestSigning;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * An abstract Servlet that receives and parses an incoming callback request.
 * This class parses and validates the request, optionally checks any provided signature or credentials,
 * and constructs an appropriate object for your subclass to consume.
 * <p>
 * Note: This servlet will immediately ack the callback as soon as it is validated. Your subclass will
 * consume the callback object asynchronously. This is because it is important to keep latency of
 * the acknowledgement to a minimum in order to maintain throughput when operating at any sort of volume.
 * You are responsible for persisting this object in the event of any failure whilst processing
 */
public abstract class AbstractNexmoServlet<T extends Serializable> extends HttpServlet {

    private static final int MAX_CONSUMER_THREADS = 10;

    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static final long serialVersionUID = 2540729456602389749L;

    private final boolean validateSignature;
    private final String signatureSharedSecret;
    private final boolean validateUsernamePassword;
    private final String expectedUsername;
    private final String expectedPassword;

    protected Executor consumer;

    public AbstractNexmoServlet(final boolean validateSignature,
                                final String signatureSharedSecret,
                                final boolean validateUsernamePassword,
                                final String expectedUsername,
                                final String expectedPassword) {
        this.validateSignature = validateSignature;
        this.signatureSharedSecret = signatureSharedSecret;
        this.validateUsernamePassword = validateUsernamePassword;
        this.expectedUsername = expectedUsername;
        this.expectedPassword = expectedPassword;

        this.consumer = Executors.newFixedThreadPool(MAX_CONSUMER_THREADS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void validateRequest(HttpServletRequest request) throws NexmoCallbackRequestValidationException {
        boolean passed = true;
        if (this.validateUsernamePassword) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (this.expectedUsername != null) {
                if (username == null || !this.expectedUsername.equals(username)) {
                    passed = false;
                }
            }
            if (this.expectedPassword != null) {
                if (password == null || !this.expectedPassword.equals(password)) {
                    passed = false;
                }
            }
        }

        if (!passed) {
            throw new NexmoCallbackRequestValidationException("Bad Credentials");
        }

        if (this.validateSignature) {
            if (!RequestSigning.verifyRequestSignature(request, this.signatureSharedSecret)) {
                throw new NexmoCallbackRequestValidationException("Bad Signature");
            }
        }
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try {
            validateRequest(request);

            Enumeration<String> paramNames = request.getParameterNames();
            //NOTE: When no parameters are defined we simply return a 200 response as this is required in order
            //to be able to set the callback URL at the Web admin interface, otherwise it is NOT saved
            if (paramNames.hasMoreElements()) {
                T bean = parseRequest(request);
                // Push the task to an async consumption thread
                ConsumeTask<T> task = new ConsumeTask<>(this, bean);
                this.consumer.execute(task);
            }
            // immediately ack the receipt
            response.setStatus(HttpServletResponse.SC_OK);
            try (PrintWriter out = response.getWriter()) {
                out.print("OK");
                out.flush();
            }
        } catch (NexmoCallbackRequestValidationException exc) {
            // TODO: Log this - it's mainly for our own use!
            response.sendError(400, exc.getMessage());
        }
    }

    protected abstract T parseRequest(HttpServletRequest request) throws NexmoCallbackRequestValidationException;

    protected static Date parseTimeStamp(ServletRequest request, String parameterName)
            throws NexmoCallbackRequestValidationException {
        return parseTimeStamp(request, TIMESTAMP_DATE_FORMAT.get(), parameterName);
    }

    protected static Date parseTimeStamp(ServletRequest request, DateFormat df, String parameterName)
            throws NexmoCallbackRequestValidationException {
        return parseTimeStamp(df, parameterName, request.getParameter(parameterName));
    }

    protected static Date parseTimeStamp(DateFormat df, String parameterName, String value)
            throws NexmoCallbackRequestValidationException {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                throw new NexmoCallbackRequestValidationException("Bad " + parameterName + " format", e);
            }
        }
        return null;
    }

    protected static BigDecimal parsePrice(String str) throws NexmoCallbackRequestValidationException {
        if (str != null) {
            try {
                return new BigDecimal(str);
            } catch (Exception e) {
                throw new NexmoCallbackRequestValidationException("Bad price field", e);
            }
        }
        return null;
    }

    /**
     * This is the task that is pushed to the thread pool upon receipt of an incoming callback. It detaches the
     * consumption of the MO from the acknowledgement of the incoming http request.
     */
    private static final class ConsumeTask<T extends Serializable> implements Runnable, Serializable {

        private static final long serialVersionUID = -6149806075817800033L;

        private final AbstractNexmoServlet<T> parent;
        private final T mo;

        private ConsumeTask(final AbstractNexmoServlet<T> parent, final T mo) {
            this.parent = parent;
            this.mo = mo;
        }

        @Override
        public void run() {
            this.parent.consume(this.mo);
        }
    }

    /**
     * This method is asynchronously passed a complete parsed object instance to be dealt with by your application logic
     *
     * @param parsedObject The object that was provided in the HTTP request.
     */
    public abstract void consume(T parsedObject);

}
