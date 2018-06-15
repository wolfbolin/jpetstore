package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.VerifyUtil;
import org.csu.mypetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/account/SignonForm")
    public String viewSignonForm(Model model){
        boolean lang1=true;
        model.addAttribute("lang1",lang1);
        return "account/SignonForm";
    }


    @PostMapping("/Sign")
    public String SignOn(@RequestParam String username,@RequestParam String password,@RequestParam String checkcode, HttpSession session){
        Account account = accountService.getAccount(username,password);
        String str=" ";
        if(account!=null && session.getAttribute("imageCode").toString().equalsIgnoreCase(checkcode)){
            str = "catalog/main";
            session.setAttribute("account",account);
        }

        else  str = "account/SignonForm";
        return str;
    }

    @GetMapping("/NewAccount")
    public String viewNewAccount(Model model){
        Account account = new Account();
        boolean lang2=true;
        model.addAttribute("lang2",lang2);
        model.addAttribute("account",account);
        return "account/NewAccountForm";
    }

    @RequestMapping(value = "/SaveAccount", method=RequestMethod.POST)
    public String viewSaveAccount(@ModelAttribute(value="account")Account account){
        accountService.insertAccount(account);
        return "catalog/main";
    }

    @GetMapping("/account/EditAccount")
    public String viewMyAccount(@RequestParam("username") String username,Model model){
        Account account = accountService.getAccount(username);
        model.addAttribute("account",account);
        return "account/EditAccountForm";
    }

    @RequestMapping(value = "/SaveEdit", method=RequestMethod.POST)
    public String viewSaveEdit(@RequestParam("username") String username,@ModelAttribute(value="account") Account account){
        account.setUsername(username);
        accountService.updateAccount(account);
        return "catalog/main";
    }

    @GetMapping("/SignOut")
    public String viewSignOut(HttpSession session){
        Account account = null;
        session.setAttribute("account",account);
        return "catalog/main";
    }

    //===========用户名验证=============================
    @GetMapping("/usernameValidation")
    public void usernameCheck(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        String username = request.getParameter("username");
        Account account = accountService.getAccount(username);

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        if(account!=null){
            out.println("<msg>Exist</msg>");
        }else{
            out.println("<msg>NotExist</msg>");
        }
        out.flush();
        out.close();
    }

   // ==========验证码生成====================
    @GetMapping("/createImage")
    public void getCode(HttpServletResponse response, HttpServletRequest request) throws Exception{
        HttpSession session=request.getSession();
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("imageCode",objs[0]);

        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    //=============国际化===================
   @GetMapping("/sign/language")
    public String viewInternational1(Model model){
       boolean lang1=true;
       model.addAttribute("lang1",lang1);
        return "account/SignonForm";
    }

    @GetMapping("/new/language")
    public String viewInternational2(Model model){
        Account account = new Account();
        boolean lang2=true;
        model.addAttribute("lang2",lang2);
        model.addAttribute("account",account);
        return "account/NewAccountForm";
    }
}
