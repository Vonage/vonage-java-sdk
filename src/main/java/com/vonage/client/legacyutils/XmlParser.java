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
        documentBuilderLock.lock();
        try {
            if (documentBuilder == null) {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
            }
            doc = XmlUtil.parseXmlString(documentBuilder, xml);
        } catch (ParserConfigurationException e) {
            throw new VonageResponseParseException("Exception initialing XML parser", e);
        } finally {
            documentBuilderLock.unlock();
        }
        return doc;
    }
}
