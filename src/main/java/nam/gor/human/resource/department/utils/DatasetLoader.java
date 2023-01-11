package nam.gor.human.resource.department.utils;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Phaser;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import nam.gor.human.resource.department.dao.MatchStaffDao;
import nam.gor.human.resource.department.dao.OfficeDao;
import nam.gor.human.resource.department.dao.WorkerDao;
import nam.gor.human.resource.department.model.Office;
import nam.gor.human.resource.department.model.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nam.gor.human.resource.department.model.MatchStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class DatasetLoader {
	private static final Log logger = LogFactory.getLog(
			MethodHandles
					.lookup()
					.lookupClass()
					.getName());

	private static final boolean VERBOSE = false;

	private DatasetLoader() {
		super();
	}

	public static void load(OfficeDao officeDao,
							WorkerDao workerDao,
							MatchStaffDao matchStaffDao) {
		final Flux<Flux<MatchStaff>> flux = prepare(officeDao,
				                                    workerDao,
			                                    	matchStaffDao);
		execute(flux);
	}

	public static void delete(OfficeDao officeDao,
										   WorkerDao workerDao,
										   MatchStaffDao matchStaffDao) {

		final Phaser phaser = new Phaser(1);
		final Consumer<Throwable> consumer = exc -> {
			logger.error(String.format(
					"deleteDataset(): exception[%s]",
					exc.getMessage()));
			phaser.forceTermination();
		};
		phaser.register();
		matchStaffDao
				.deleteAll()
				.subscribe(null,
						           consumer,
						           phaser::arriveAndDeregister);
		phaser.arriveAndAwaitAdvance();
		phaser.register();
		workerDao
				.deleteAll()
				.subscribe(null,
					            	consumer,
					             	phaser::arriveAndDeregister);
		phaser.arriveAndAwaitAdvance();
		phaser.register();
		officeDao
				.deleteAll()
				.subscribe(null,
						           consumer,
						           phaser::arriveAndDeregister);
		phaser.arriveAndAwaitAdvance();
	}

	private static Flux<Flux<MatchStaff>> prepare(OfficeDao officeDao,
												  WorkerDao workerDao,
												  MatchStaffDao matchStaffDao) {
		final Mono<Void> deletedMono = officeDao
				                             .deleteAll()
				                             .then(workerDao.deleteAll());
		final BiFunction<Office, Worker, Mono<MatchStaff>> savedMatch =
				(office, worker) -> matchStaffDao
								      .save(MatchStaff.of(office, worker))
								      .transform(
											  mono -> VERBOSE ?
											  mono.log() : mono);

		final BiFunction<Office, Worker, Mono<MatchStaff>> savedWorker =
				(office, worker) -> workerDao
						             .save(worker)
						             .flatMap(wrkSaved ->
											 savedMatch.apply(office, wrkSaved));

		final BiFunction<Integer, Office, Flux<MatchStaff>> createdWorker =
				(offIndex, office) -> Flux
						.just(StringPatternConfig.WRK_INDEX_LOWER_BOUND,
								StringPatternConfig.WRK_INDEX_UPPER_BOUND)
						.map(wrkIndex -> StringPatternConfig
								        .WRK_INDEX_FUN
								        .applyAsInt(offIndex, wrkIndex))
						.map(Worker::fromIndex)
						.flatMap(worker -> savedWorker.apply(office, worker));

		final BiFunction<Integer, Office, Mono<Flux<MatchStaff>>> savedOffice =
				(offIndex, office) -> officeDao
					             	.save(office)
						            .map(offSaved -> createdWorker
														.apply(offIndex, offSaved));

		final Function<Integer, Mono<Flux<MatchStaff>>> createdOffice =
				offIndex -> Mono
						      .just(Office.fromIndex(offIndex))
				              .flatMap(office -> savedOffice.apply(offIndex, office));

		final Flux<Flux<MatchStaff>> addFlux = Flux
				.just(
			        	StringPatternConfig.OFF_INDEX_LOWER_BOUND,
						StringPatternConfig.OFF_INDEX_UPPER_BOUND)
				.flatMap(createdOffice);
		return deletedMono.thenMany(addFlux);
	}

	private static void execute(Flux<Flux<MatchStaff>> loaderFlux) {
		final Phaser phaser = new Phaser(1);
		phaser.register();
		final Consumer<Flux<MatchStaff>> consumer =
				matchStaffFlux -> matchStaffFlux.subscribe(
						m -> logger.debug(String.format(
								"execute(): saved match staff," +
										" officeId[%s]," +
										" workerId[%s]",
								m.officeId(),
								m.workerId())),
				exc -> {
					logger.error(String.format(
							"execute(): saving match staff exception[%s]",
							exc.getMessage()));
					phaser.forceTermination();
				},
				() -> logger.info("execute(): saving match staff completed"));

		loaderFlux.subscribe(consumer,
				exc -> {
					logger.error(String.format(
							"execute(): saving all exception[%s]",
							exc.getMessage()));
					phaser.forceTermination();
				},
				() -> {
					logger.info("execute(): saving all completed");
					phaser.arriveAndDeregister();
				});
		phaser.arriveAndAwaitAdvance();
	}
}