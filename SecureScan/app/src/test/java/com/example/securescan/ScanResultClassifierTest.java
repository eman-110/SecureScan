package com.example.securescan;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScanResultClassifierTest {
    @Test
    public void classifiesHttpsLinksAsSecure() {
        assertEquals(ScanResultClassifier.Result.SECURE,
                ScanResultClassifier.classify("https://example.com/path"));
        assertEquals(ScanResultClassifier.Result.SECURE,
                ScanResultClassifier.classify("HTTPS://example.com"));
    }

    @Test
    public void classifiesHttpLinksAsInsecure() {
        assertEquals(ScanResultClassifier.Result.INSECURE,
                ScanResultClassifier.classify("http://example.com"));
    }

    @Test
    public void rejectsNonWebLinksAndMalformedValues() {
        assertEquals(ScanResultClassifier.Result.NOT_WEB_LINK,
                ScanResultClassifier.classify("javascript:alert(1)"));
        assertEquals(ScanResultClassifier.Result.NOT_WEB_LINK,
                ScanResultClassifier.classify("https://"));
        assertEquals(ScanResultClassifier.Result.NOT_WEB_LINK,
                ScanResultClassifier.classify(null));
    }
}
