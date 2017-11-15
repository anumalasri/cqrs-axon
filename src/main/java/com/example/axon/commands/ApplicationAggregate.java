package com.example.axon.commands;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.axon.events.ApplicationCreatedEvent;
import com.example.axon.events.ApplicationSavedEvent;

@Aggregate
public class ApplicationAggregate {
	
	private Logger logger = LoggerFactory.getLogger(ApplicationAggregate.class);

	
	@AggregateIdentifier
	private String applicationId;	 
	private String status;	

	
	@CommandHandler
	public ApplicationAggregate(CreateApplicationCmd cmd) throws Exception {
		Assert.isFalse(cmd.getQuoteNo().isEmpty(), () -> "Quote must be present.");
		
		this.applicationId = cmd.getApplicationId();
		apply(new ApplicationCreatedEvent(cmd.getApplicationId(), cmd.getStatus(), cmd.getQuoteNo(), cmd.getQuoteVersion()));
	}
	
	@SuppressWarnings("unused")
	private ApplicationAggregate() {}
	
	@CommandHandler
	public void handle(SaveApplicationCmd cmd) {
		ApplicationSavedEvent event = new ApplicationSavedEvent();
		event.setApplicationId(cmd.getApplicationId());
		event.setQuoteNo(cmd.getQuoteNo());
		event.setQuoteVersion(cmd.getQuoteVersion());
		event.setStatus(cmd.getStatus()); 
		apply(event);
	}


	
	@EventSourcingHandler	
	protected void on(ApplicationCreatedEvent event) {
		logger.info("EventSourcingHander for Create Event");
		this.setApplicationId(event.getApplicationId());
		this.setStatus("draft");
		logger.info("Updated Aggregate : {}, {} ", this.applicationId, this.status);
	}
	
	@EventSourcingHandler	
	protected void on(ApplicationSavedEvent event) {
		logger.info("EventSourcingHander for Save Event ");
		this.setApplicationId(event.getApplicationId());
		this.setStatus(event.getStatus());
		logger.info("Updated Aggregate : {}, {} ", this.applicationId, this.status);
		 		
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
}

