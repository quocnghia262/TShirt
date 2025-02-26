package tshirt.ecommerce.library.service;


import tshirt.ecommerce.library.model.*;
import tshirt.ecommerce.library.repository.CustomerRepository;
import tshirt.ecommerce.library.repository.OrderDetailRepository;
import tshirt.ecommerce.library.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tshirt.ecommerce.library.repository.ProductRepository;
import tshirt.ecommerce.library.web.dto.CounterSaleDto;
import tshirt.ecommerce.library.web.dto.CounterSaleProductDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public List<Order> findAllOrders(Customer customer) {
        return customer.getOrders();
    }
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order save(Order order){
        return orderRepository.save(order);
    }
    public Order saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setCustomer(shoppingCart.getCustomer());
        order.setOrderDate(new Date());
        order.setDeliveryDate(new Date());
        order.setSubTotal(shoppingCart.getSubTotal());
        if(shoppingCart.getShippingMethod().equals("Flat Shipping")){
            order.setShippingTotal(50f);
        }
        else{
            order.setShippingTotal(shoppingCart.getShippingTotal());
        }
        order.setTaxRate(shoppingCart.getTaxRate());
        order.setTaxTotal(shoppingCart.getTaxTotal());
        order.setGrandTotal(shoppingCart.getGrandTotal());
        order.setShippingMethod(shoppingCart.getShippingMethod());
        order.setPaymentMethod(shoppingCart.getPaymentMethod());
        order.setOrderStatus("Pending");
        order.setDescription(shoppingCart.getDescription());
        order.setShippingAddress(shoppingCart.getShippingAddress());

        //order details
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        for(CartItem cartItem : shoppingCart.getCartItemList()){
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setOurPrice(cartItem.getOurPrice());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setTotalPrice(cartItem.getTotalPrice());
            orderDetail.setIsDeleted(false);

            orderDetailList.add(orderDetail);
        }
        //set order details in list
        order.setOrderDetailList(orderDetailList);

        orderRepository.save(order);

        return order;
    }


    public Order get(long  id) {
        return orderRepository.findById(id).get();
    }

    public void delete(long id) {
        orderRepository.deleteById(id);
    }

    public void createOrder(CounterSaleDto counterSaleDto) {
        Float total = 0f;
        for(CounterSaleProductDto counterSaleProductDto : counterSaleDto.getProducts()){
            Product product = productRepository.findById(counterSaleProductDto.getId()).get();
            total += counterSaleProductDto.getQuantity() * product.getOurPrice();
        }
        Customer customer = customerRepository.findById(counterSaleDto.getCustomerId()).get();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus("Accepted");
        order.setOrderDate(new Date());
        order.setDeliveryDate(new Date());
        order.setPaymentMethod("Counter Sale");
        order.setTaxRate(0f);
        order.setShippingTotal(0f);
        order.setTaxTotal(0f);
        order.setGrandTotal(total);
        order.setSubTotal(total);

        Order savedOrder = orderRepository.save(order);

        for(CounterSaleProductDto counterSaleProductDto : counterSaleDto.getProducts()){
            if(counterSaleProductDto.getQuantity() > 0){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(savedOrder);
                Product product = productRepository.findById(counterSaleProductDto.getId()).get();
                orderDetail.setProduct(product);
                orderDetail.setOurPrice(product.getOurPrice());
                orderDetail.setQuantity(Long.valueOf(counterSaleProductDto.getQuantity()));
                orderDetail.setTotalPrice(product.getOurPrice() * counterSaleProductDto.getQuantity());
                orderDetailRepository.save(orderDetail);
            }
        }
    }
}