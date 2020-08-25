package com.ankit.restapi.library.api.publisher;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository<PublisherEntity, Integer>{

	
}
