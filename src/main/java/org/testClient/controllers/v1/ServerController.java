package org.testClient.controllers.v1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.testClient.model.server.OxServerRequest;
import org.testClient.model.server.OxServerResponse;
import org.testClient.services.ClientService;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/v1/client")
public class ServerController {
    private static final Logger LOG = LoggerFactory.getLogger(ServerController.class);

    private final ClientService clientService;

    @Autowired
    public ServerController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value ="/register", produces = "application/json")
    public OxServerResponse postIdentity() {
        LOG.debug("registering ......");
        OxServerRequest oxServerRequest = new OxServerRequest();
        OxServerResponse oxServerResponse = clientService.registerToServer(oxServerRequest);
        return oxServerResponse;
    }


    @PostMapping(value = "/token", produces = "application/json")
    public OxServerResponse post() {
        OxServerRequest oxServerRequest = new OxServerRequest();
        OxServerResponse oxServerResponse = clientService.getTokenFromServer(oxServerRequest);
        return oxServerResponse;
    }
}
