package edu.harvard.mcz.precapture.test;

import edu.harvard.mcz.precapture.utils.GZipCompressor;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the GZipCompressor
 */
public class TestGZipCompressor {

    @Test
    public void testGZipCompressor() {
        String testString = "alsd290a .!as321'";
        String result = "";
        try {
            byte[] compressed = GZipCompressor.compress(testString);
            result = GZipCompressor.decompress(compressed);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertTrue(result.equals(testString));
    }
}
