package com.example.axon.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SaveApplicationCmd {

	@NonNull
	@TargetAggregateIdentifier
	String applicationId;
	
	String status;
	
	String quoteNo;
	
	String quoteVersion;
	 
}
