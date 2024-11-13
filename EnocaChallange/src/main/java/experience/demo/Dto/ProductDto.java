package experience.demo.Dto;

import experience.demo.Model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private String name;
    private int stock;
    private Long price;

}
