package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogController {

    @Autowired
    private CatalogService catalogService;


    @GetMapping("/catalog/main")
    public String viewMain(){
        return "catalog/main";
    }


    //如何从客户端网页获取值，@RequestParam;如何将服务端控制器中的值传递给客户端网页，Model
    @GetMapping("/catalog/category")
    public String viewCategory(@RequestParam("categoryId") String categoryId, Model model)
    {
        if(categoryId != null){
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category" , category);
            model.addAttribute("productList" , productList);
        }

        return "catalog/category";
    }

    @GetMapping("/catalog/product")
    public String viewProduct(@RequestParam("productId") String productId, Model model)
    {
        Item item = catalogService.getItem(productId);
        List<Item> itemList = catalogService.getItemListByProduct(productId);
        model.addAttribute("item",item);
        model.addAttribute("itemList",itemList);
        return "catalog/product";
    }

    @GetMapping("/catalog/item")
    public String viewItem(@RequestParam("itemId") String itemId, Model model)
    {
        Item item = catalogService.getItem(itemId);
        model.addAttribute("item",item);
        return "catalog/item";
    }

    @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
    public String viewSearchProduct(@RequestParam("keyword") String keyword, Model model){

        if(keyword != null){

            List<Product> productList = catalogService.searchProductList(keyword);
            model.addAttribute("productList" , productList);
        }
        return "catalog/searchProduct";
    }
}
