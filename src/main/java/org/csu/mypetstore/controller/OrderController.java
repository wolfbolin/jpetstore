package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/newOrderForm")
    public String newOrder(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            Order order = new Order();

            Cart cart =(Cart)session.getAttribute("cart");
            order.initOrder(account, cart);

            model.addAttribute("order", order);
            session.setAttribute("allOrder", order);
            return "order/newOrderForm";
        } else {
            return "account/SignonForm";
        }
    }

    @RequestMapping(value = "/order/comfirmOrder")
    public String confirmOrder(HttpServletRequest request, @ModelAttribute(value = "order") Order neworder, Model model) {

        String shippingAddressRequired = request.getParameter("shippingAddressRequired");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String accountName = account.getUsername();
        neworder.setUsername(accountName);
        neworder.setOrderDate(new Date());

        Order allOrder = (Order) session.getAttribute("allOrder");
        neworder.setCourier(allOrder.getCourier());
        neworder.setTotalPrice(allOrder.getTotalPrice());
        neworder.setLocale(allOrder.getLocale());
        neworder.setStatus(allOrder.getStatus());
        neworder.setExpiryDate(allOrder.getExpiryDate());

        model.addAttribute("neworder", neworder);
        session.setAttribute("neworder", neworder);
        if (shippingAddressRequired == null) {

            neworder.setShipToFirstName(allOrder.getShipToFirstName());
            neworder.setShipToLastName(allOrder.getShipToLastName());
            neworder.setShipAddress1(allOrder.getShipAddress1());
            neworder.setShipAddress2(allOrder.getShipAddress2());
            neworder.setShipCity(allOrder.getShipCity());
            neworder.setShipState(allOrder.getShipState());
            neworder.setShipZip(allOrder.getShipZip());
            neworder.setShipCountry(allOrder.getShipCountry());

            model.addAttribute("neworder", neworder);
            session.setAttribute("neworder", neworder);
            orderService.insertOrder(neworder);
            Cart cart = new Cart();
            session.setAttribute("cart",cart);
            System.out.println("id:"+neworder.getOrderId());
            return "order/comfirmOrder";
        } else {

            model.addAttribute("allOrder", allOrder);
            session.setAttribute("neworder", neworder);

            return "order/shippingForm";
        }
    }

    @RequestMapping(value = "/order/shipping")
    public String shippingOrder(HttpServletRequest request, @ModelAttribute(value = "order") Order shippingOrder, Model model) {
        HttpSession session = request.getSession();
        Order neworder = (Order) session.getAttribute("neworder");

        neworder.setShipToFirstName(shippingOrder.getShipToFirstName());
        neworder.setShipToLastName(shippingOrder.getShipToLastName());
        neworder.setShipAddress1(shippingOrder.getShipAddress1());
        neworder.setShipAddress2(shippingOrder.getShipAddress2());
        neworder.setShipCity(shippingOrder.getShipCity());
        neworder.setShipState(shippingOrder.getShipState());
        neworder.setShipZip(shippingOrder.getShipZip());
        neworder.setShipCountry(shippingOrder.getShipCountry());
        neworder.setCourier(shippingOrder.getCourier());
        neworder.setTotalPrice(shippingOrder.getTotalPrice());
        model.addAttribute("neworder", neworder);
        session.setAttribute("neworder", neworder);

        return "order/comfirmOrder";

    }

    @GetMapping("/order/viewOrder")
    public String viewOrder(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Order neworder = (Order) session.getAttribute("neworder");
        Order allOrder = (Order) session.getAttribute("allOrder");
        neworder.setLineItems(allOrder.getLineItems());
        model.addAttribute("neworder", neworder);
      return  "order/viewOrder";
    }

    @GetMapping("/order/myOrder")
    public String myOrder(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Account account = (Account)session.getAttribute("account");
        List<Order> orderList = orderService.getOrdersByUsername(account.getUsername());

        model.addAttribute("orderList",orderList);
        return  "order/myOrder";
    }
}
