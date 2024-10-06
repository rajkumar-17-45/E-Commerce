package com.ecommerce.site_project.controller;

import com.ecommerce.site_project.entity.Delivery;
import com.ecommerce.site_project.entity.DeliveryStatus;
import com.ecommerce.site_project.entity.Order;
import com.ecommerce.site_project.entity.OrderBasket;
import com.ecommerce.site_project.entity.OrderType;
import com.ecommerce.site_project.entity.User;
import com.ecommerce.site_project.service.OrdersService;
import com.ecommerce.site_project.service.UserService;
import com.ecommerce_project.repository.DeliveryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryRepository    deliveryRepository;
    @GetMapping("/orders")
    public String showOrders(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByLogin(principal.getName());
            List<Order> orders = ordersService.getAllOrdersByUser(user);
              
            model.addAttribute("orders", orders);
            
        } else {
            model.addAttribute("error", new NotFoundException("Orders was not found"));
            return "/error/404";
        }
        return "/user/orders";
    }

    @GetMapping("/payment")
    public String createOrders(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByLogin(principal.getName());
            List<OrderBasket> orderBaskets = user.getOrderBaskets();
            model.addAttribute("order", new Order());
            model.addAttribute("user", user);
            model.addAttribute("orderBaskets", orderBaskets);
            model.addAttribute("waiting", OrderType.Pending);  
            model.addAttribute("payed", OrderType.Paid);      
          
        } else {
            model.addAttribute("error", new NotFoundException("Orders for payment were not found"));
            return "/error/404";
        }
        return "checkout";
    }


    @PostMapping("/payment")
    public String saveOrder(Order newOrder, Principal principal,
                            Model model, RedirectAttributes attributes) {
        User user = userService.getUserByLogin(principal.getName());
        List<OrderBasket> orderBaskets = user.getOrderBaskets();
        newOrder.setUser(user);
        newOrder.setTotalPrice(ordersService.countSum(orderBaskets));
        
        try {
            ordersService.saveOrder(newOrder);
            
       
            Delivery delivery = new Delivery();
            delivery.setOrder(newOrder);
            delivery.setStatus(DeliveryStatus.Processing);
            deliveryRepository.save(delivery);
            
            attributes.addFlashAttribute("message", "Order was completed! Check your email!");
            sendVerificationEmail(newOrder);
        } catch (JpaSystemException | MessagingException | UnsupportedEncodingException ex) {
            model.addAttribute("error", ex.getCause().getCause().getMessage());
            return "error/404";
        }
        return "redirect:/orders";
    }

    
    private void sendVerificationEmail(Order order)
            throws MessagingException, UnsupportedEncodingException {

        String shipping = order.getShippingType() == 0 ? "Fast Delivery" : "Pay through store";

        String subject = "Thank you for ordering in Spark";
        String senderName = "Spark Store";
        String mailContent = "<p><b>Order number:</b> " + order.getId() + "</p>";
        mailContent += "<p><b>Payment:</b> " + order.getOrderStatus() + "</p>";
        mailContent += "<p><b>Shipping:</b> " + shipping + "</p>";
        mailContent += "<p><b>City:</b> " + order.getCity() + "</p>";
        mailContent += "<p><b>Address:</b> " + order.getAddress() + "</p>";
        mailContent += "<p><b>Order total:</b> " + order.getTotalPrice() + "</p>";

        mailContent += "<hr><img src='cid:logoImage' width=150 />";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("rajkumarperiyasamy7@gmail.com", senderName);
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        ClassPathResource pathResource = new ClassPathResource("/static/assets/emal.gif");
        helper.addInline("logoImage", pathResource);
        javaMailSender.send(message);
    }

}
