package de.yagub.deliverysystem.msprocessmanager.client.order;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.config.OrderFeignConfig;
import de.yagub.deliverysystem.msprocessmanager.error.*;
import de.yagub.deliverysystem.msprocessmanager.error.response.BaseExceptionResponse;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name          = "order-service",
        url           = "${order.service.url}",
        configuration = OrderFeignConfig.class
)
public interface OrderServiceClient {

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = OrderBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = OrderNotFoundException.class),
                    @ErrorCodes(codes = {503}, generate = OrderServiceUnavailableException.class)
            },
            defaultException = OrderProviderException.class
    )
    @PostMapping("")
    OrderResponse createOrder(@RequestBody OrderRequest request);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = OrderBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = OrderNotFoundException.class),
                    @ErrorCodes(codes = {503}, generate = OrderServiceUnavailableException.class)
            },
            defaultException = OrderProviderException.class
    )
    @GetMapping("/{id}")
    OrderResponse getOrderById(@PathVariable("id") String id);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = OrderBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = OrderNotFoundException.class),
                    @ErrorCodes(codes = {503}, generate = OrderServiceUnavailableException.class)
            },
            defaultException = OrderProviderException.class
    )
    @GetMapping("/customer/{customerId}")
    List<OrderResponse> getOrdersByCustomer(@PathVariable String customerId);


}
