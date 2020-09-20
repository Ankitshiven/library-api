	package com.ankit.restapi.library.api.publisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId) {
    Publisher publisher = null;
    try {
    	publisher = publisherService.getPublisher(publisherId);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher){
		try {
			 publisherService.addPublisher(publisher);
		} catch (LibraryResourceAlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); 
		}
		return new ResponseEntity<>(publisher, HttpStatus.CREATED);
		
	}
	
	@PutMapping(path="/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId, @RequestBody Publisher publisher) {
    try {
    	publisher.setPublisherId(publisherId);
        publisherService.updatePublisher(publisher);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	

	@DeleteMapping(path="/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId) {
    try {
        publisherService.deletePublisher(publisherId);
    }
    catch(LibraryResourceNotFoundException e) {
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping(path="/search")
	public ResponseEntity<?> searchPublisher(@RequestParam String name) {
      
		if(!LibraryAPIUtils.doesStrValExist(name)) {
			return new ResponseEntity<>("Please Enter a Name to Search Publisher. ", HttpStatus.BAD_REQUEST);
      }
   
    return new ResponseEntity<>(publisherService.searchPublisher(name), HttpStatus.OK);
	}
}













