package experience.demo.Model;


import experience.demo.Dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    private String name;
    private int stock;
    private Long price;



    public ProductDto getDto(){
        ProductDto productDto = new ProductDto();
        productDto.setId(this.getId());
        productDto.setCreationTimestamp(this.getCreationTimestamp());
        productDto.setUpdateTimestamp(this.getUpdateTimestamp());
        productDto.setName(name);
        productDto.setStock(stock);
        productDto.setPrice(price);
        return productDto;
    }
}
