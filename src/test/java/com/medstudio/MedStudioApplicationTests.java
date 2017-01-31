package com.medstudio;

import static org.assertj.core.api.Assertions.assertThat;

import com.medstudio.controllers.ConfigController;
import com.medstudio.controllers.ResultController;
import com.medstudio.controllers.UserController;
import com.medstudio.models.entity.Config;
import com.medstudio.models.entity.Result;
import com.medstudio.models.entity.User;
import com.medstudio.models.repository.ConfigRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MedStudioApplicationTests {

	@Autowired
	UserController userController;

	@Autowired
	ResultController resultController;

	@Autowired
	ConfigController configController;

	@Test
	public void addUpdateRemoveUser() throws InterruptedException {

		User userForTest = new User();
		userForTest.setName("Test");
		userForTest.setSurname("User");
		userForTest.setEnabled(true);
		userForTest.setPassword("123");
		userForTest.setLogin("TestLogin");
		userForTest.setEmail("test@email.com");

		assertThat(userController.updateUser(userForTest)).isNotNull();

		userForTest.setName("NewTestName");

		userForTest = userController.updateUser(userForTest);
		assertThat(userController.updateUser(userForTest).getName().equals("NewTestName")).isTrue();

		resultController.addResult(userForTest.getId(), 0L, "120/60");
		Thread.sleep(2000);
		resultController.addResult(userForTest.getId(), 0L, "120/70");
		Thread.sleep(2000);
		resultController.addResult(userForTest.getId(), 0L, "120/54");

		Map res = resultController.resultsType(userForTest.getId(), 0L, LocalDateTime.now());
		assertThat(res).isNotNull();
        assertThat(res.keySet().size()).isEqualTo(3);

		userController.deleteUser(userForTest.getId());
	}

	@Test
	public void getUserConfig() {

		User userForTest = new User();
		userForTest.setName("Test");
		userForTest.setSurname("User");
		userForTest.setEnabled(true);
		userForTest.setPassword("123");
		userForTest.setLogin("TestLogin2");
		userForTest.setEmail("test@email.com");

		assertThat(userController.updateUser(userForTest)).isNotNull();

		Config newConfig = new Config();
		newConfig.setUser(userForTest);
		newConfig.setCounter(2L);
		newConfig.setMeasurementInterval(5L);
		configController.updateConfig(newConfig);

		Config config = (Config) configController.getConfigByUserId(userForTest.getId()).get(0);
		assertThat(config).isNotNull();
		assertThat(config.getCounter()).isEqualTo(2L);
		assertThat(config.getMeasurementInterval()).isEqualTo(5L);

		userController.deleteUser(userForTest.getId());
	}
}
