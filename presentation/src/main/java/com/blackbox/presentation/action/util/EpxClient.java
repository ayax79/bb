package com.blackbox.presentation.action.util;

import com.blackbox.foundation.billing.Money;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * @author A.J. Wright
 */
public interface EpxClient {
    String getTac(Money amount, long transactionId) throws IOException, XMLStreamException, SAXException, XPathExpressionException, ParserConfigurationException;

    String formatMoney(Money money);
}
