package experience.demo.Dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long customerId;
    private Long cartId;
    private Long totalAmount;
    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
}
