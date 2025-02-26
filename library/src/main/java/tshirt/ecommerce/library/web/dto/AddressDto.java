package tshirt.ecommerce.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String company;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
