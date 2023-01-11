package nam.gor.human.resource.department.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nam.gor.human.resource.department.model.Worker;

public interface WorkerDao extends ReactiveMongoRepository<Worker, String> {
}
