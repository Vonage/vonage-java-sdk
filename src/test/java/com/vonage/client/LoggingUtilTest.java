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
package com.vonage.client;

import com.vonage.client.logging.LoggingUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LoggingUtilTest {

  @BeforeClass
  public static void setupBeforeClass() {
    TestUtils.unmockStaticLoggingUtils();
  }

  @AfterClass
  public static void cleanupAfterClass() {
    TestUtils.mockStaticLoggingUtils();
  }

  @Test
  public void testNoContentResponseMethod() {
    HttpResponse stubResponse = new BasicHttpResponse(
        new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 204, "NO CONTENT")
    );
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(null);
    stubResponse.setEntity(null);

    try {
      LoggingUtils.logResponse(stubResponse);
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
