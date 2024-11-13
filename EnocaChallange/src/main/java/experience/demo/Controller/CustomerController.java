package experience.demo.Controller;


import experience.demo.Dto.*;
import experience.demo.Service.AdminService;
import experience.demo.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(customerService.getAllProduct());
    }
    @GetMapping("/getProduct/{name}")
    public ResponseEntity<List<ProductDto>> getProduct(@PathVariable String name){
        return ResponseEntity.ok(customerService.getProduct(name));
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCardDto addProductInCardDto) {
        return customerService.addProductToCart(addProductInCardDto);
    }

    @GetMapping("/cart/{customerId}")
    public ResponseEntity<OrderDto> getCartByCustomerId(@PathVariable Long customerId) {
        OrderDto orderDto = customerService.getCartByCustomerId(customerId);

        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{customerId}/decuct/{productId}")
    public ResponseEntity<?> removeProductToCart(@PathVariable Long customerId, @PathVariable Long productId) throws Exception {
        ResponseEntity<?> orderDto = customerService.removeProductFromCart(customerId, productId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{customerId}/add/{productId}")
    public ResponseEntity<?> addProductCart(@PathVariable Long customerId, @PathVariable Long productId ) {
        ResponseEntity<?> orderDto = customerService.addProductCart(customerId, productId);
        return ResponseEntity.ok(orderDto);
    }
    @PostMapping("/{customerId}/empty")
    public ResponseEntity<OrderDto> emptyCart(@PathVariable Long customerId) {
        OrderDto orderDto = customerService.emptyCart(customerId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        OrderDto orderDto = customerService.placeOrder(placeOrderDto);
        if (orderDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @GetMapping("/orders/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrderForCode(@PathVariable Long customerId) {
        List<OrderDto> orderDtoList = customerService.getAllOrdersForCustomer(customerId);
        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/order/{orderCode}")
    public ResponseEntity<OrderDto> getOrderForCode(@PathVariable String orderCode) {
        OrderDto orderDto = customerService.getOrderForCode( orderCode);
        if (orderDto == null) {
            return ResponseEntity.notFound().build(); // Sipariş bulunamazsa 404 döner
        }
        return ResponseEntity.ok(orderDto); // Siparişi döndür
    }
}