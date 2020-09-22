	package com.ankit.restapi.library.api.publisher;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.restapi.library.api.exception.LibraryResourceAlreadyExistException;
import com.ankit.restapi.library.api.exception.LibraryResourceNotFoundException;
import com.ankit.restapi.library.api.util.LibraryAPIUtils;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {
	
	private PublisherService publisherService;
	
	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	@GetMapping(path="/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id",defaultValue = "") String traceId) {
		
		if(!LibraryAPIUtils.doesStrValExist(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
    Publisher publisher = null;
    try {
    	publisher = publisherService.getPublisher(publisherId,traceId);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "") String traceId){
		
		if(!LibraryAPIUtils.doesStrValExist(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
		try {
			 publisherService.addPublisher(publisher,traceId);
		} catch (LibraryResourceAlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); 
		}
		return new ResponseEntity<>(publisher, HttpStatus.CREATED);
		
	}
	
	@PutMapping(path="/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId, @RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "") String traceId) {
		if(!LibraryAPIUtils.doesStrValExist(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
    try {
    	publisher.setPublisherId(publisherId);
        publisherService.updatePublisher(publisher,traceId);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	

	@DeleteMapping(path="/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id",defaultValue = "") String traceId) {
		
		if(!LibraryAPIUtils.doesStrValExist(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
    try {
        publisherService.deletePublisher(publisherId,traceId);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping(path="/search")
	public ResponseEntity<?> searchPublisher(@RequestParam String name,
			@RequestHeader(value = "Trace-Id",defaultValue = "") String traceId) {
      
		if(!LibraryAPIUtils.doesStrValExist(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
		
		if(!LibraryAPIUtils.doesStrValExist(name)) {
			return new ResponseEntity<>("Please Enter a Name to Search Publisher. ", HttpStatus.BAD_REQUEST);
      }
   
    return new ResponseEntity<>(publisherService.searchPublisher(name,traceId), HttpStatus.OK);
	}
}













