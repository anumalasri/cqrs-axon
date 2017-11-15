package com.example.axon.events;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.example.axon.document.Application;
import com.example.axon.repository.ApplicationRepository;

@Component
public class ApplicationEventListener {

	private Logger logger = LoggerFactory.getLogger(ApplicationEventListener.class);
	
	@Autowired
	ApplicationRepository queryRepo;
	
	@EventHandler
	public void createApplication(ApplicationCreatedEvent event) {
		logger.info("EventHander for Create Event");

		Application application = new Application();
		application.setApplicationId(event.getApplicationId());
		application.setStatus(event.getStatus());
		application.setQuote( event.getQuoteNo() + "-" + event.getQuoteVersion());
		
		queryRepo.insert(application);
		
		logger.info("New Application {} created in Mongo", event.getApplicationId());
		
	}
	
	@EventHandler
	public void saveeApplication(ApplicationSavedEvent event) {
		logger.info("EventHander for Save Event");

		Application exampleApp = new Application();
		exampleApp.setApplicationId(event.getApplicationId());
		
		Example<Application> example = Example.of(exampleApp);
		
		Application application = queryRepo.findOne(example);
				
		application.setStatus(event.getStatus());
		application.setQuote( event.getQuoteNo() + "-" + event.getQuoteVersion());
		
		queryRepo.save(application);
		
		logger.info(" Application {} saved in Mongo", event.getApplicationId());
		
	}
}
