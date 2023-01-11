package nam.gor.human.resource.department.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nam.gor.human.resource.department.model.MatchStaff;
import reactor.core.publisher.Flux;


public interface MatchStaffDao extends ReactiveMongoRepository<MatchStaff, String> {

	Flux<MatchStaff> findByOfficeId(String officeId);

	Flux<MatchStaff> findByWorkerId(String workerId);
}
