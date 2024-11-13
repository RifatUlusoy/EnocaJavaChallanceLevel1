package experience.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import experience.demo.Dto.CartDto;
import experience.demo.Dto.CartItemDto;
import experience.demo.Dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{

    private Long totalAmount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    public CartDto getCartDto(){
        CartDto cartDto = new CartDto();
        cartDto.setId(this.getId());
        cartDto.setTotalAmount(totalAmount);
        List<CartItemDto> cartItemDtos = this.getCartItems().stream()
                .map(cartItem -> {
                    CartItemDto cartItemDto = new CartItemDto();
                    cartItemDto.setCustomerId(cartItem.getCartItemDto().getCustomerId());
                    cartItemDto.setId(cartItem.getId());
                    cartItemDto.setCartId(cartItem.getCartItemDto().getCartId());
                    cartItemDto.setTotalAmount(cartItem.getTotalAmount());
                    cartItemDto.setProductName(cartItem.getProduct().getName());
                    cartItemDto.setProductId(cartItem.getProduct().getId()); // Product ID
                    cartItemDto.setQuantity(cartItem.getQuantity()); // Quantity
                    cartItemDto.setPrice(cartItem.getPrice()); // Total Amount
                    return cartItemDto;
                })
                .collect(Collectors.toList());
        cartDto.setCartItems(cartItemDtos);
        cartDto.setCustomerId(customer.getId());
        cartDto.setOrderId(order.getId());
        cartDto.setCreationTimestamp(this.getCreationTimestamp());
        cartDto.setUpdateTimestamp(this.getUpdateTimestamp());
        return cartDto;
    }
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this); // CartItem ile Cart arasındaki ilişkiyi kur
    }



}
