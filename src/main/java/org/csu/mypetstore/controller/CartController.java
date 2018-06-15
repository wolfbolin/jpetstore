package org.csu.mypetstore.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    private CatalogService catalogService;

    private Cart cart;

    @RequestMapping(value = "/cart/updateCart", method = RequestMethod.POST)
    public String updateCart(HttpServletRequest request,Model model){

        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();

            try {
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                cart.setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                }
            } catch (Exception e) {
                model.addAttribute("message", "The Quantities of Item must be Integer!");
                return "common/Error";
            }
        }
        model.addAttribute("cart", cart);
        HttpSession session = request.getSession();
        session.setAttribute("cart",cart);

        return  "cart/cart";
    }

    @GetMapping("/cart/category")
    public String viewCatalog(@RequestParam("categoryId") String categoryId, Model model){

        if(categoryId != null){
            Category category = catalogService.getCategory(categoryId);
            model.addAttribute("category" , category);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("productList" , productList);
        }

        return "catalog/category";

    }

    @GetMapping("/cart/cart")
    public String addCart(HttpServletRequest request,@RequestParam("workingItemId") String workingItemId,Model model){

        if (cart == null) {
            cart = new Cart();
        }

        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {

            boolean isInStock = catalogService.isItemInStock(workingItemId);
            Item item = catalogService.getItem(workingItemId);

            cart.addItem(item, isInStock);
        }

        model.addAttribute("cart",cart);
        HttpSession session = request.getSession();
        session.setAttribute("cart",cart);

        return "cart/cart";
    }

    @GetMapping("/catalog/cart")
    public String viewCart(HttpServletRequest request,Model model){

        if (cart == null) {
            cart = new Cart();
        }

        model.addAttribute("cart",cart);
        HttpSession session = request.getSession();
        session.setAttribute("cart",cart);
        cart= new Cart();
        return "cart/cart";
    }

    @GetMapping("/cart/removeCart")
    public String removeCart(HttpServletRequest request,@RequestParam("workingItemId") String workingItemId,Model model){

        Item item = cart.removeItemById(workingItemId);

        if (item == null) {
            model.addAttribute("message", "Attempted to remove null CartItem from Cart.");
            return "common/Error";
        } else {
            model.addAttribute("cart",cart);
        }

        HttpSession session = request.getSession();
        session.setAttribute("cart",cart);

        return "cart/cart";
    }

}
