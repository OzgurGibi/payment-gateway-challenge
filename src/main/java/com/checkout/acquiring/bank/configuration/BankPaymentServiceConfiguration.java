package com.checkout.acquiring.bank.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "acquiring.bank.payment.service")
public class BankPaymentServiceConfiguration
{
    private String url;
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

	private int connectTimeoutMilliseconds;
    public int getConnectTimeoutMilliseconds() { return connectTimeoutMilliseconds; }
    public void setConnectTimeoutMilliseconds(int connectTimeoutMilliseconds) { this.connectTimeoutMilliseconds = connectTimeoutMilliseconds; }

	private int readTimeoutMilliseconds;
    public int getReadTimeoutMilliseconds() { return readTimeoutMilliseconds; }
    public void setReadTimeoutMilliseconds(int readTimeoutMilliseconds) { this.readTimeoutMilliseconds = readTimeoutMilliseconds; }
}