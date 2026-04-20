package com.arielzarate.Spring_thymeleaf_Mvc.controller;

import com.arielzarate.Spring_thymeleaf_Mvc.dto.ClientDTO;
import com.arielzarate.Spring_thymeleaf_Mvc.service.ClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    // Listar todos
    @GetMapping
    public String getClients(Model model) {
        log.info("request get clients");
        model.addAttribute("clients", clientService.findAll());
        log.info("response get clients");
        return "index";
    }

    // Formulario nuevo
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("request show create form");
        model.addAttribute("client", new ClientDTO());
        return "formClient";
    }

    // Guardar nuevo
    @PostMapping
    public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO) // BindingResult result, Model model
    {
        log.info("POST create client: {} {}", clientDTO.getName(), clientDTO.getLastName());
        //   if (result.hasErrors()) {
        //       return "formClient";
        //  }
        clientService.save(clientDTO);
        log.info("client created successfully");
        return "redirect:/";
    }

    // Formulario editar
    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("PUT form for id: {}", id);

        //esta logica ve en el service nunca aca
        var client = clientService.findById(id);
        if (client.isEmpty()) {
            log.warn("client not found: {}", id);
            return "redirect:/";
        }
        model.addAttribute("client", client.get());
        return "formClient";
    }

    // Actualizar
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
        return "redirect:/";
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        log.info("request delete client id: {}", id);
        clientService.deleteById(id);
        log.info("client deleted successfully");
        return "redirect:/";
    }

}