package com.abc.dto;

import org.springframework.lang.Nullable;

public class ClientResponse {
//	@NotBlank(message = "REQUIRED: responseKey") protected String responseKey;
	@Nullable
	private Object data;

	public ClientResponse() {}

	public ClientResponse(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ClientResponse [data=" + data + "]";
	}
}
