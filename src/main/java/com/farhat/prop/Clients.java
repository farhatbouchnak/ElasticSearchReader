package com.farhat.prop;

import lombok.Getter;
import lombok.Setter;

/*
 * Created by farhat
 */
@Getter
@Setter
public class Clients {
    private String hostname;
    private String scheme;
    private int httpPort;
    private int containerPort;
	
}
