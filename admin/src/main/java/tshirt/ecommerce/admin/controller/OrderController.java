package tshirt.ecommerce.admin.controller;

import tshirt.ecommerce.library.model.Order;
import tshirt.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    private String list_template="/order/list-order";
    private String detail_template="/order/order-details";
    @GetMapping("/list")
    public String listOrder(Model model) {
        List<Order> orderList = orderService.findAll();
        model.addAttribute("orderList", orderList);

        return list_template;
    }

    @GetMapping("/accept/{id}")
    public String acceptOrder(Model model, @PathVariable Long id) {
        Order order = orderService.get(id);
        order.setOrderStatus("Accepted");
        orderService.save(order);

        List<Order> orderList = orderService.findAll();
        model.addAttribute("orderList", orderList);

        return list_template;
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(Model model, @PathVariable Long id) {
        Order order = orderService.get(id);
        order.setOrderStatus("Canceled");
        orderService.save(order);

        List<Order> orderList = orderService.findAll();
        model.addAttribute("orderList", orderList);

        return list_template;
    }

    @GetMapping("/order-details")
    public String orderDetail(Model model, @RequestParam("id") Long id){
        Order order = orderService.get(id);
        model.addAttribute("order", order);
        return detail_template;
    }
}
