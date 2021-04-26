package com.dalma.broker.service.notification;

import com.dalma.broker.service.exception.latte.LatteAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class LatteBridge {

    @Value("${dalma.latte.api.url}")
    private String latteUrl;

    @Value("${dalma.latte.api.connect.timeout}")
    private Integer connectTimeout;

    @Value("${dalma.latte.api.read.timeout}")
    private Integer readTimeout;

    public String postToLatte(String payload, LatteApiPath path, String id) {
        try {
            URL url = new URL(new StringBuilder(latteUrl).append(path.getPath(id)).toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.POST.name());

            con.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);

            con.setDoOutput(Boolean.TRUE);
            OutputStream os = con.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            if (HttpStatus.valueOf(responseCode).is2xxSuccessful()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }
        } catch (IOException e) {
            throw new LatteAccessException(e.getMessage());
        }
        return null;
    }
}
