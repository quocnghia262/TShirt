package tshirt.ecommerce.admin.controller;

import tshirt.ecommerce.library.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class StatisticController {
    @Autowired
    private StatisticService statisticService;
    @RequestMapping("/statistic")
    public String statisticPage(Model model){
        model.addAttribute("top5products", statisticService.getTopProducts(5));
        model.addAttribute("top5customers", statisticService.getTopCustomers(5));
        model.addAttribute("revenueStatistic", statisticService.getRevenueStatistic());
//        model.addAttribute("maxInMonth", statisticService.getMaxRevenueInMonth());
        model.addAttribute("totalRevenue", statisticService.getTotalRevenue());
        return "statistic/index";
    }
}
