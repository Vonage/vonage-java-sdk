package com.vonage.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.vonage.client.logging.LoggingUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

public class LoggingUtilTest {

  @Test
  public void testNoContentResponseMethod() {

    HttpResponse stubResponse = new BasicHttpResponse(
        new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 204, "NO CONTENT")
    );
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(null);
    stubResponse.setEntity(null);


    try {
      String response = LoggingUtils.logResponse(stubResponse);
    } catch (Exception ex) {
      fail("LoggingUtils Failed for null Response");
    }
  }


  @Test
  public void testContentResponseMethod() {

    HttpResponse stub = TestUtils.makeJsonHttpResponse(200, "{\n" +
        "  \"value\": BODY_CONTENT,\n" +
        "  \"otherValue\": false\n" +
        "}}");

    try {
      String response = LoggingUtils.logResponse(stub);
      assertTrue((response.contains("BODY_CONTENT")));
    } catch (Exception ex) {
      fail("LoggingUtils Failed for Content Response");
    }
  }
}
