package nam.gor.human.resource.department.dao;

import nam.gor.human.resource.department.utils.DatasetLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.SystemProperties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class DaoTests {
	@Autowired
	protected OfficeDao officeDao;
	@Autowired
	protected WorkerDao workerDao;
	@Autowired
	protected MatchStaffDao matchStaffDao;

	public DaoTests() {
		super();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry properties) {
		if (SystemProperties
				         .get("os.name")
				         .startsWith("Windows")) {
			properties
					.add("spring.data.mongodb.host",
							() -> "localhost");
		}
	}

	@BeforeAll
	public void initAll() {
		DatasetLoader.load(officeDao, workerDao, matchStaffDao);
	}

	@AfterAll
	public void tearDownAll() {
		DatasetLoader.delete(officeDao, workerDao, matchStaffDao);
	}
}