package org.learning.springpizza.controller;

import jakarta.validation.Valid;
import org.learning.springpizza.model.Pizza;
import org.learning.springpizza.repository.IngredientiRepository;
import org.learning.springpizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/pizza")
public class PizzaController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private IngredientiRepository ingredientiRepository;
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String searchKeyword,Model model){
        List<Pizza> pizzaList;
        if(searchKeyword != null){
            pizzaList = pizzaRepository.findByNameContaining(searchKeyword);
        } else {
            pizzaList = pizzaRepository.findAll();
        }
        model.addAttribute("pizzalist", pizzaList);
        model.addAttribute("preloadSearch", searchKeyword);
        return "pizzas/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model){
        Optional<Pizza> result = pizzaRepository.findById(id);
        if(result.isPresent()){
            Pizza pizza = result.get();
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id: " + id + " is not found");
        }
    }
    @GetMapping("/create")
    public String create(Model model){
        Pizza pizza = new Pizza();
        model.addAttribute("pizza", new Pizza());
        model.addAttribute("listaingredienti", ingredientiRepository.findAll());
        return "pizzas/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model){
    if(bindingResult.hasErrors()){
        model.addAttribute("listaingredienti", ingredientiRepository.findAll());
        return "pizzas/create";
    }
    Pizza savedPizza = pizzaRepository.save(formPizza);
    return "redirect:/pizza/show/" + savedPizza.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Pizza> result = pizzaRepository.findById(id);
        if(result.isPresent()){
        model.addAttribute("pizza", result.get());
        model.addAttribute("listaingredienti", ingredientiRepository.findAll());
        return "pizzas/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id: " + id + " is not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult){
        {
            Optional<Pizza> result = pizzaRepository.findById(id);
            if (result.isPresent()){
                Pizza pizzaToEdit = result.get();
                if (bindingResult.hasErrors()){
                    return "/pizzas/edit";
                }
                formPizza.setOffertespeciali(pizzaToEdit.getOffertespeciali());
                formPizza.setPhoto(pizzaToEdit.getPhoto());
                Pizza savedPizza = pizzaRepository.save(formPizza);
                return "redirect:/pizza/show/" + id;
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id" + id + "not found");
            }
        }
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()) {
            pizzaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("redirectMessage",
                    "pizza" + result.get().getName() + " deleted!");
            return "redirect:/pizza";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

    /* @GetMapping("/search")
    public String search(@RequestParam(name="keyword") String searchKeyword, Model model){
        List<Pizza> pizzaList = pizzaRepository.findByNameContaining(searchKeyword);
        model.addAttribute("pizzaList", pizzaList);
        return "pizzas/list";
    } */

}
