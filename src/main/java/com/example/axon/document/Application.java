package com.example.axon.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="applications")
public class Application {
	@Id
	String applicationId;
	
	String quote;
	String status;
	

}
