package nam.gor.human.resource.department.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Worker(@Id String id,
					 String firstName,
					 String lastName) {

	public static Worker fromName(String firstName,
								  String lastName) {
		return new Worker(null, firstName, lastName);
	}

	public static Worker fromIndex(int index) {
		return Worker.fromName(
				String.format("EF-Name-%2d", index),
				String.format("EL-Name-%2d", index));
	}
}
