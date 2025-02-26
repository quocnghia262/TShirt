package tshirt.ecommerce.library.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "company")
    private String company;

    @NotNull(message = "Enter main address!")
    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @NotNull(message = "Enter city!")
    @Column(name = "city")
    private String city;

    @NotNull(message = "Enter state!")
    @Column(name = "state")
    private String state;

    @NotNull(message = "Enter postal code!")
    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_default")
    private Boolean isDefault;

    @NotNull(message = "Select Country!")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    public String toString(){
        return address1 + ", " + city + ", " + state + ", " + postalCode + ", " + country.getName();
    }
}
