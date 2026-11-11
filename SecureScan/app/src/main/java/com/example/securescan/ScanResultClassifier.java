package com.example.securescan;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

final class ScanResultClassifier {
    enum Result {
        SECURE,
        INSECURE,
        NOT_WEB_LINK
    }

    private ScanResultClassifier() {
    }

    static Result classify(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Result.NOT_WEB_LINK;
        }

        try {
            URI uri = new URI(value.trim());
            String scheme = uri.getScheme();
            if (uri.getHost() == null || scheme == null) {
                return Result.NOT_WEB_LINK;
            }
            if ("https".equals(scheme.toLowerCase(Locale.ROOT))) {
                return Result.SECURE;
            }
            if ("http".equals(scheme.toLowerCase(Locale.ROOT))) {
                return Result.INSECURE;
            }
        } catch (URISyntaxException e) {
            return Result.NOT_WEB_LINK;
        }
        return Result.NOT_WEB_LINK;
    }
}
