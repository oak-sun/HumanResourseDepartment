package nam.gor.human.resource.department.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The aggregate of {@link Office} and {@link Worker}s.<br>
 * This object is <b>NOT</b> persisted in the MongoDB.
 * 
 * @param office the {@link Office}
 * @param workers  the list of the {@link Worker}s
 */
public record Staff(Office office,
					SortedSet<Worker> workers) {

	public static Staff of(Office office) {
		final Comparator<Worker> comparator = Comparator
					              	.comparing(Worker::lastName)
					             	.thenComparing(Worker::firstName)
					            	.thenComparing(Worker::id);
		final SortedSet<Worker> set = Collections
				.synchronizedSortedSet(new TreeSet<>(comparator));
		return new Staff(office, set);
	}

	public Staff newWorker(Worker worker) {
		if (Objects.nonNull(worker)) {
			this.workers().add(worker);
		}
		return this;
	}
}