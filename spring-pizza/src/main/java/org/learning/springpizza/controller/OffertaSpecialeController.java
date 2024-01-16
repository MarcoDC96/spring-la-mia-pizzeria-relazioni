package org.learning.springpizza.controller;

import org.learning.springpizza.model.OffertaSpeciale;
import org.learning.springpizza.model.Pizza;
import org.learning.springpizza.repository.OffertaSpecialeRepository;
import org.learning.springpizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/offerta")
public class OffertaSpecialeController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OffertaSpecialeRepository offertaSpecialeRepository;

    @GetMapping("/create")
    public String create(@RequestParam(name="pizzaId", required = true) Integer pizzaId, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
        if (result.isPresent()) {
            Pizza pizzaOffertaSpeciale = result.get();
            model.addAttribute("pizza", pizzaOffertaSpeciale);
            OffertaSpeciale offertaSpeciale = new OffertaSpeciale();
            offertaSpeciale.setPizza(pizzaOffertaSpeciale);
            model.addAttribute("offertaspeciale", new OffertaSpeciale());
            return "offerte/create";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza with id: " + pizzaId + " not Found");
        }
    }
    @PostMapping("/create")
    public String store(OffertaSpeciale formOfferta){
        OffertaSpeciale offerteAttive = offertaSpecialeRepository.save(formOfferta);
        return "redirect:/pizzas/show/" + offerteAttive.getPizza().getId();
    }
}
