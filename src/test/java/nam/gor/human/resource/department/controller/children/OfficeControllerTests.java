package nam.gor.human.resource.department.controller.children;

import nam.gor.human.resource.department.TestStringPatternConfig;
import nam.gor.human.resource.department.controller.ControllerTests;
import nam.gor.human.resource.department.dao.OfficeDao;
import nam.gor.human.resource.department.model.Office;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static nam.gor.human.resource.department.utils.StringPatternConfig.FIND_OFFICES;
import static nam.gor.human.resource.department.utils.StringPatternConfig.FIND_OFFICE_BY_NAME;
import static nam.gor.human.resource.department.utils.StringPatternConfig.NAME_SORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class OfficeControllerTests extends ControllerTests {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	@MockBean
	private OfficeDao officeDao;

	private static final boolean VERBOSE = false;

	OfficeControllerTests() {
		super();
	}

	@Test
	void shouldFindOffices() throws Exception {
		Mockito
				.when(officeDao.findAll(NAME_SORT))
				.thenReturn(
						TestStringPatternConfig.EXPECTED_OFFICES_FLUX);
		var actions = mockMvc.perform(get(FIND_OFFICES));
		if (VERBOSE) {
			actions.andDo(print());
		}
		actions.andExpect(status().isOk());
		final var result = actions.andReturn();
		@SuppressWarnings("unchecked")
		final List<Office> list = (List<Office>) result.getAsyncResult();
		checkOffices(list);
		logger.debug("shouldFindOffices():");
	}

	@Test
	void shouldFindOfficeByName() throws Exception {
		Mockito
				.when(officeDao.findByName(
						TestStringPatternConfig.EXPECTED_OFFICE_1.name()))
				.thenReturn(Flux.just(
						TestStringPatternConfig.EXPECTED_OFFICE_1));
		var actions = mockMvc
				.perform(get(FIND_OFFICE_BY_NAME)
						.param("name", TestStringPatternConfig
								.EXPECTED_OFFICES
								.get(0)
								.name()));
		if (VERBOSE) {
			actions.andDo(print());
		}
		actions.andExpect(status().isOk());
		final var result = actions.andReturn();
		@SuppressWarnings("unchecked")
		final List<Office> list = (List<Office>) result.getAsyncResult();
		Assertions.assertNotNull(list);
		Assertions.assertEquals(1, list.size());
		checkOffice(list.get(0));
		logger.debug("shouldFindOfficeByName():");
	}

	private void checkOffices(List<Office> list) {
		Assertions.assertNotNull(list);
		Assertions.assertEquals(
				TestStringPatternConfig.EXPECTED_OFFICES.size(),
				list.size());
		for (int i = 0;
			 i < TestStringPatternConfig.EXPECTED_OFFICES.size();
			 i++) {
			Assertions.assertEquals(
					TestStringPatternConfig
							.EXPECTED_OFFICES.get(i).name(),
					list.get(i).name());
		}
	}

	private void checkOffice(Office office) {
		Assertions.assertNotNull(office);
		Assertions.assertEquals(
				TestStringPatternConfig
						.EXPECTED_OFFICES
						.get(0)
						.name(),
				office.name());
	}
}