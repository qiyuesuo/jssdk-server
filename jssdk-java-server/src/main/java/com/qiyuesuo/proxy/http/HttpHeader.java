package com.qiyuesuo.proxy.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HttpHeader implements Iterable<HttpHeader.RequestProperty> {

	public static final String ACCEPT = "Accept";
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String USER_AGENT = "User-Agent";
	
	public static HttpHeader DEFAULT = new HttpHeader();

	public HttpHeader() {
		super();
		addProperty(ACCEPT_ENCODING, "gzip,deflate");
		addProperty(ACCEPT, "text/plain, text/html,application/json");
	}

	private List<RequestProperty> headers = new ArrayList<RequestProperty>();

	public List<RequestProperty> getHeaders() {
		return headers;
	}

	public void setHeaders(List<RequestProperty> headers) {
		this.headers = headers;
	}

	public void addProperty(String key, String value) {
		headers.add(new RequestProperty(key, value));
	}

	@Override
	public Iterator<RequestProperty> iterator() {
		return headers.iterator();
	}

	static class RequestProperty {
		private String key;
		private String value;

		public RequestProperty(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
