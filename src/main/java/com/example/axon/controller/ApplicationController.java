package com.example.axon.controller;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.axon.commands.CreateApplicationCmd;
import com.example.axon.commands.SaveApplicationCmd;
import com.example.axon.document.Application;
import com.example.axon.repository.ApplicationRepository;

@RestController 
@RequestMapping("/applications")
public class ApplicationController {

	private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired
	ApplicationRepository queryRepo;
	
	@Autowired
	CommandGateway gateway;

	@Autowired
	EntityLinks links;

	
	@GetMapping("/create/{appId}")
	public String createApplication(@PathVariable(value="appId") String appId) {
		
		gateway.send( new CreateApplicationCmd(appId, "draft","Q1", "1,0" ));
	
		logger.info("CreateApplicationCmd created successfully" );
		return appId + " Application Created";
	}
	
	
	@GetMapping("/save/{appId}")
	public String saveApplication(@PathVariable(value="appId") String appId) {
		 
		SaveApplicationCmd cmd =  new SaveApplicationCmd(appId);
		cmd.setQuoteNo("Q2");
		cmd.setQuoteVersion("2.0");
		cmd.setStatus("InProgress");
		gateway.send(cmd);
		
		logger.info("SaveApplicationCmd created successfully" );
		
		return appId+ " Application Saved";
	}
	
	@GetMapping
	public List<Application> getApplications() {
		return queryRepo.findAll();
		
	}
 
/*
	@PostMapping("/commands/create")
	public CompletableFuture<String> createApplication(@RequestBody CreateAppRequest request) {
		isValidRequest(request);
		String appId = new String(UUID.randomUUID().toString());

		CreateApplicationCmd cmd = new CreateApplicationCmd(appId, request);

		FutureCallback<CreateApplicationCmd, Object> callback = new FutureCallback<>();
		gateway.send(cmd, callback);

		return callback.thenApply(
				v -> links.linkForSingleResource(String.class, appId).withSelfRel().getHref())
				.toCompletableFuture();

	}

	@PostMapping("/commands/save")
	public CompletableFuture<String> saveApplication(@RequestBody SaveAppRequest request) {

		String appId = request.getApplicationId() ;
		SaveApplicationCmd cmd = new SaveApplicationCmd(appId, null);

		FutureCallback<SaveApplicationCmd, Object> callback = new FutureCallback<>();
		gateway.send(cmd, callback);

		return callback.thenApply(
				v -> links.linkForSingleResource(String.class, appId).withSelfRel().getHref())
				.toCompletableFuture();

	}

	@PostMapping("/commands/submit")
	public CompletableFuture<String> submitApplication(@RequestBody SaveAppRequest request) {
		String appId = request.getApplicationId() ;
		SubmitApplicationCmd cmd = new SubmitApplicationCmd(appId);

		FutureCallback<SubmitApplicationCmd, Object> callback = new FutureCallback<>();
		gateway.send(cmd, callback);

		return callback.thenApply(
				v -> links.linkForSingleResource(String.class, appId).withSelfRel().getHref())
				.toCompletableFuture();
	}

	private boolean isValidRequest(CreateAppRequest request) {
		return true;
	}*/
}
