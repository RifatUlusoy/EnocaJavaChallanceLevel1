package experience.demo.Dto;

import experience.demo.Model.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class CartDto {

    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private Long totalAmount;
    private List<CartItemDto> cartItems;
    private Long customerId;
    private Long orderId;
}
