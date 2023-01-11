package nam.gor.human.resource.department.dao.children;

import nam.gor.human.resource.department.TestStringPatternConfig;
import nam.gor.human.resource.department.dao.DaoTests;
import nam.gor.human.resource.department.model.MatchStaff;
import nam.gor.human.resource.department.model.Office;
import nam.gor.human.resource.department.model.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure
		.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.LastStep;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.List;
import java.util.Objects;


@DataMongoTest
@TestInstance(Lifecycle.PER_CLASS)
class MatchStaffDaoTests extends DaoTests {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	private List<String> officeIds;
	private List<String> workerIds;

	MatchStaffDaoTests() {
		super();
	}


	@BeforeAll
	void setUp() {
		officeIds = officeDao
				.findAll()
				.map(Office::id)
				.collectList()
				.block(Duration.ofSeconds(1));
		Assertions.assertFalse(
				Objects.requireNonNull(officeIds).isEmpty(),
				TestStringPatternConfig.OFF_ID_LIST_ERR_MSG);
		workerIds = workerDao
				.findAll()
				.map(Worker::id)
				.collectList()
				.block(Duration.ofSeconds(1));
		Assertions.assertFalse(
				Objects.requireNonNull(workerIds).isEmpty(),
				TestStringPatternConfig.WRK_ID_LIST_ERR_MSG);
	}

	@Test
	void shouldFindWorkerIdByOfficeId() throws Exception {
		final Flux<MatchStaff> flux =
				matchStaffDao.findByOfficeId(officeIds.get(0));
		final LastStep lastStep = StepVerifier
				.create(flux)
				.assertNext(m -> Assertions.assertTrue(
								workerIds.contains(m.workerId()),
								TestStringPatternConfig.BAD_WRK_ID_ERR_MSG))
				.expectNextCount(1)
				.as(TestStringPatternConfig.BAD_OFF_MTCH_NUMBER_ERR_MSG);
		lastStep.verifyComplete();
		logger.debug("shouldFindWorkerIdByOfficeId():");
	}

	@Test
	void shouldFindOfficeIdByWorkerId() throws Exception {
		final Flux<MatchStaff> flux = matchStaffDao
				.findByWorkerId(workerIds.get(0));
		final LastStep lastStep = StepVerifier
				.create(flux)
				.assertNext(m -> Assertions.assertTrue(
						officeIds.contains(m.officeId()),
						TestStringPatternConfig.BAD_OFF_ID_ERR_MSG))
				.expectNextCount(0)
				.as(TestStringPatternConfig.BAD_WRK_MTCH_NUMBER_ERR_MSG);
		lastStep.verifyComplete();
		logger.debug("shouldFindOfficeIdByWorkerId():");
	}
}
