package tshirt.ecommerce.library.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    //------------ Mapped Column -----------//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;
    //--------------------------------------//

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "our_price")
    private Float ourPrice;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
