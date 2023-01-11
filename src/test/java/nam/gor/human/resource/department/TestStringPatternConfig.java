package nam.gor.human.resource.department;

import java.util.List;
import nam.gor.human.resource.department.model.Office;
import nam.gor.human.resource.department.model.Worker;
import org.springframework.data.domain.Example;
import nam.gor.human.resource.department.model.MatchStaff;
import reactor.core.publisher.Flux;
import static nam.gor.human.resource.department.utils.StringPatternConfig.*;


@SuppressWarnings("doclint:missing")
public final class TestStringPatternConfig {
	public static final Office EXPECTED_OFFICE_1 =
			Office.fromIndex(OFF_INDEX_LOWER_BOUND);
	public static final Office EXPECTED_OFFICE_2 =
			Office.fromIndex(OFF_INDEX_LOWER_BOUND + 1);
	public static final List<Office> EXPECTED_OFFICES =
			List.of(
					EXPECTED_OFFICE_1,
					EXPECTED_OFFICE_2);
	public static final Flux<Office> EXPECTED_OFFICES_FLUX =
			Flux.just(
					EXPECTED_OFFICE_1,
		        	EXPECTED_OFFICE_2);
	public static final Worker EXPECTED_WORKER_11 =
			Worker.fromIndex(WRK_INDEX_FUN
					.applyAsInt(
							OFF_INDEX_LOWER_BOUND,
							WRK_INDEX_LOWER_BOUND));
	public static final Worker EXPECTED_WORKER_12 =
			Worker.fromIndex(WRK_INDEX_FUN
					.applyAsInt(
							   OFF_INDEX_LOWER_BOUND,
							WRK_INDEX_LOWER_BOUND + 1));
	public static final Worker EXPECTED_WORKER_21 =
			Worker.fromIndex(WRK_INDEX_FUN
					.applyAsInt(
							OFF_INDEX_LOWER_BOUND + 1,
							  WRK_INDEX_LOWER_BOUND));
	public static final Worker EXPECTED_WORKER_22 =
			Worker.fromIndex(WRK_INDEX_FUN
					.applyAsInt(
							OFF_INDEX_LOWER_BOUND + 1,
							WRK_INDEX_LOWER_BOUND + 1));
	public static final List<Worker> EXPECTED_WORKERS =
			List.of(
					EXPECTED_WORKER_11,
					EXPECTED_WORKER_12,
					EXPECTED_WORKER_21,
					EXPECTED_WORKER_22);
	public static final Flux<Worker> EXPECTED_WORKERS_FLUX =
			Flux.just(
					EXPECTED_WORKER_11,
					EXPECTED_WORKER_12,
					EXPECTED_WORKER_21,
					EXPECTED_WORKER_22);

	public static
	final String UNKNOWN_OFFICE_NAME = "unknown";
	public static final Example<Worker> UNKNOWN_WORKER_EXAMPLE = Example.of(
			new Worker(
					"",
					"unknown",
					"unknown"));

	public static final String OFF_NAME_ERR_MSG =
			"Bad office name";
	public static final String WRK_FNAME_ERR_MSG =
			"Bad worker first name";
	public static final String WRK_LNAME_ERR_MSG =
			"Bad worker last name";
	public static final String OFF_ID_LIST_ERR_MSG =
			"office id list should be not empty";
	public static final String WRK_ID_LIST_ERR_MSG =
			"worker id list should be not empty";
	public static final String BAD_OFF_ID_ERR_MSG =
			"match should have correct office id";
	public static final String BAD_WRK_ID_ERR_MSG =
			"match should have correct worker id";
	public static final String BAD_OFF_MTCH_NUMBER_ERR_MSG =
			"number of matches for office";
	public static final String BAD_WRK_MTCH_NUMBER_ERR_MSG =
			"number of matches for worker";
	public static final String STF_OFF_ID = "123";
	public static final String STF_WRK_ID = "456";
	public static final Flux<MatchStaff> EXPECTED_MATCH_STAFF_FLUX =
			Flux.just(new MatchStaff(STF_OFF_ID, STF_WRK_ID));
	public static final Office EXPECTED_OFFICE_WITH_ID =
			new Office(STF_OFF_ID, EXPECTED_OFFICE_1.name());
	public static final Worker EXPECTED_WORKER_WITH_ID =
			new Worker(STF_WRK_ID,
					  Worker.fromIndex(11).firstName(),
					  EXPECTED_WORKER_11.lastName());

	private TestStringPatternConfig() {
		throw new IllegalStateException("Utility class");
	}
}
