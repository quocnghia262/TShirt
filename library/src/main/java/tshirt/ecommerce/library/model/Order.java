package tshirt.ecommerce.library.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    //------------ Mapped Column -----------//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address shippingAddress;
    //-------------------------------------//

    @Column(name ="order_date")
    private Date orderDate;

    @Column(name ="delivery_date")
    private Date deliveryDate;

    @Column(name = "sub_total")
    private Float subTotal;

    @Column(name = "shipping_total")
    private Float shippingTotal;

    //Default 5 %
    @Column(name = "tax_rate")
    private Float taxRate;

    @Column(name = "tax_total")
    private Float taxTotal;

    @Column(name = "grand_total")
    private Float grandTotal;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "description")
    private String description;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "payment_method")
    private String paymentMethod;


}
