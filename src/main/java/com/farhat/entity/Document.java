package com.farhat.entity;
/*
 * Created by farhat
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Document {

	@JsonProperty("ri_company")
	private String ri_company;

	@JsonProperty("mne_company")
	private String mne_company;

	@JsonProperty("lib_company")
	private String lib_company;

	@JsonProperty("actif")
	private int actif;

	@JsonProperty("key_book")
	private String key_book;

	@JsonProperty("key_book_pere")
	private String key_book_pere;

	@JsonProperty("niveau")
	private int niveau;

	@JsonProperty("time_stamp")
	private String time_stamp;
	
}
