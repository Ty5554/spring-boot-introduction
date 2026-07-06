package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.ItemEntity;
import com.example.todo.service.accounting.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public String list(Model model) {
        var items = itemService.findAll().stream().map(ItemDTO::fromEntity).toList();
        model.addAttribute("items", items);
        return "items/list";
    }

    @GetMapping("/creationForm")
    public String creationForm(@ModelAttribute("itemForm") ItemForm form) {
        return "items/form";
    }

    @PostMapping
    public String create(@ModelAttribute("itemForm") @Validated ItemForm form,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "items/form";
        }
        itemService.create(form.toEntity());
        return "redirect:/items";
    }

    @GetMapping("/{id}/editForm")
    public String editForm(@PathVariable long id, Model model) {
        var entity = itemService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("itemForm", ItemForm.fromEntity(entity));
        model.addAttribute("itemId", id);
        return "items/editForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable long id,
                         @ModelAttribute("itemForm") @Validated ItemForm form,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("itemId", id);
            return "items/editForm";
        }
        itemService.update(form.toEntity(id));
        return "redirect:/items";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        itemService.delete(id);
        return "redirect:/items";
    }
}
