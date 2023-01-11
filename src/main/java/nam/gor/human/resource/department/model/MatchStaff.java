package nam.gor.human.resource.department.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record MatchStaff(String officeId,
						 String workerId) {

	public static MatchStaff of(Office office,
								Worker worker) {
		return new MatchStaff(office.id(), worker.id());
	}
}
