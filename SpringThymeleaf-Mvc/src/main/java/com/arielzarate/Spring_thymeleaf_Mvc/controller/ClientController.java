package com.arielzarate.Spring_thymeleaf_Mvc.controller;

import com.arielzarate.Spring_thymeleaf_Mvc.dto.ClientDTO;
import com.arielzarate.Spring_thymeleaf_Mvc.service.ClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequestMapping("/client")
@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // List and search
    @GetMapping
    public String getClients(Model model) {
        log.info("request get clients");
        model.addAttribute("clients", clientService.findAll());
        log.info("response get clients");
        return "index";
    }

    // form create
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("request show create form");

        //necesita mandar un objeto vacio para que thymeleaf pueda bindear los campos del formulario
        //cuando el formulario se envie, thymeleaf va a crear un objeto client con los datos del formulario y lo va a mandar al metodo create
        model.addAttribute("client", new ClientDTO());
        return "formClient";
    }

    // save new
    @PostMapping                              //recibe un objeto client de tipo ClientDTO con los datos del formulario, el @ModelAttribute("client") indica que el objeto se llama "client" y thymeleaf lo va a bindear con los campos del formulario
    public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO, Errors errors) // BindingResult result, Model model
    {
        log.info("POST create client: {} {}", clientDTO.getName(), clientDTO.getLastName());
        if(errors.hasErrors()) {
            log.warn("validation errors: {}", errors.getAllErrors());
            return "formClient";
        }
        clientService.save(clientDTO);
        log.info("client created successfully");
        return "redirect:/client";
    }

    // Form edit
    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("PUT form for id: {}", id);

        //esta logica ve en el service nunca aca
        var client = clientService.findById(id);
        if (client.isEmpty()) {
            log.warn("client not found: {}", id);
            return "redirect:/client";
        }
        model.addAttribute("client", client.get());
        return "formClient";
    }

    // Update
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("client") ClientDTO clientDTO) //BindingResult result, Model model
    {
        log.info("request update client id: {}", id);
//        if (result.hasErrors()) {
//            return "formClient";
//        }
        //clientDTO.setId(id);
        clientService.save(clientDTO);
        log.info("client updated successfully");
        return "redirect:/client";
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        log.info("request delete client id: {}", id);
        clientService.deleteById(id);
        log.info("client deleted successfully");
        return "redirect:/client";
    }

}