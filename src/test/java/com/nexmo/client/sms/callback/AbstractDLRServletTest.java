package com.nexmo.client.sms.callback;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class TestDLRServlet extends AbstractDLRServlet {

    private static final long serialVersionUID = 8355386439151956651L;

    public DLR receipt;

    TestDLRServlet() {
        this(false, null, false, null, null);
    }

    TestDLRServlet(final boolean validateSignature,
                   final String signatureSharedSecret,
                   final boolean validateUsernamePassword,
                   final String expectedUsername,
                   final String expectedPassword) {
        super(validateSignature, signatureSharedSecret, validateUsernamePassword, expectedUsername, expectedPassword);
        this.consumer = new SynchronousExecutor();
    }

    @Override
    public void consume(DLR parsedObject) {
        this.receipt = parsedObject;
    }
}


public class AbstractDLRServletTest {

    public AbstractDLRServletTest() {
    }

    @Test
    public void testHandleValidEmptyRequest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        Enumeration<String> paramNames = mock(Enumeration.class);
        when(paramNames.hasMoreElements()).thenReturn(false);
        when(request.getParameterNames()).thenReturn(paramNames);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).setStatus(200);
        assertEquals("OK", dummyResponseWriter.toString());
    }

    @Test
    public void testHandleMissingFieldsRequest() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("messageId")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Missing mandatory fields");
    }

    @Test
    public void testHandleValidTextRequest() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestDLRServlet servlet = new TestDLRServlet();
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        DLR receipt = servlet.receipt;
        testDummyReceipt(receipt);
    }

    @Test
    public void testHandleBadStatus() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("status")).thenReturn("fluffy");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Unrecognized delivery status: fluffy");
    }


    @Test
    public void testHandleBadPrice() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("price")).thenReturn("not a number");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad price field");
    }

    @Test
    public void testHandleBadTimestamp() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("message-timestamp")).thenReturn("this is not a date-time");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad message-timestamp format");
    }

    @Test
    public void testHandleBadScts() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("scts")).thenReturn("this is not a date-time");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad scts format");
    }

    @Test
    public void testHandleValidateUsernamePassword() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestDLRServlet servlet = new TestDLRServlet(false, null, true, "admin", "default");
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        testDummyReceipt(servlet.receipt);
    }

    /**
     * If validateUserCredentials is true, and the servlet is instantiated with null username and password,
     * no validation is actually done.
     */
    @Test
    public void testHandleValidateNullCredentials() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestDLRServlet servlet = new TestDLRServlet(false, null, true, null, null);
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        testDummyReceipt(servlet.receipt);
    }

    @Test
    public void testHandleMissingUsername() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectUsername() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleMissingPassword() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectPassword() throws IOException, ServletException {
        HttpServletRequest request = createDummyRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestDLRServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    private static HttpServletRequest createDummyRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("to")).thenReturn("to");
        when(request.getParameter("network-code")).thenReturn("networkcode");
        when(request.getParameter("messageId")).thenReturn("messageid");
        when(request.getParameter("msisdn")).thenReturn("anisdn");
        when(request.getParameter("status")).thenReturn(DLR.DELIVERY_STATUS.ACCEPTED.getStatus());
        when(request.getParameter("err-code")).thenReturn("0");
        when(request.getParameter("price")).thenReturn("0.0448");
        when(request.getParameter("scts")).thenReturn("1101181426");//2011 Jan 18th 14:26 - YYMMDDHHMM
        when(request.getParameter("message-timestamp")).thenReturn("2012-04-05 09:22:57");// YYYY-MM-DD HH:MM:SS
        when(request.getParameter("client-ref")).thenReturn("client-ref");

        Enumeration<String> paramNames = mock(Enumeration.class);
        when(paramNames.hasMoreElements()).thenReturn(true);
        when(request.getParameterNames()).thenReturn(paramNames);

        return request;
    }

    private static void testDummyReceipt(DLR receipt) {
        assertEquals("to", receipt.getSender());
        assertEquals("anisdn", receipt.getDestination());
        assertEquals("networkcode", receipt.getNetworkCode());
        assertEquals("messageid", receipt.getMessageId());
        assertEquals(DLR.DELIVERY_STATUS.ACCEPTED, receipt.getStatus());
        assertEquals(DLR.ERR_CODE_DELIVERED, receipt.getErrorCode().intValue());
        assertEquals(0, new BigDecimal("0.0448").compareTo(receipt.getPrice()));
        assertEquals(new GregorianCalendar(2011, 0, 18, 14, 26, 0).getTime(), receipt.getScts());
        assertEquals(new GregorianCalendar(2012, 3, 5, 9, 22, 57).getTime(), receipt.getTimeStamp());
        assertEquals("client-ref", receipt.getClientRef());
    }

}