package tshirt.ecommerce.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounterSaleProductDto {
    private Long id;
    private String name;
    private String image;
    private Float price;
    private Integer quantity = 0;
}
