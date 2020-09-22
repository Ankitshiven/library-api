package com.ankit.restapi.library.api.publisher;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	public void addPublisher(Publisher publisherToBeAdded, String traceId)  throws LibraryResourceAlreadyExistException{
		
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
			throw new LibraryResourceAlreadyExistException("traceId :"+ traceId +", Publisher Already Exists");
		}
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherId());
	}

	public Publisher getPublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {
     Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
     Publisher publisher =null;
     if(publisherEntity.isPresent()) {
    	 PublisherEntity pe = publisherEntity.get();
    	 publisher = createPublisherFromEntity(pe);
     }
     else {
    	 throw new LibraryResourceNotFoundException("traceId :"+ traceId +", PublisherId :"+ publisherId + "Not Found");
     }
     return publisher;
	}

	private Publisher createPublisherFromEntity(PublisherEntity pe) {
		return new Publisher(pe.getPublisherId(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());
	}

	public Publisher updatePublisher(Publisher publisherToBeUpdated, String traceId)
			throws LibraryResourceNotFoundException {
		Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
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
	    	 throw new LibraryResourceNotFoundException("traceId :"+ traceId +", PublisherId :"+ publisherToBeUpdated.getPublisherId() + "Not Found");
	     }
	     
	     
		return null;
	}

	public void deletePublisher(Integer publisherId, String traceId) 
			throws LibraryResourceNotFoundException {
       try {
		publisherRepository.deleteById(publisherId);
       }
       catch (EmptyResultDataAccessException e) {
    	   throw new LibraryResourceNotFoundException("traceId :"+ traceId +", PublisherId :"+ publisherId + " Not Found");
       }
	}

	public List<Publisher> searchPublisher(String name, String traceId) {
		List<PublisherEntity> publisherEntities =null;
		if(LibraryAPIUtils.doesStrValExist(name)) {
			publisherEntities = publisherRepository.findByNameContaining(name);
		}
		if(publisherEntities != null && publisherEntities.size() > 0) {
			return createPublishersSearchResponse(publisherEntities);
		}
		else {
		return Collections.EMPTY_LIST;
		}
	}

	private List<Publisher> createPublishersSearchResponse(List<PublisherEntity> publisherEntities) {

		return publisherEntities.stream()
				.map(pe -> createPublisherFromEntity(pe))
				.collect(Collectors.toList());
	}

}
