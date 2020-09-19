package com.ankit.restapi.library.api.publisher;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.ankit.restapi.library.api.exception.LibraryResourceAlreadyExistException;
import com.ankit.restapi.library.api.exception.LibraryResourceNotFoundException;
import com.ankit.restapi.library.api.util.LibraryAPIUtils;

@Service
public class PublisherService {

	private PublisherRepository publisherRepository;
	
	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}
	
	public void addPublisher(Publisher publisherToBeAdded)  throws LibraryResourceAlreadyExistException{
		
		PublisherEntity publisherEntity = new PublisherEntity(
				publisherToBeAdded.getName(),
				publisherToBeAdded.getEmailId(),
				publisherToBeAdded.getPhoneNumber()
				);
		
		PublisherEntity addedPublisher =null;
		try{
			addedPublisher = publisherRepository.save(publisherEntity);
		}
		catch(DataIntegrityViolationException e) {
			throw new LibraryResourceAlreadyExistException("Publisher Already Exists");
		}
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());
	}

	public Publisher getPublisher(Integer publisherId) throws LibraryResourceNotFoundException {
     Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
     Publisher publisher =null;
     if(publisherEntity.isPresent()) {
    	 PublisherEntity pe = publisherEntity.get();
    	 publisher = createPublisherFromEntity(pe);
     }
     else {
    	 throw new LibraryResourceNotFoundException("PublisherId :"+ publisherId + "Not Found");
     }
     return publisher;
	}

	private Publisher createPublisherFromEntity(PublisherEntity pe) {
		return new Publisher(pe.getPublisherId(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());
	}

	public Publisher updatePublisher(Publisher publisherToBeUpdated) throws LibraryResourceNotFoundException {
		Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
	    // Publisher publisher =null;
	     if(publisherEntity.isPresent()) {
	    	 PublisherEntity pe = publisherEntity.get();
	    	 if(LibraryAPIUtils.doesStrValExist(publisherToBeUpdated.getEmailId())) {
	    		 pe.setEmailId(publisherToBeUpdated.getEmailId());
	    	 }
	    	 if(LibraryAPIUtils.doesStrValExist(publisherToBeUpdated.getPhoneNumber())) {
	    		 pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
	    	 }
	    	 publisherRepository.save(pe);
	    	 publisherToBeUpdated = createPublisherFromEntity(pe);
	     }
	     else {
	    	 throw new LibraryResourceNotFoundException("PublisherId :"+ publisherToBeUpdated.getPublisherId() + "Not Found");
	     }
	     
	     
		return null;
	}

	public void deletePublisher(Integer publisherId) throws LibraryResourceNotFoundException {
       try {
		publisherRepository.deleteById(publisherId);
       }
       catch (EmptyResultDataAccessException e) {
    	   throw new LibraryResourceNotFoundException("PublisherId :"+ publisherId + " Not Found");
       }
	}

}
