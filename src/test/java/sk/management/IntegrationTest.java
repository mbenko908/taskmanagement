package sk.management

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.Test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringbootTest.Webenvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class IntegrationTest {
	@Autowired
	protexted TestRestTemplate restTemplate;
}
