package experience.demo.Model;


import experience.demo.Dto.OrderDto;
import experience.demo.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    private Long price;
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    private String orderCode;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order",cascade = CascadeType.ALL)
    private List<Cart> carts;


    public OrderDto getOrderDto(){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(this.getId());
        return orderDto;
    }


}
