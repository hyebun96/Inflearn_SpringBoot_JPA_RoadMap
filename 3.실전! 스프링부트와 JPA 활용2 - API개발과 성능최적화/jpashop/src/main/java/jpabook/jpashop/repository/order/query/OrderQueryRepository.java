package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /* 주문 조회 - JPA에서 DTO 직접조회 */
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();  // Query 1번 -> N개
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItemQueryDtos = findOrderItems(o.getOrderId());    // N + 1 발생 : Query N번
            o.setOrderItems(orderItemQueryDtos);
        });
        return result;
    }

    /* 주문 조회 - JPA에서 DTO 직접조회(컬렉션 조회 최적화) */
    /* query 총 2 번나감 */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = toOrderId(result);
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {

        List<OrderItemQueryDto> orderItems =
                em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(" +
                        "oi.order.id, i.name, i.price, oi.count)" +
                        " from OrderItem oi " +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private List<Long> toOrderId(List<OrderQueryDto> result) {
        return result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
    }

    /* 주문 조회 - JPA에서 DTO 직접조회(플랫 데이터 최적화) */
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderFlatDto(" +
                "o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i" +
                "", OrderFlatDto.class).getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(" +
                                "o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(" +
                        "oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi " +
                        " join oi.item i" +
                        " where oi.order.id = :orderId" , OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
