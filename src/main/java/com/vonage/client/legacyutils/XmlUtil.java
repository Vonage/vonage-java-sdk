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
package com.vonage.client.legacyutils;

import com.vonage.client.VonageResponseParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.StringReader;

public class XmlUtil {

    public static String stringValue(Node node) {
        return node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
    }

    public static int intValue(Node node) throws VonageResponseParseException {
        String str = stringValue(node);
        if (str != null) {
            return Integer.parseInt(str, 10);
        } else {
            throw new VonageResponseParseException("Null or empty value provided for numeric value: " + node.getNodeName());
        }
    }

    public static Document parseXmlString(final DocumentBuilder documentBuilder,
                                          final String response) throws VonageResponseParseException {
        try {
            return documentBuilder.parse(new InputSource(new StringReader(response)));
        } catch (SAXException se) {
            throw new VonageResponseParseException("XML parse failure", se);
        } catch (IOException ioe) {
            // Should never happen:
            throw new VonageResponseParseException("IOException while parsing response XML!", ioe);
        }
    }
}
