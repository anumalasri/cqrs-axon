package com.example.axon.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationCreatedEvent {

	String applicationId;
	
	String status;
	
	String quoteNo;
	
	String quoteVersion;
	
}
