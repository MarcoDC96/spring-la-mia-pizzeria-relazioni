package org.learning.springpizza.controller;

import jakarta.validation.Valid;
import org.learning.springpizza.model.OffertaSpeciale;
import org.learning.springpizza.model.Pizza;
import org.learning.springpizza.repository.OffertaSpecialeRepository;
import org.learning.springpizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            model.addAttribute("offerta", offertaSpeciale);
            return "offerte/create";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza with id: " + pizzaId + " not Found");
        }
    }
    @PostMapping("/create")
    public String store(OffertaSpeciale formOfferta){
        OffertaSpeciale offerteAttive = offertaSpecialeRepository.save(formOfferta);
        return "redirect:/pizza/show/" + offerteAttive.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        // recupero il Borrowing con quell'id da database
        Optional<OffertaSpeciale> result = offertaSpecialeRepository.findById(id);
        // se è presente precarico il form con il Borrowing
        if (result.isPresent()) {
            model.addAttribute("offertaspeciale", result.get());
            return "offerte/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offerta Speciale with id " + id
                    + " not found");

        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("offertaspeciale") OffertaSpeciale formOfferta, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "offerte/edit";
        }
        OffertaSpeciale upadatedOfferta = offertaSpecialeRepository.save(formOfferta);
        return "redirect:/pizza/show/" + upadatedOfferta.getPizza().getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<OffertaSpeciale> result = offertaSpecialeRepository.findById(id);
        if (result.isPresent()) {
            OffertaSpeciale offertaDelete = result.get();
            offertaSpecialeRepository.delete(offertaDelete);
            return "redirect:/pizza/show/" + offertaDelete.getPizza().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Offerta with id " + id + " not found");
        }

    }
}
