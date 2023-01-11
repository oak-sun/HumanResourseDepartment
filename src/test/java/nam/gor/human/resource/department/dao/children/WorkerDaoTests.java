package nam.gor.human.resource.department.dao.children;

import nam.gor.human.resource.department.TestStringPatternConfig;
import nam.gor.human.resource.department.dao.DaoTests;
import nam.gor.human.resource.department.model.Worker;
import nam.gor.human.resource.department.utils.StringPatternConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.LastStep;
import java.lang.invoke.MethodHandles;

@DataMongoTest
@TestInstance(Lifecycle.PER_CLASS)
class WorkerDaoTests extends DaoTests {

	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	WorkerDaoTests() {
		super();
	}

	@Test
	void shouldFindWorkers() throws Exception {
		final Flux<Worker> flux = workerDao.findAll(
				StringPatternConfig.LASTNAME_FIRSTNAME_SORT);
		final LastStep lastStep = StepVerifier
				.create(flux)
				.assertNext(w -> {
					Assertions.assertEquals(
							TestStringPatternConfig.EXPECTED_WORKER_11.firstName(),
							w.firstName(),
							TestStringPatternConfig.WRK_FNAME_ERR_MSG);
					Assertions.assertEquals(
							TestStringPatternConfig.EXPECTED_WORKER_11.lastName(),
							w.lastName(),
							TestStringPatternConfig.WRK_LNAME_ERR_MSG);
				})
				.expectNextCount(3)
				.as("number of workers");
		lastStep.verifyComplete();
		logger.debug("shouldFindWorkers():");
	}

	@Test
	void shouldFindWorkerByFirstNameAndLastName() throws Exception {
		final Mono<Worker> mono = workerDao.findOne(Example.of(
				TestStringPatternConfig.EXPECTED_WORKER_11));
		final LastStep lastStep = StepVerifier
				.create(mono)
				.assertNext(w -> {
			Assertions.assertEquals(
					TestStringPatternConfig.EXPECTED_WORKER_11.firstName(),
					w.firstName(),
					TestStringPatternConfig.WRK_FNAME_ERR_MSG);
			Assertions.assertEquals(
					TestStringPatternConfig.EXPECTED_WORKER_11.lastName(),
					w.lastName(),
					TestStringPatternConfig.WRK_LNAME_ERR_MSG);
		});
		lastStep.verifyComplete();
		logger.debug("shouldFindWorkerByFirstNameAndLastName():");
	}

	@Test
	void shouldNotFindUnknownWorker() throws Exception {
		final Mono<Worker> mono = workerDao.findOne(
				TestStringPatternConfig.UNKNOWN_WORKER_EXAMPLE);
		final LastStep lastStep = StepVerifier.create(mono);
		lastStep.verifyComplete();
		logger.debug("shouldNotFindUnknownWorker():");
	}
}
