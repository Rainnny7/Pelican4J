/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.pelican.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.braydon.pelican.client.ClientConfig;
import me.braydon.pelican.exception.PanelAPIException;
import me.braydon.pelican.model.PanelModel;
import okhttp3.*;

/**
 * A json web request.
 *
 * @author Braydon
 */
@Builder
public class JsonWebRequest {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final MediaType JSON_MEDIA = MediaType.get("application/json");
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    /**
     * The method of this request.
     */
    @Builder.Default @NonNull private final HttpMethod method = HttpMethod.GET;

    /**
     * The endpoint to make the request to.
     */
    @NonNull private final String endpoint;

    /**
     * The body to send in this request, null if none.
     */
    private final String body;

    /**
     * Execute this request.
     *
     * @param clientConfig the client config to use
     * @param responseType the expected response type, null for none
     * @return the response, null if none
     * @param <T> the response type
     */
    @SneakyThrows
    protected <T extends PanelModel<T>> T execute(@NonNull ClientConfig clientConfig, Class<T> responseType) {
        Request request = new Request.Builder()
                .method(method.name(), body == null ? null : RequestBody.create(body, JSON_MEDIA))
                .url(clientConfig.panelUrl() + "/api" + endpoint)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "Application/vnd.pterodactyl.v1+json")
                .addHeader("Authorization", "Bearer " + clientConfig.apiKey())
                .build(); // Build the request to send

        // Execute the request and receive the response
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            int status = response.code(); // The HTTP response code
            String json = response.body().string(); // The json response

            // If the status is not 200 (OK), handle the error
            if (status != 200) {
                JsonObject errorJsonObject = GSON.fromJson(json, JsonObject.class);
                errorJsonObject = errorJsonObject.get("errors").getAsJsonArray().get(0).getAsJsonObject();
                throw new PanelAPIException(status, errorJsonObject.get("code").getAsString(), errorJsonObject.get("detail").getAsString());
            }

            // If there is a response expected, parse
            // the received json and return it
            return responseType == null ? null : GSON.fromJson(json, responseType).getValue();
        }
    }
}