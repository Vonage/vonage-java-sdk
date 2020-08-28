/*
 * Copyright (c) 2011-2017 Vonage Inc
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
package com.nexmo.client.legacyutils;

import com.nexmo.client.VonageResponseParseException;
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
