package com.blackbox.presentation.action.util;

import com.blackbox.billing.Money;
import static com.blackbox.presentation.action.util.PresentationUtil.getProperty;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.String.format;
import java.text.DecimalFormat;

/**
 * This class is kind of temporary, eventually it will be pulled out and moved into yes billing
 *
 * @author A.J. Wright
 */
public class DefaultEpxClient implements EpxClient {

    private String proxyHost;
    private Integer proxyPort;

    public DefaultEpxClient() {
    }

    public DefaultEpxClient(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public String getTac(Money amount, long transactionId) throws IOException, XMLStreamException, SAXException, XPathExpressionException, ParserConfigurationException {
        String mac = getProperty("epx.mac");
        String url = format(getProperty("epx.keyexchange.url"), mac, transactionId, formatMoney(amount));

        GetMethod get = new GetMethod(url);
        HttpClient client = new HttpClient();
        if (proxyHost != null && proxyPort != null) {
            client.getHostConfiguration().setProxy(proxyHost, proxyPort);
        }
        client.executeMethod(get);
        InputStream body = get.getResponseBodyAsStream();
        return parseTac(body);
    }

    String parseTac(InputStream xml) throws XMLStreamException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.evaluate("/RESPONSE/FIELDS/FIELD[@KEY='TAC']", d);
    }

    public String formatMoney(Money money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money.getAmount());
    }


}
