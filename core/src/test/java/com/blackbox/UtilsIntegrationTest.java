/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox;

import static org.junit.Assert.assertEquals;

import com.blackbox.foundation.Utils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class UtilsIntegrationTest {

    @Test
    public void checkUrl200() throws IOException {
        int result = Utils.checkUrl("http://www.google.com");
        assertEquals(200, result);
    }

    @Test
    public void checkUrl404() throws IOException {
        int result = Utils.checkUrl("http://www.cnn.com/greenpoopoo");
        assertEquals(404, result);

    }
}