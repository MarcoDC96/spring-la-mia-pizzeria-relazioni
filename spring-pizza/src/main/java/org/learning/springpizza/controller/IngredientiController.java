package org.learning.springpizza.controller;


import jakarta.validation.Valid;
import org.learning.springpizza.model.Ingredienti;
import org.learning.springpizza.repository.IngredientiRepository;
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
@RequestMapping("/ingredienti")
public class IngredientiController {
    @Autowired
    private IngredientiRepository ingredientiRepository;
    @GetMapping public String listaIngredienti(Model model){
        List<Ingredienti> ingredientiList = ingredientiRepository.findAll();
        model.addAttribute("ingredienti", ingredientiList);
        return "ingredienti/list";

    }
    @GetMapping("/create")
    public String ingredientiCreate(Model model) {
        Ingredienti ingredienti = new Ingredienti();
        model.addAttribute("ingredienti", ingredienti);
        return "ingredienti/create";
    }

    @PostMapping("/create")
    public String ingredientis(@Valid @ModelAttribute("ingredienti") Ingredienti formIngredienti, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredienti", ingredientiRepository.findAll());
            return "ingredienti/create";
        }
        Ingredienti ingredientiSalvati = ingredientiRepository.save(formIngredienti);
        return "redirect:/ingredienti";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Ingredienti> result = ingredientiRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("ingredienti", result.get());

            return "ingredienti/create";

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ingrediente with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("ingredienti") Ingredienti formIngrediente, BindingResult bindingResult) {
        Optional<Ingredienti> result = ingredientiRepository.findById(id);
        if (result.isPresent()) {
            Ingredienti ingredienteDaEditare = result.get();
            if (bindingResult.hasErrors()) {
                return "ingredienti/create";
            }
            Ingredienti savedIngrediente = ingredientiRepository.save(formIngrediente);
            return "redirect:/ingredienti";

        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingrediente with id " + id + " not found");
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Ingredienti> result = ingredientiRepository.findById(id);
        if (result.isPresent()) {
            Ingredienti ingredientToDelete = result.get();
            ingredientiRepository.delete(ingredientToDelete);
            redirectAttributes.addFlashAttribute("redirectMessage", "ingredienti" + ingredientToDelete.getName() + " deleted");
            return "redirect:/ingredienti";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredienti with id " + id + " not found");
        }

    }
}
