package com.farhat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PayloadDocument  implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("before")
	private Map<String, Object> before;
	
	@JsonProperty("after")
	private Map<String, Object> after;
	
	@JsonProperty("source")
	private Source source;

	@JsonProperty("op")
	private String op;

	@JsonProperty("ts_ms")
	private Date ts_ms;
	
	@JsonProperty("transaction")
	private List<Transaction> transaction;

	
	
//	public static <T> T parseObjectFromString(String s, Class<T> clazz) throws Exception {
//	    return clazz.getConstructor(new Class[] {String.class }).newInstance(s);
//	}
}
