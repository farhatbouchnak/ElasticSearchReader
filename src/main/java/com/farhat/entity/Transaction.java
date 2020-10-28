package com.farhat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Transaction {

	private String id;

	private int total_order;

	private int data_collection_order;

}
