/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.commerce;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Ignore
public class InventoryServiceIntegrationTest {


    @Test
    public void testUploadInventory() throws URISyntaxException {
        String testInventoryXml = "<inventory><platform>zen-1.3</platform><products><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>299.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Matrox G200 MMS</name><description>Reinforcing its position as a multi-monitor trailblazer, Matrox Graphics Inc. has once again developed the most flexible and highly advanced solution in the industry. Introducing the new Matrox G200 Multi-Monitor Series; the first graphics card ever to support up to four DVI digital flat panel displays on a single 8&amp;quot; PCI board.&lt;br /&gt;&lt;br /&gt;With continuing demand for digital flat panels in the financial workplace, the Matrox G200 MMS is the ultimate in flexible solutions. The Matrox G200 MMS also supports the new digital video interface (DVI) created by the Digital Display Working Group (DDWG) designed to ease the adoption of digital flat panels. Other configurations include composite video capture ability and onboard TV tuner, making the Matrox G200 MMS the complete solution for business needs.&lt;br /&gt;&lt;br /&gt;Based on the award-winning MGA-G200 graphics chip, the Matrox G200 Multi-Monitor Series provides superior 2D/3D graphics acceleration to meet the demanding needs of business applications such as real-time stock quotes (Versus), live video feeds (Reuters &amp; Bloombergs), multiple windows applications, word processing, spreadsheets and CAD.</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/matrox/mg200mms.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>4</category></categories><sku>1</sku><quantity>31</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>499.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Matrox G400 32MB</name><description>Dramatically Different High Performance Graphics&lt;br /&gt;&lt;br /&gt;Introducing the Millennium G400 Series - a dramatically different, high performance graphics experience. Armed with the industry's fastest graphics chip, the Millennium G400 Series takes explosive acceleration two steps further by adding unprecedented image quality, along with the most versatile display options for all your 3D, 2D and DVD applications. As the most powerful and innovative tools in your PC's arsenal, the Millennium G400 Series will not only change the way you see graphics, but will revolutionize the way you use your computer.&lt;br /&gt;&lt;br /&gt;Key features:New Matrox G400 256-bit DualBus graphics chipExplosive 3D, 2D and DVD performanceDualHead DisplaySuperior DVD and TV output3D Environment-Mapped Bump MappingVibrant Color Quality rendering UltraSharp DAC of up to 360 MHz3D Rendering Array ProcessorSupport for 16 or 32 MB of memory</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/matrox/mg400-32mb.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>4</category></categories><sku>2</sku><quantity>31</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>39.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Microsoft IntelliMouse Pro</name><description>Every element of IntelliMouse Pro - from its unique arched shape to the texture of the rubber grip around its base - is the product of extensive customer and ergonomic research. Microsoft's popular wheel control, which now allows zooming and universal scrolling functions, gives IntelliMouse Pro outstanding comfort and efficiency.</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/microsoft/msimpro.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>9</category></categories><sku>3</sku><quantity>500</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>42.0000</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>The Replacement Killers</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).&lt;br /&gt;Languages: English, Deutsch.&lt;br /&gt;Subtitles: English, Deutsch, Spanish.&lt;br /&gt;Audio: Dolby Surround 5.1.&lt;br /&gt;Picture Format: 16:9 Wide-Screen.&lt;br /&gt;Length: (approx) 80 minutes.&lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/replacement_killers.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>4</sku><quantity>12</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>30.0000</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Blade Runner - Director's Cut Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).&lt;br /&gt;Languages: English, Deutsch.&lt;br /&gt;Subtitles: English, Deutsch, Spanish.&lt;br /&gt;Audio: Dolby Surround 5.1.&lt;br /&gt;Picture Format: 16:9 Wide-Screen.&lt;br /&gt;Length: (approx) 112 minutes.&lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/blade_runner.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>11</category></categories><sku>5</sku><quantity>15</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>30.0000</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>The Matrix Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).\n" +
                "&lt;br /&gt;\n" +
                "Languages: English, Deutsch.\n" +
                "&lt;br /&gt;\n" +
                "Subtitles: English, Deutsch.\n" +
                "&lt;br /&gt;\n" +
                "Audio: Dolby Surround.\n" +
                "&lt;br /&gt;\n" +
                "Picture Format: 16:9 Wide-Screen.\n" +
                "&lt;br /&gt;\n" +
                "Length: (approx) 131 minutes.\n" +
                "&lt;br /&gt;\n" +
                "Other: Interactive Menus, Chapter Selection, Making Of.</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/the_matrix.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>6</sku><quantity>8</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>34.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>You've Got Mail Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa). &lt;br /&gt;Languages: English, Deutsch, Spanish. &lt;br /&gt;Subtitles: English, Deutsch, Spanish, French, Nordic, Polish. &lt;br /&gt;Audio: Dolby Digital 5.1. &lt;br /&gt;Picture Format: 16:9 Wide-Screen. &lt;br /&gt;Length: (approx) 115 minutes. &lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/youve_got_mail.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>12</category></categories><sku>7</sku><quantity>500</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>35.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>A Bug's Life Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa). &lt;br /&gt;Languages: English, Deutsch. &lt;br /&gt;Subtitles: English, Deutsch, Spanish. &lt;br /&gt;Audio: Dolby Digital 5.1 / Dolby Surround Stereo. &lt;br /&gt;Picture Format: 16:9 Wide-Screen. &lt;br /&gt;Length: (approx) 91 minutes. &lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/a_bugs_life.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>13</category></categories><sku>8</sku><quantity>499</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>29.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Under Siege Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa). &lt;br /&gt;Languages: English, Deutsch. &lt;br /&gt;Subtitles: English, Deutsch, Spanish. &lt;br /&gt;Audio: Dolby Surround 5.1. &lt;br /&gt;Picture Format: 16:9 Wide-Screen. &lt;br /&gt;Length: (approx) 98 minutes. &lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/under_siege.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>9</sku><quantity>10</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>29.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Under Siege 2 - Dark Territory</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).\n" +
                "&lt;br /&gt;\n" +
                "Languages: English, Deutsch.\n" +
                "&lt;br /&gt;\n" +
                "Subtitles: English, Deutsch, Spanish.\n" +
                "&lt;br /&gt;\n" +
                "Audio: Dolby Surround 5.1.\n" +
                "&lt;br /&gt;\n" +
                "Picture Format: 16:9 Wide-Screen.\n" +
                "&lt;br /&gt;\n" +
                "Length: (approx) 98 minutes.\n" +
                "&lt;br /&gt;\n" +
                "Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/under_siege2.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>10</sku><quantity>9</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>29.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Fire Down Below Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).\n" +
                "&lt;br /&gt;\n" +
                "Languages: English, Deutsch.\n" +
                "&lt;br /&gt;\n" +
                "Subtitles: English, Deutsch, Spanish.\n" +
                "&lt;br /&gt;\n" +
                "Audio: Dolby Surround 5.1.\n" +
                "&lt;br /&gt;\n" +
                "Picture Format: 16:9 Wide-Screen.\n" +
                "&lt;br /&gt;\n" +
                "Length: (approx) 100 minutes.\n" +
                "&lt;br /&gt;\n" +
                "Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/fire_down_below.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>11</sku><quantity>10</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>39.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Die Hard With A Vengeance Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa). &lt;br /&gt;Languages: English, Deutsch. &lt;br /&gt;Subtitles: English, Deutsch, Spanish. &lt;br /&gt;Audio: Dolby Surround 5.1. &lt;br /&gt;Picture Format: 16:9 Wide-Screen. &lt;br /&gt;Length: (approx) 122 minutes. &lt;br /&gt;Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/die_hard_3.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>12</sku><quantity>9</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product><product><tags><tag><name>tag1</name><status>ENABLED</status></tag></tags><ownerType>PRODUCT</ownerType><total><amount>34.9900</amount><language>english</language><locale>{Not implemented}</locale><currency>USD</currency></total><supplier><ownerType>MERCHANT</ownerType><name>Merchant Name</name><integrationGuid>merchantId</integrationGuid></supplier><name>Lethal Weapon Linked</name><description>Regional Code: 2 (Japan, Europe, Middle East, South Africa).\n" +
                "&lt;br /&gt;\n" +
                "Languages: English, Deutsch.\n" +
                "&lt;br /&gt;\n" +
                "Subtitles: English, Deutsch, Spanish.\n" +
                "&lt;br /&gt;\n" +
                "Audio: Dolby Surround 5.1.\n" +
                "&lt;br /&gt;\n" +
                "Picture Format: 16:9 Wide-Screen.\n" +
                "&lt;br /&gt;\n" +
                "Length: (approx) 100 minutes.\n" +
                "&lt;br /&gt;\n" +
                "Other: Interactive Menus, Chapter Selection, Subtitles (more languages).</description><images><supplierLargeImageLocation>http://bb.mytestingserver.net/images/dvd/lethal_weapon.gif</supplierLargeImageLocation><supplierTinyImageLocation>{Not implemented}</supplierTinyImageLocation></images><categories><category>10</category></categories><sku>13</sku><quantity>10</quantity><currentQuantity>0</currentQuantity><minimumOrderQuantity>1</minimumOrderQuantity></product>" +
                "</products>" +
                "</inventory>";

        String merchantGuid = "TestGuid";
        Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();

        POJOResourceFactory noDefaults = new POJOResourceFactory(InventoryService.class);
//        noDefaults.createResource()
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        {
            MockHttpRequest request = MockHttpRequest.put("/uploadInventory/" + merchantGuid);
            request.contentType("text/xml");
            request.content(testInventoryXml.getBytes());
            MockHttpResponse response = new MockHttpResponse();

            dispatcher.invoke(request, response);


            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
            assertEquals("basic", response.getContentAsString());
        }
    }
}