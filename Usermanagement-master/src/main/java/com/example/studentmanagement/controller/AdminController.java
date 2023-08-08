package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.UserInfo;
import com.example.studentmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

//    CONNECTION WITH SERVICE LAYER
    @Autowired
    UserService userService;

//    adduser encoder
    @Autowired
    private PasswordEncoder passwordEncoder;

//    ALL method()
    @GetMapping("/adminhome")

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAll(Model model){
       List<UserInfo> users = userService.findAll();
        model.addAttribute("users", users);
        return "dashboard";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String editUser(@PathVariable Long id, Model model){
        Optional<UserInfo> user = userService. getUserdetails(id);
        model.addAttribute("id",id);
        model.addAttribute("username",user.get().getUsername());
        model.addAttribute("firstname",user.get().getFirstname());
        model.addAttribute("lastname",user.get().getLastname());
        model.addAttribute("email",user.get().getEmail());
        model.addAttribute("password",user.get().getPassword());
        return "edit-details";
    }



/*   add user */
    @GetMapping("/adduser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public  String addusers(){
        return "add-user";
    }


    /*edit user*/
    @GetMapping("/edits")

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public  String editpage(){
        return "edit-details";
    }


    /*save*/
    @PostMapping("/save")
    public  String save(@ModelAttribute UserInfo user){
        user.setRoles("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin/adminhome";
    }


    /*    edit user*/
    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id){
        userService.delete(id);
        return "redirect:/admin/adminhome";
    }


    /*search*/
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String search(Model model,@RequestParam String keyword){
        List<UserInfo> users;
        if(keyword == null){
            users = userService.loadAllUsers();
        }
        else{
            users = userService.searchUsers(keyword);
//            System.out.println(users);
        }

        model.addAttribute("users",users);

        return "dashboard";
    }

    /*logout*/
    @GetMapping("/logout")
    public String handleLogoutRequest(HttpServletRequest request) {

        request.getSession().invalidate();
        return "redirect:/login?logout";
    }

}
