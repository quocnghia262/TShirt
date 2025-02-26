package tshirt.ecommerce.library.service;

import tshirt.ecommerce.library.model.Customer;
import tshirt.ecommerce.library.model.Order;
import tshirt.ecommerce.library.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    public List<Product> getTopProducts(int top) {
        return productService.topMostOrderedProducts(top);
    }

    public List<Customer> getTopCustomers(int top) {
        return customerService.topMostOrderedCustomers(top);
    }

    public Map<Integer, Double> getRevenueStatistic() {
        List<Order> orderList = orderService.findAll();
        return orderList.stream().collect(Collectors.groupingBy(
                order -> order.getOrderDate().getMonth() + 1,
                Collectors.summingDouble(Order::getSubTotal)
        ));
    }

    public Integer getTotalRevenue() {
        return orderService.findAll().stream().mapToInt(order-> order.getSubTotal().intValue()).sum();
    }

    public Integer getMaxRevenueInMonth() {
        return Collections.max(getRevenueStatistic().entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue().intValue();
    }
}
