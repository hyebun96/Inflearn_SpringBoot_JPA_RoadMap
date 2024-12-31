package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void 상품주문() throws Exception{
        // Given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        // When
        Long savedIOrderId = orderService.order(member.getId(), book.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findOne(savedIOrderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 (가격 * 수량)이다.");
        Assertions.assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    void 주문취소() throws Exception{
        // Given
        Member member = createMember();
        Book item = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long savedIOrderId = orderService.order(member.getId(), item.getId(), orderCount);

        // When
        orderService.cancelOrder(savedIOrderId);
        Order findOrder = orderRepository.findOne(savedIOrderId);

        // Then
        Assertions.assertEquals(findOrder.getStatus(), OrderStatus.CANCEL, "주문 취소시 상태는 CANCEL이다.");
        Assertions.assertEquals(item.getStockQuantity(), 10, "주문이 취소된 상품은 그만큼 재고가 복구되어야 한다.");
    }

    @Test
    void 상품주문_재고수량초과(){
        // Given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10);

        int orderCount = 11;

        // When

        // Then
        Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }
}