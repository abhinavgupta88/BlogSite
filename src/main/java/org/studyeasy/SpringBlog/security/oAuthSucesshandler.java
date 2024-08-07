package org.studyeasy.SpringBlog.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringBlog.models.Account;
import org.studyeasy.SpringBlog.services.AccountService;

@Component
public class oAuthSucesshandler implements AuthenticationSuccessHandler {

    Logger logger=LoggerFactory.getLogger(oAuthSucesshandler.class);
    @Autowired
  private  AccountService accountService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        logger.info("oAuthSucesshandler");

     DefaultOAuth2User user=  (DefaultOAuth2User) authentication.getPrincipal();
     
      logger.info(user.getName());
      user.getAttributes().forEach((key,value)->{
        logger.info("{} => {}",key,value);
      });

      String email = user.getAttribute("email") != null ? user.getAttribute("email").toString() : "unknown email";
      String fname = user.getAttribute("given_name") != null ? user.getAttribute("given_name").toString() : "unknown first name";
      String lname = user.getAttribute("family_name") != null ? user.getAttribute("family_name").toString() : "unknown last name";
      String picture = user.getAttribute("picture") != null ? user.getAttribute("picture").toString() : "no picture";

      // Log the details
      logger.info("Email: {}", email);
      logger.info("First Name: {}", fname);
      logger.info("Last Name: {}", lname);
      logger.info("Picture: {}", picture);
      Account account =new Account();

     account.setFirstname(fname);
     account.setLastname(lname);
     account.setEmail(email);
     account.setPhoto(picture);
     account.setGender("male");
     account.setPassword("dummy");
     account.setAge(20);
     
      Optional<Account> optionalAccount = accountService.findOneByEmail(email);
     if(!optionalAccount.isPresent())
     {
        accountService.save(account);
         System.out.println("user saved:" + email);
     }





        new DefaultRedirectStrategy().sendRedirect(request, response,"/");
       
    }
    
}
