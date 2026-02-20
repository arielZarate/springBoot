package com.arielzarate.infraestructure.rest;


import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.in.ClientService;
import com.arielzarate.infraestructure.rest.dto.ClientRequest;
import com.arielzarate.infraestructure.rest.dto.ClientResponse;
import com.arielzarate.infraestructure.rest.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        log.info("Request POST/client -  create client with body : {}", request);
        Client client = clientMapper.mapToDomainWithAddresses(request);
        Client createdClient = clientService.createClient(client);
        ClientResponse response = clientMapper.mapToClientResponse(createdClient);
        log.info("Response POST/client {}", response);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        log.info("Request GET/client -  get all clients");
        List<ClientResponse> list = clientMapper.mapToClientResponseList(clientService.getAllClients());
        log.info("Response GET/client -  get all clients {}", list);
        return ResponseEntity.ok().body(list);
    }

}
