package com.github.maxcriser.qrscanner.server;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

class RequestFormer {

    Request create(final Map<String, String> dictionary, final String url) {
        final FormBody.Builder builder = new FormBody.Builder();
        for (final Map.Entry<String, String> entry :
                dictionary.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return new Request.Builder()
                .url(url)
                .post(builder.build()).build();
    }
}
