package nam.gor.human.resource.department.controller.children;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.lang.invoke.MethodHandles;
import nam.gor.human.resource.department.controller.ControllerTests;
import nam.gor.human.resource.department.model.Staff;
import nam.gor.human.resource.department.utils.StringPatternConfig;
import nam.gor.human.resource.department.TestStringPatternConfig;
import nam.gor.human.resource.department.dao.MatchStaffDao;
import nam.gor.human.resource.department.dao.OfficeDao;
import nam.gor.human.resource.department.dao.WorkerDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootTest
@AutoConfigureMockMvc
class MatchStaffControllerTests extends ControllerTests {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	@MockBean
	private OfficeDao officeDao;

	@MockBean
	private WorkerDao workerDao;

	@MockBean
	private MatchStaffDao matchStaffDao;

	private static final boolean VERBOSE = false;


	MatchStaffControllerTests() {
		super();
	}


	@Test
	void shouldFindAggregateByDepartmentName() throws Exception {
		Mockito
				.when(matchStaffDao.findByOfficeId(
						TestStringPatternConfig.STF_OFF_ID))
				.thenReturn(
						TestStringPatternConfig.EXPECTED_MATCH_STAFF_FLUX);
		Mockito
				.when(officeDao.findByName(
						TestStringPatternConfig.EXPECTED_OFFICE_1.name()))
				.thenReturn(Flux.just(
						TestStringPatternConfig.EXPECTED_OFFICE_WITH_ID));
		Mockito
				.when(workerDao.findById(
						TestStringPatternConfig.STF_WRK_ID))
				.thenReturn(Mono.just(
						TestStringPatternConfig.EXPECTED_WORKER_WITH_ID));
		var actions = mockMvc.perform(
				MockMvcRequestBuilders.get(
						        StringPatternConfig.FIND_STAFF_BY_OFFICE_NAME).param(
										"officeName",
								              TestStringPatternConfig.EXPECTED_OFFICE_1.name()));
		// THEN
		if (VERBOSE) {
			actions.andDo(print());
		}
		actions.andExpect(status().isOk());
		@SuppressWarnings("unchecked")
		final ResponseEntity<Staff> response = (ResponseEntity<Staff>) actions
						.andReturn()
						.getAsyncResult();
		Assertions.assertNotNull(response);
		final var staff = response.getBody();
		Assertions.assertNotNull(staff);
		Assertions.assertEquals(
				TestStringPatternConfig.EXPECTED_OFFICE_1.name(),
				staff.office().name());
		Assertions.assertEquals(1, staff.workers().size());
		Assertions.assertTrue(
				staff
				.workers()
				.contains(TestStringPatternConfig.EXPECTED_WORKER_WITH_ID));
		logger.debug("shouldFindStaffByOfficeName():");
	}
}