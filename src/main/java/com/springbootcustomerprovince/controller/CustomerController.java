package com.springbootcustomerprovince.controller;

import com.springbootcustomerprovince.model.Customer;
import com.springbootcustomerprovince.model.Province;
import com.springbootcustomerprovince.service.ICustomerService;
import com.springbootcustomerprovince.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProvinceService provinceService;

    // Read + Paging
    @ModelAttribute("provinces")
    public Iterable<Province> listProvinces() {
        return provinceService.findAll();
    }

    @GetMapping
    public ModelAndView listCustomers(@PageableDefault(value = 5) Pageable pageable, @RequestParam("search") Optional<String> search) {
        Page<Customer> customers;
        if(search.isPresent()){
            customers = customerService.findAllByNameContainingIgnoreCase(pageable, search.get());
        } else {
            customers = customerService.findAll(pageable);
        }
        System.out.println("Hello");
        ModelAndView modelAndView = new ModelAndView("customer/list");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

//    @GetMapping
//    public ModelAndView listCustomers(
//            @PageableDefault(size = 3, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable,
//            @RequestParam("search") Optional<String> search) {
//        Page<Customer> customers;
//
//        if(search.isPresent()){
//            customers = customerService.findAllByFirstNameContaining(pageable, search.get());
//        } else {
//            customers = customerService.findAll(pageable);
//        }
//        ModelAndView modelAndView = new ModelAndView("customer/list");
//        modelAndView.addObject("customers", customers);
//        return modelAndView;
//    }


    // Old code
//    @GetMapping
//    public ModelAndView listCustomers() {
//        ModelAndView modelAndView = new ModelAndView("/customer/list");
//        modelAndView.addObject("customers", customerService.findAll());
//        return modelAndView;
//    }
    //


    // Create

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("message", "Added a new customer");
        return modelAndView;
    }

    // Update

    @GetMapping("/update/{id}")
    public ModelAndView showEditForm(@PathVariable int id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/customer/update");
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            return new ModelAndView("/error_404");
        }
    }

    @PostMapping("/update")
    public ModelAndView updateCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/update");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }

    // Delete

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable int id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/customer/delete");
            modelAndView.addObject("customer", customer.get());
            return modelAndView;
        } else {
            return new ModelAndView("/error_404");
        }
    }

    @PostMapping("/delete")
    public String deleteCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.remove(customer.getId());
        return "redirect:/customers";
    }


}
