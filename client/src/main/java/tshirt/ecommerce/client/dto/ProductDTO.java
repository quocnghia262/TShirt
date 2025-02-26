package tshirt.ecommerce.client.dto;

import tshirt.ecommerce.library.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Product product;
    private Integer totalSold;
}
