package nam.gor.human.resource.department.dao;

import nam.gor.human.resource.department.model.Office;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface OfficeDao extends ReactiveMongoRepository<Office, String> {
	Flux<Office> findByName(String name);
}