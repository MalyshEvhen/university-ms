package ua.foxstudent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:tc:postgresql:12:///ums_db")
class UniversityManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
