package de.yagub.deliverysystem.msorder;

import de.yagub.deliverysystem.msorder.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MsOrderApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// Basic smoke test
		assertNotNull(context);
	}

	@Test
	void verifyOrderServiceBean() {
		assertNotNull(context.getBean(OrderService.class));
	}
}
