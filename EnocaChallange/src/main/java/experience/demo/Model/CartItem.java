package experience.demo.Model;

import experience.demo.Dto.CartItemDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem extends BaseEntity{

    private Long quantity;
    private Long price;
    private Long totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public CartItemDto getCartItemDto(){
        CartItemDto dto = new CartItemDto();
        dto.setId(this.getId());
        dto.setProductId(product.getId());
        dto.setQuantity(quantity);
        dto.setPrice(price);
        dto.setTotalAmount(totalAmount);
        dto.setCartId(cart.getId());
        dto.setCustomerId(getCart().getCustomer().getId());
        return dto;
    }
}
