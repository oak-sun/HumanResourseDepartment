package nam.gor.human.resource.department.dao.children;

import java.lang.invoke.MethodHandles;

import nam.gor.human.resource.department.dao.DaoTests;
import nam.gor.human.resource.department.model.Office;
import nam.gor.human.resource.department.utils.StringPatternConfig;
import nam.gor.human.resource.department.TestStringPatternConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.LastStep;


@DataMongoTest
@TestInstance(Lifecycle.PER_CLASS)
class OfficeDaoTests extends DaoTests {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	OfficeDaoTests() {
		super();
	}

	@Test
	void shouldFindOffices() throws Exception {
		final Flux<Office> flux = officeDao.findAll(
				StringPatternConfig.NAME_SORT);
		final LastStep lastStep = StepVerifier
				.create(flux)
				.assertNext(off -> Assertions.assertEquals(
								TestStringPatternConfig.EXPECTED_OFFICE_1.name(),
								off.name(),
								TestStringPatternConfig.OFF_NAME_ERR_MSG))
				.expectNextCount(1)
				.as("number of offices");
		lastStep.verifyComplete();
		logger.debug("shouldFindOffices():");
	}

	@Test
	void shouldFindOfficeByName() throws Exception {
		final Flux<Office> flux = officeDao.findByName(
				TestStringPatternConfig.EXPECTED_OFFICE_1.name());
		final LastStep lastStep = StepVerifier
				.create(flux)
				.assertNext(off -> Assertions.assertEquals(
						TestStringPatternConfig.EXPECTED_OFFICE_1.name(),
						off.name(),
						TestStringPatternConfig.OFF_NAME_ERR_MSG));
		lastStep.verifyComplete();
		logger.debug("shouldFindOfficeByName():");
	}

	@Test
	void shouldNotFindUnknownOffice() throws Exception {
		final Flux<Office> flux = officeDao.findByName(
				TestStringPatternConfig.UNKNOWN_OFFICE_NAME);
		final LastStep lastStep = StepVerifier.create(flux);
		lastStep.verifyComplete();
		logger.debug("shouldNotFindUnknownOffice():");
	}
}
