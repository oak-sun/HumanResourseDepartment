package nam.gor.human.resource.department.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Office(@Id String id,
					 String name) {

	public static Office fromName(String name) {
		return new Office(null, name);
	}

	public static Office fromIndex(int index) {
		return Office.fromName(
				String.format("D-Name-%d", index));
	}
}