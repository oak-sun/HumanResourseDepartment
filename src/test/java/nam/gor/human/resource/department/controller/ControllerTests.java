package nam.gor.human.resource.department.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.SystemProperties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTests {

	public ControllerTests() {
		super();
	}

	@Autowired
	protected MockMvc mockMvc;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry properties) {

		if (SystemProperties
				.get("os.name")
				.startsWith("Windows")) {
			properties.add("spring.data.mongodb.host",
					        () -> "localhost");
		}
	}
}
