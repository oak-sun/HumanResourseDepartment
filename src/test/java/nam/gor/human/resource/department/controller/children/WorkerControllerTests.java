package nam.gor.human.resource.department.controller.children;

import nam.gor.human.resource.department.TestStringPatternConfig;
import nam.gor.human.resource.department.controller.ControllerTests;
import nam.gor.human.resource.department.dao.WorkerDao;
import nam.gor.human.resource.department.model.Worker;
import nam.gor.human.resource.department.utils.StringPatternConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class WorkerControllerTests extends ControllerTests {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	@MockBean
	private WorkerDao workerDao;

	private static final boolean VERBOSE = false;

	WorkerControllerTests() {
		super();
	}

	@Test
	void shouldFindWorkers() throws Exception {
		Mockito
				.when(workerDao.findAll(
						StringPatternConfig.LASTNAME_FIRSTNAME_SORT))
				.thenReturn(
						TestStringPatternConfig.EXPECTED_WORKERS_FLUX);
		var actions = mockMvc.perform(
				MockMvcRequestBuilders.get(
						StringPatternConfig.FIND_WORKERS));
		if (VERBOSE) {
			actions.andDo(print());
		}
		actions.andExpect(status().isOk());
		@SuppressWarnings("unchecked")
		final List<Worker> list = (List<Worker>) actions
				                                       .andReturn()
			                                        	.getAsyncResult();
		checkWorkers(list);
		logger.debug("shouldFindWorkers():");
	}

	@Test
	void shouldFindWorkerByFirstNameAndLastName() throws Exception {
		Mockito
				.when(workerDao.findOne(Example.of(
						TestStringPatternConfig.EXPECTED_WORKER_11)))
				.thenReturn(Mono.just(
						TestStringPatternConfig.EXPECTED_WORKER_11));
		var actions = mockMvc.perform(
				MockMvcRequestBuilders.get(StringPatternConfig.
								FIND_WORKERS_BY_FIRST_NAME_AND_LAST_NAME)
				.param("firstName",
						TestStringPatternConfig
								.EXPECTED_WORKERS
								.get(0)
								.firstName())
				.param("lastName",
						TestStringPatternConfig
								.EXPECTED_WORKERS
								.get(0)
								.lastName()));
		if (VERBOSE) {
			actions.andDo(print());
		}
		actions.andExpect(status().isOk());
		@SuppressWarnings("unchecked")
		final ResponseEntity<Worker> response = (ResponseEntity<Worker>)
				actions
						.andReturn()
						.getAsyncResult();
		Assertions.assertNotNull(response);
		checkWorker(response.getBody());
		logger.debug("shouldFindWorkerByFirstNameAndLastName():");
	}

	private void checkWorkers(List<Worker> list) {
		Assertions.assertNotNull(list);
		Assertions.assertEquals(
				TestStringPatternConfig.EXPECTED_WORKERS.size(),
				list.size());
		for (int i = 0;
			 i < TestStringPatternConfig.EXPECTED_WORKERS.size();
			 i++) {
			Assertions.assertEquals(
					TestStringPatternConfig
							.EXPECTED_WORKERS
							.get(i)
							.firstName(),
					list.get(i).firstName());
			Assertions.assertEquals(
					TestStringPatternConfig
							.EXPECTED_WORKERS
							.get(i)
							.lastName(),
					list.get(i).lastName());
		}
	}

	private void checkWorker(Worker worker) {
		Assertions.assertNotNull(worker);
		Assertions.assertEquals(
				TestStringPatternConfig
						.EXPECTED_WORKERS
						.get(0)
						.firstName(),
				worker.firstName());
		Assertions.assertEquals(
				TestStringPatternConfig
						.EXPECTED_WORKERS
						.get(0)
						.lastName(),
				worker.lastName());
	}
}