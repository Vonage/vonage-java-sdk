/*
 * Copyright (c) 2020 Vonage
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XmlParser {
    /**
     * A lock associated with {@link #documentBuilder}.
     */
    private final Lock documentBuilderLock = new ReentrantLock();

    /**
     * Used for parsing XML data.
     *
     * Do not use this without locking on {@link #documentBuilderLock}
     */
    private DocumentBuilder documentBuilder;

    /**
     * Parse a provided XML String and return the generated DOM Document.
     *
     * @param xml A String containing XML.
     * @return A Document generated from the parsed XML.
     * @throws VonageResponseParseException If there is a problem initializing the XML parser or parsing the XML.
     */
    public Document parseXml(String xml) throws VonageResponseParseException {
        // TODO: Maybe an Error subclass for XML initialization errors, as these are serious and unexpected.
        Document doc;
        this.documentBuilderLock.lock();
        try {
            if (this.documentBuilder == null) {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
            }
            doc = XmlUtil.parseXmlString(this.documentBuilder, xml);
        } catch (ParserConfigurationException e) {
            throw new VonageResponseParseException("Exception initialing XML parser", e);
        } finally {
            this.documentBuilderLock.unlock();
        }
        return doc;
    }
}
