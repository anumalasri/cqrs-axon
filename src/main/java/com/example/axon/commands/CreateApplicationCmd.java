package com.example.axon.commands;

import static lombok.AccessLevel.PRIVATE;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CreateApplicationCmd {
	
	@TargetAggregateIdentifier
String applicationId;
	
	String status;
	
	String quoteNo;
	
	String quoteVersion;
	 
	
}
