package com.dalma.coffee.service.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public abstract class BaseBridge {

	protected String call(String payload, String host, String path, HttpMethod method, int connectTimeout,
			int readTimeout) throws IOException {
		URL url = new URL(new StringBuilder(host).append(path).toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		setVerb(con, method.name());

		con.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		con.setConnectTimeout(connectTimeout);
		con.setReadTimeout(readTimeout);

		if (payload != null) {
			con.setDoOutput(Boolean.TRUE);
			OutputStream os = con.getOutputStream();
			os.write(payload.getBytes());
			os.flush();
			os.close();
		}

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
		return null;
	}

	private void setVerb(HttpURLConnection cn, String verb) throws IOException {
		switch (verb) {
		case "GET":
		case "POST":
		case "HEAD":
		case "OPTIONS":
		case "PUT":
		case "DELETE":
		case "TRACE":
			cn.setRequestMethod(verb);
			break;
		default:
			// set a dummy POST verb
			cn.setRequestMethod("POST");
			try {
				// Change protected field called "method" of public class HttpURLConnection
				setProtectedFieldValue(HttpURLConnection.class, "method", cn, verb);
			} catch (Exception ex) {
				log.error("Error adding verb {}", verb, ex);
				throw new IOException(ex);
			}
			break;
		}
	}

	private static <T> void setProtectedFieldValue(Class<T> clazz, String fieldName, T object, Object newValue)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, newValue);
	}
}
