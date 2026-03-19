package com.fake_store_login.infraestructure.rest;


import com.fake_store_login.domain.model.Client;
import com.fake_store_login.domain.ports.in.ClientService;
import com.fake_store_login.infraestructure.rest.dto.request.ClientRequest;
import com.fake_store_login.infraestructure.rest.dto.response.ClientResponse;
import com.fake_store_login.infraestructure.rest.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientMapper clientMapper;
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest request) {
        log.info("Request POST/client - create client with body : {}", request);
        Client createdClient = clientService.create(clientMapper.mapToDomain(request));
        ClientResponse response = clientMapper.mapToResponse(createdClient);
        log.info("Response POST/client {}", response);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        log.info("Request GET/client -  get all clients");
        List<ClientResponse> list = clientMapper.mapToResponseList(clientService.getAll());
        log.info("Response GET/client -  get all clients {}", list);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long clientId) {
        log.info("Request GET/client/{} -  get client by id", clientId);
        Client client = clientService.getById(clientId);
        ClientResponse response = clientMapper.mapToResponse(client);
        log.info("Response GET/client/{} -  get client by id {}", clientId, response);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long clientId, @RequestBody ClientRequest request) {
        log.info("Request PUT/client/{} -  update client with body : {}", clientId, request);
        Client client = clientMapper.mapToDomain(request);
        Client updatedClient = clientService.update(client, clientId);
        ClientResponse response = clientMapper.mapToResponse(updatedClient);
        log.info("Response PUT/client/{} -  update client {}", clientId, response);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        log.info("Request DELETE/client/{} -  delete client", clientId);
        clientService.delete(clientId);
        log.info("Response DELETE/client/{} -  deleted", clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{clientId}/activate")
    public ResponseEntity<ClientResponse> activateClient(@PathVariable Long clientId) {
        log.info("Request PATCH/client/{}/activate -  activate client", clientId);
        clientService.activate(clientId);
        log.info("Response PATCH/client/{}/activate", clientId);
        return ResponseEntity.ok().build();
    }


}
