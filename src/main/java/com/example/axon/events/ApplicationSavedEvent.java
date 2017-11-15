package com.example.axon.events;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationSavedEvent {

	String applicationId;
	
	String status;
	
	String quoteNo;
	
	String quoteVersion;
}
