package nam.gor.human.resource.department.utils;

import java.util.function.IntBinaryOperator;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@SuppressWarnings("doclint:missing")
public final class StringPatternConfig {
	private static final String ROOT = "/";
	public static final String LOAD_DATASET_PATH =
			ROOT + "loadDataset";
	public static final String FIND_STAFF_BY_OFFICE_NAME =
			ROOT + "staff/find/findByOfficeName";
	public static final String FIND_STAFF_FLUX_BY_OFFICE_NAME =
			ROOT + "staffFlux/find/findByOfficeName";
	public static final String FIND_OFFICES =
			ROOT + "offices";
	public static final String FIND_OFFICE_BY_NAME =
			ROOT + "offices/find/findByName";
	public static final String FIND_WORKERS =
			ROOT + "workers";
	public static final String FIND_WORKERS_BY_FIRST_NAME_AND_LAST_NAME =
			ROOT + "workers/find/findByFirstNameAndLastName";

	public static final String LOAD_DATASET_RESULT =
			"The dataset was loaded with success.";
	public static final Sort NAME_SORT = Sort.by("name");
	public static final Sort LASTNAME_FIRSTNAME_SORT =
			Sort.by("lastName", "firstName");
	public static final ResponseStatusException NOT_FOUND_EXCEPTION =
			new ResponseStatusException(HttpStatus.NOT_FOUND);

	public static final IntBinaryOperator WRK_INDEX_FUN =
			(offIndex, wrkIndex) -> 100 * offIndex + wrkIndex;
	public static final int OFF_INDEX_LOWER_BOUND = 1;
	public static final int OFF_INDEX_UPPER_BOUND = 2;
	public static final int WRK_INDEX_LOWER_BOUND = 1;
	public static final int WRK_INDEX_UPPER_BOUND = 2;

	private StringPatternConfig() {
		throw new IllegalStateException("Utility class");
	}
}
