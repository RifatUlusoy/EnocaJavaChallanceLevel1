package experience.demo.Service;



import experience.demo.Dto.*;
import experience.demo.Dto.Converter.CustomerDtoConverter;
import experience.demo.Exception.NegativeStockException;
import experience.demo.Model.*;
import experience.demo.OrderStatus;
import experience.demo.Repository.*;

import experience.demo.UserRole;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService  {

    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter converter;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @PostConstruct
    public void createAdminAccount(){
        Customer adminAccount = customerRepository.findByUserRole(UserRole.ADMIN);
        if(adminAccount == null){
            Customer customer = new Customer();
            customer.setUserRole(UserRole.ADMIN);
            customer.setEmail("admin@test.com");
            customer.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            customerRepository.save(customer);
        }
    }
    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
    public List<ProductDto> getProduct(String name){
        List<Product> product = productRepository.findAllByNameContaining(name);
        return product.stream().map(Product::getDto).collect(Collectors.toList());
    }


    public ResponseEntity<?> addProductToCart(AddProductInCardDto addProductInCardDto) {
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(addProductInCardDto.getCustomerId(), OrderStatus.PENDING);


        Optional<Cart> optionalCart = cartRepository.findByCustomerIdAndOrderId(addProductInCardDto.getCustomerId(), pendingOrder.getId());
        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {

            cart = new Cart();
            cart.setCustomer(customerRepository.findById(addProductInCardDto.getCustomerId()).orElseThrow(() -> new RuntimeException("Müşteri bulunamadı")));
            cart.setOrder(pendingOrder);
            cart.setTotalAmount(0L);
            cart = cartRepository.save(cart);
        }

        Optional<Product> product = productRepository.findById(addProductInCardDto.getProductId());

        if (product.isPresent()) {
            Optional<CartItem> existingCartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(addProductInCardDto.getProductId()))
                    .findFirst();

            if (existingCartItem.isPresent()) {

                return ResponseEntity.status(HttpStatus.OK).body("Product is already exists");

            } else {

                CartItem newCartItem = new CartItem();
                newCartItem.setProduct(product.get());
                newCartItem.setTotalAmount(1L * product.get().getPrice());
                newCartItem.setQuantity(1L);
                newCartItem.setPrice(newCartItem.getQuantity() * product.get().getPrice());
                newCartItem.setCart(cart);
                cart.addCartItem(newCartItem);
                cartItemRepository.save(newCartItem);
            }

            cart.setTotalAmount(cart.getCartItems().stream().mapToLong(CartItem::getTotalAmount).sum());
            cartRepository.save(cart);

            pendingOrder.setPrice(cart.getTotalAmount());
            orderRepository.save(pendingOrder);

            return ResponseEntity.status(HttpStatus.OK).body(cart.getCartDto());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ürün bulunamazsa
        }
    }



    public OrderDto emptyCart(Long customerId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        Optional<Cart> optionalCart = cartRepository.findByCustomerIdAndOrderId(customerId,pendingOrder.getId());
        if(optionalCart.isPresent()){
            Cart cart = optionalCart.get();
            cart.getCartItems().clear();
            pendingOrder.setPrice(0L);
            cart.setTotalAmount(cart.getCartItems().stream().mapToLong(CartItem::getTotalAmount).sum());
            orderRepository.save(pendingOrder);
            cartRepository.save(cart);
            return pendingOrder.getOrderDto();
        }
        return null;
    }
    public ResponseEntity<?>  removeProductFromCart(Long customerId,Long productId) throws Exception{
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        Optional<Cart> optionalCart = cartRepository.findByCustomerIdAndOrderId(customerId,pendingOrder.getId());
        if (!optionalCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found.");
        }

        Cart cart = optionalCart.get();
        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (!optionalCartItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }
        CartItem cartItem = optionalCartItem.get();
        Product product = cartItem.getProduct();
        if (cartItem.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot decrease quantity. Minimum quantity reached.");
        }

        cartItem.setQuantity(cartItem.getQuantity() - 1L);
        cartItem.setTotalAmount(cartItem.getQuantity() * product.getPrice());
        cartItemRepository.save(cartItem);

        cart.setTotalAmount(cart.getCartItems().stream().mapToLong(CartItem::getTotalAmount).sum());
        cartRepository.save(cart);
        pendingOrder.setPrice(cartItem.getTotalAmount());
        orderRepository.save(pendingOrder);
        return ResponseEntity.ok("Product quantity updated successfully.");

    }
    @Transactional
    public ResponseEntity<?> addProductCart(Long customerId,Long productId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        Optional<Cart> optionalCart = cartRepository.findByCustomerIdAndOrderId(customerId,pendingOrder.getId());
        if (!optionalCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found.");
        }

        Cart cart = optionalCart.get();
        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (!optionalCartItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }
        CartItem cartItem = optionalCartItem.get();
        Product product = cartItem.getProduct();

        cartItem.setQuantity(cartItem.getQuantity() + 1L);
        cartItem.setTotalAmount(cartItem.getQuantity() * product.getPrice());
        cartItemRepository.save(cartItem);

        cart.setTotalAmount(cart.getCartItems().stream().mapToLong(CartItem::getTotalAmount).sum());
        cartRepository.save(cart);
        pendingOrder.setPrice(cart.getTotalAmount());
        orderRepository.save(pendingOrder);
        return ResponseEntity.ok("Product quantity updated successfully.");
    }
    public OrderDto getCartByCustomerId(Long customerId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        List<CartDto> cartDtoList = pendingOrder.getCarts().stream().map(Cart::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setCartDtoList(cartDtoList);
        orderDto.setAddress(pendingOrder.getAddress());
        orderDto.setEmail(pendingOrder.getCustomer().getEmail());
        orderDto.setTotalAmount(pendingOrder.getCarts().getFirst().getTotalAmount());
        orderDto.setCreationTimestamp(pendingOrder.getCreationTimestamp());
        orderDto.setUpdateTimestamp(pendingOrder.getUpdateTimestamp());
        orderDto.setPrice(pendingOrder.getPrice());
        orderDto.setId(pendingOrder.getId());
        orderDto.setOrderStatus(pendingOrder.getOrderStatus());
        return orderDto;
    }
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order existingOrder = orderRepository.findByCustomerIdAndOrderStatus(placeOrderDto.getCustomerId(), OrderStatus.PENDING);
        Optional<Customer> user = customerRepository.findById(placeOrderDto.getCustomerId());
        if(user.isPresent()){
            for (CartItem cartItem : existingOrder.getCarts().getFirst().getCartItems()) {
                Product product = cartItem.getProduct();

                // Ürün stok miktarını kontrol et
                if (product.getStock() < cartItem.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }
                product.setStock((int) (product.getStock() - cartItem.getQuantity()));
                productRepository.save(product);
            }
            existingOrder.setOrderStatus(OrderStatus.SUBMITTED);
            existingOrder.setAddress(placeOrderDto.getAddress());
            existingOrder.setCreationTimestamp(new Order().getCreationTimestamp());
            existingOrder.setPrice(existingOrder.getPrice());
            orderRepository.save(existingOrder);
            Order order = new Order();
            order.setOrderStatus(OrderStatus.SUBMITTED);
            order.setCustomer(user.get());
            order.setPrice(0L);
            orderRepository.save(order);
            return order.getOrderDto();
        }
        return null;
    }
    public OrderDto getOrderForCode(String orderCode) {

        Optional<Order> order = orderRepository.findByOrderCode( orderCode);
        //Random code for example
        if(orderCode.equals("asdf")){
            return order.map(Order::getOrderDto).orElse(null);
        }
        return null;

    }
    public List<OrderDto> getAllOrdersForCustomer(Long customerId){
        return orderRepository.findAllByCustomerIdAndOrderStatus(customerId,OrderStatus.SUBMITTED).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }


    public Boolean existsByEmail(String email){
        return customerRepository.existsByEmail(email);
    }


    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }


}
