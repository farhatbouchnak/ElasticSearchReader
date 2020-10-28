package com.farhat.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Source implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("version")
	private String version;

	@JsonProperty("connector")
    private String connector;

	@JsonProperty("name")
    private String name;

	@JsonProperty("ts_ms")
    private Date ts_ms;
    
	@JsonProperty("snapshot")
    private boolean snapshot;

	@JsonProperty("db")
    private String db;

	@JsonProperty("schema")
    private String schema;

	@JsonProperty("table")
    private String table;
    
	@JsonProperty("change_lsn")
    private String change_lsn;

	@JsonProperty("commit_lsn")
    private String commit_lsn;

	@JsonProperty("event_serial_no")
    private int event_serial_no;

}
