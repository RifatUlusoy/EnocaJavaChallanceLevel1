package experience.demo.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AddProductInCardDto {

    private Long customerId;
    private Long productId;
}
