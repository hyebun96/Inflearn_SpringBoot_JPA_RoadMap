package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderRepository orderRepository;

    /* 주문 조회 - 엔티티 직접노출(지양) */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();        // LAZY 초기화
            order.getDelivery().getAddress();   // LAZY 초기화
            List<OrderItem> orderItems = order.getOrderItems(); // LAZY 초기화
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
}
