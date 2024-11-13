package experience.demo.Dto;

import lombok.Data;

@Data
public class UpdateCartRequest {
    private Long customerId;
    private Long productId;
}
