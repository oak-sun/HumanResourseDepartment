package nam.gor.human.resource.department.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nam.gor.human.resource.department.dao.WorkerDao;
import nam.gor.human.resource.department.model.Worker;
import nam.gor.human.resource.department.utils.StringPatternConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.lang.invoke.MethodHandles;


@RestController
@RequiredArgsConstructor
public class WorkerController {

	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());
    @NonNull
	private final WorkerDao dao;


	@GetMapping(StringPatternConfig.FIND_WORKERS)
	public Flux<Worker> findWorkers() {
		final Flux<Worker> flux = dao.findAll(
				StringPatternConfig.LASTNAME_FIRSTNAME_SORT);
		logger.info("findWorkers():");
		return flux;
	}

	@GetMapping(StringPatternConfig.FIND_WORKERS_BY_FIRST_NAME_AND_LAST_NAME)
	public Mono<ResponseEntity<Worker>> findWorkerByFirstNameAndLastName(String firstName,
																		 String lastName) {
		final Example<Worker> example =
				Example.of(Worker.fromName(firstName, lastName));
		final Mono<ResponseEntity<Worker>> response = dao
				.findOne(example)
				.map(ResponseEntity::ok)
				.switchIfEmpty(Mono.error(
						StringPatternConfig.NOT_FOUND_EXCEPTION));
		logger.info(String.format(
				"findWorkerByFirstNameAndLastName(): " +
						"firstName[%s], " +
						"lastName[%s]",
				firstName,
				lastName));
		return response;
	}
}
