package com.omadi.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class BasicAuthRestTemplate extends RestTemplate {
    private String username;
    private String password;

    public BasicAuthRestTemplate(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        addAuthentication();
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        ResponseEntity<T> entity;
        try {
            entity = super.getForEntity(url, responseType, uriVariables);
        } catch (Exception e) {
            sleep(30000);
            return getForEntity(url, responseType, uriVariables);
        }

        return entity;
    }

    private void addAuthentication() {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("Username is mandatory for Basic Auth");
        }

        List<ClientHttpRequestInterceptor> interceptors = Collections
                .singletonList(new BasicAuthInterceptor(username, password));
        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(), interceptors));
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
