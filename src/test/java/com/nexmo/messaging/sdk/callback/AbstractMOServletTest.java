package com.nexmo.messaging.sdk.callback;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.nexmo.messaging.sdk.callback.messages.MO;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;


class TestMOServlet extends AbstractMOServlet {
    TestMOServlet() {
        super(false, null, false, null, null);
    }

    TestMOServlet(final boolean validateSignature,
                         final String signatureSharedSecret,
                         final boolean validateUsernamePassword,
                         final String expectedUsername,
                         final String expectedPassword) {
        super(validateSignature, signatureSharedSecret, validateUsernamePassword, expectedUsername, expectedPassword);
    }

    @Override
    public void consume(MO mo) {

    }
}


public class AbstractMOServletTest {
    @Test
    public void testHandleEmptyRequest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Missing mandatory fields");
    }

    @Test
    public void testHandleValidTextRequest() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());
        // TODO: Validate produced MO object.
    }

    @Test
    public void testHandleValidUnicodeRequest() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("unicode");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());
        // TODO: Validate produced MO object.
    }

    @Test
    public void testHandleValidBinaryRequest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("msisdn")).thenReturn("anisdn");
        when(request.getParameter("to")).thenReturn("to");
        when(request.getParameter("network-code")).thenReturn("networkcode");
        when(request.getParameter("messageId")).thenReturn("messageid");
        when(request.getParameter("sessionId")).thenReturn("asessionid");
        when(request.getParameter("keyword")).thenReturn("akeyword");
        when(request.getParameter("type")).thenReturn("binary");
        when(request.getParameter("data")).thenReturn("01234567890abcde");
        when(request.getParameter("price")).thenReturn("12.00");
        when(request.getParameter("message-timestamp")).thenReturn("2016-10-09 08:07:06");
        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());
        // TODO: Validate produced MO object.
    }

    @Test
    public void testHandleBadPrice() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("price")).thenReturn("not a number");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad price field");
    }

    @Test
    public void testHandleBadMessageType() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("fluffy");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Unrecognized message type");
    }

    @Test
    public void testHandleMissingTextField() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("text")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Missing text field");
    }

    @Test
    public void testHandleBadTimestamp() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("message-timestamp")).thenReturn("this is not a date-time");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad message-timestamp format");
    }

    @Test
    public void testHandleValidateUsernamePassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());
        // TODO: Validate produced MO object.
    }

    @Test
    public void testHandleMissingUsername() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectUsername() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleMissingPassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectPassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    /**
     * If validateUserCredentials is true, and the servlet is instantiated with null username and password,
     * no validation is actually done.
     */
    @Test
    public void testHandleValidateNullCredentials() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, null, null).doPost(request, response);
        // TODO: Not sure this is correct behaviour!
        assertEquals("OK", dummyResponseWriter.toString());
        // TODO: Validate produced MO object.
    }

    private HttpServletRequest dummyTextRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("msisdn")).thenReturn("anisdn");
        when(request.getParameter("to")).thenReturn("to");
        when(request.getParameter("network-code")).thenReturn("networkcode");
        when(request.getParameter("messageId")).thenReturn("messageid");
        when(request.getParameter("sessionId")).thenReturn("asessionid");
        when(request.getParameter("keyword")).thenReturn("akeyword");
        when(request.getParameter("type")).thenReturn("text");
        when(request.getParameter("text")).thenReturn("Dear John");

        return request;
    }
}
