package org.testClient.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testClient.client.OxClientServer;
import org.testClient.model.server.OxServerRequest;
import org.testClient.model.server.OxServerResponse;

@Service
public class ClientService {


    private final OxClientServer oxClientServer;

    @Autowired
    public ClientService(OxClientServer oxClientServer) {
        this.oxClientServer = oxClientServer;
    }


    public OxServerResponse registerToServer(OxServerRequest oxServerRequest){
        return oxClientServer.registerUser(oxServerRequest);
    }

    public OxServerResponse getTokenFromServer(OxServerRequest oxServerRequest){
        return oxClientServer.token(oxServerRequest);
    }
}
