package de.yagub.deliverysystem.msorder;

import de.yagub.deliverysystem.msorder.dto.request.OrderItemRequest;
import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.model.Order;
import de.yagub.deliverysystem.msorder.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class MsOrderApplication {



	public static void main(String[] args) {
		SpringApplication.run(MsOrderApplication.class, args);



	}

}
