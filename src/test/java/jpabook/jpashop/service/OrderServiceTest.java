package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // arrange
        int initialStockQuantity = 10;
        int orderCount = 2;
        Member member = createMember();
        Book book = createBook("JPA", initialStockQuantity, 10000);

        // act
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order result = orderRepository.findOne(orderId);

        // assert
        // 상품 주문시 상태는 ORDER
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER);
        // 주문한 상품 종류 수가 정확해야 한다.
        assertThat(result.getOrderItems().size()).isEqualTo(1);
        // 주문 가격은 가격 * 수량이다.
        assertThat(result.getTotalPrice()).isEqualTo(book.getPrice() * orderCount);
        // 주문 수량만큼 재고가 줄어야 한다.
        assertThat(book.getStockQuantity()).isEqualTo(initialStockQuantity - orderCount);
    }

    @Test
    void 주문취소() {
        // arrange
        int initialStockQuantity = 10;
        int orderCount = 2;
        Member member = createMember();
        Book book = createBook("JPA", initialStockQuantity, 10000);
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // act
        orderService.cancelOrder(orderId);

        // assert
        Order order = orderRepository.findOne(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(initialStockQuantity);
    }

    @Test
    void 상품주문_재고수량초과() {
        // arrange
        Member member = createMember();
        Book book = createBook("JPA", 10, 10000);

        int orderCount = 11;

        // act & assert
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });

    }

    @Test
    void findOrder() {
    }

    private Book createBook(String name, int stockQuantity, int price) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "가산로", "143-49"));
        em.persist(member);
        return member;
    }
}