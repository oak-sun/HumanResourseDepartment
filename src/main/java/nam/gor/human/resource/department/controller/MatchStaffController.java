package nam.gor.human.resource.department.controller;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import nam.gor.human.resource.department.dao.OfficeDao;
import nam.gor.human.resource.department.dao.WorkerDao;
import nam.gor.human.resource.department.model.Staff;
import nam.gor.human.resource.department.utils.DatasetLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nam.gor.human.resource.department.utils.StringPatternConfig;
import nam.gor.human.resource.department.model.MatchStaff;
import nam.gor.human.resource.department.dao.MatchStaffDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;


@RestController
@AllArgsConstructor
public class MatchStaffController {

	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	private final OfficeDao officeDao;
	private final WorkerDao workerDao;
	private final MatchStaffDao matchStaffDao;


	@GetMapping(StringPatternConfig.LOAD_DATASET_PATH)
	public String loadSampleDataset() {
		DatasetLoader.load(officeDao, workerDao, matchStaffDao);
		logger.info("loadDataset():");
		return StringPatternConfig.LOAD_DATASET_RESULT;
	}


	@GetMapping(StringPatternConfig.FIND_STAFF_BY_OFFICE_NAME)
	public Mono<ResponseEntity<Staff>> findStaffByOfficeName(String officeName) {
		final Set<Staff> set = Collections.synchronizedSet(new HashSet<>());
		final Phaser phaser = new Phaser(1);
		phaser.register();
		findStaffFluxByOfficeName(officeName).subscribe(
				office -> {
					set.add(office);
			logger.debug(
					String.format(
							"findStaffByOfficeName(): " +
									"office name[%s]",
							office.office().name()));
		},
						exc -> {
			logger.error(String.format(
					"findStaffByOfficeName(): " +
							"office name[%s]," +
							" exception[%s]",
					officeName,
					exc.getMessage()));
			phaser.forceTermination();
		},
						() -> {
			logger.debug("findStaffByOfficeName(): completed");
			phaser.arriveAndDeregister();
		});
		phaser.arriveAndAwaitAdvance();

		final Mono<ResponseEntity<Staff>> response = set
				.stream()
				.findFirst()
				.map(ResponseEntity::ok)
				.map(Mono::just)
				.orElse(Mono.error(
						StringPatternConfig.NOT_FOUND_EXCEPTION));
		logger.info(String.format(
				"findStaffByOfficeName(): " +
						"office name[%s]",
				officeName));
		return response;
	}


	@GetMapping(StringPatternConfig.FIND_STAFF_FLUX_BY_OFFICE_NAME)
	public Flux<Staff> findStaffFluxByOfficeName(String officeName) {

		final Function<Tuple2<String, Staff>, Flux<Staff>> function =
				tuple -> matchStaffDao
						.findByOfficeId(tuple.getT1())
						.map(MatchStaff::workerId)
						.flatMap(workerDao::findById)
						.map(tuple.getT2()::newWorker);
		final Flux<Staff> flux = officeDao
				.findByName(officeName)
				.map(off -> Tuples.of(off.id(), Staff.of(off)))
				.flatMap(function);
		logger.info(String.format(
				"findStaffFluxByOfficeName(): " +
						"officeName[%s]",
				officeName));
		return flux;
	}
}