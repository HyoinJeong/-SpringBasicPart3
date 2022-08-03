package org.prgms.kdt.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

//    @RequestMapping(value = "/customers",method = RequestMethod.GET)
    @GetMapping("/customers")
    public String findCustomers(Model model){
        var allCustomers = customerService.getAllCustomers();
        model.addAttribute("serverTime",LocalDateTime.now());
        model.addAttribute("customers",allCustomers);
        return "views/customers";
    }

    @GetMapping("/customers/new")
    public String viewNewCustomerPage(){
        return "views/new-customers";
    }

    @PostMapping("/customers/new")
    public String addNewCustomer(CreateCustomerRequest createCustomerRequest){
        customerService.createCustomer(createCustomerRequest.email(),createCustomerRequest.name());
        return "redirect:/customers";
    }
}
