package tshirt.ecommerce.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterSaleDto {
    private List<CounterSaleProductDto> products;
    private Long customerId;
}
