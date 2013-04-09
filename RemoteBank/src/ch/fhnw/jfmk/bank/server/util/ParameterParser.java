package ch.fhnw.jfmk.bank.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class ParameterParser extends Filter {

	@Override
	public String description() {
		return "Parses the requested URI for parameters";
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
		parseGetParameters(exchange);
		parsePostParameters(exchange);
		chain.doFilter(exchange);
	}

	private void parseGetParameters(HttpExchange exchange) throws UnsupportedEncodingException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		URI requestedUri = exchange.getRequestURI();
		String query = requestedUri.getRawQuery();
		parseQuery(query, parameters);
		exchange.setAttribute("parameters", parameters);
	}

	private void parsePostParameters(HttpExchange exchange) throws IOException {
		if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			@SuppressWarnings("unchecked")
			Map<String, Object> parameters = (Map<String, Object>) exchange.getAttribute("parameters");
			InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			parseQuery(query, parameters);
		}
	}

	@SuppressWarnings("unchecked")
	public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
		if (query != null) {
			StringTokenizer st = new StringTokenizer(query, "&");
			while (st.hasMoreTokens()) {
				String keyValue = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(keyValue, "=");
				String key = null;
				String value = "";
				if (st2.hasMoreTokens()) {
					key = st2.nextToken();
					key = URLDecoder.decode(key, System.getProperty("file.encoding"));
				}

				if (st2.hasMoreTokens()) {
					value = st2.nextToken();
					value = URLDecoder.decode(value, System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object o = parameters.get(key);
					if (o instanceof List) {
						List<String> values = (List<String>) o;
						values.add(value);
					} else if (o instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) o);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
}