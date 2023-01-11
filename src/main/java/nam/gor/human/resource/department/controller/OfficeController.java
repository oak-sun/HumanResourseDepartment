package nam.gor.human.resource.department.controller;

import java.lang.invoke.MethodHandles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nam.gor.human.resource.department.dao.OfficeDao;
import nam.gor.human.resource.department.model.Office;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nam.gor.human.resource.department.utils.StringPatternConfig;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
public class OfficeController {

	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());
    @NonNull
	private final OfficeDao dao;


	@GetMapping(StringPatternConfig.FIND_OFFICES)
	public Flux<Office> findOffices() {
		final Flux<Office> flux = dao.findAll(
				StringPatternConfig.NAME_SORT);
		logger.info("findOffices():");
		return flux;
	}

	@GetMapping(StringPatternConfig.FIND_OFFICE_BY_NAME)
	public Flux<Office> findOfficeByName(String name) {
		final Flux<Office> flux = dao.findByName(name);
		logger.info(String.format(
				"findOfficeByName(): name[%s]", name));
		return flux;
	}
}
