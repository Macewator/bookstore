package com.bookstore.service;

import com.bookstore.model.Client;
import com.bookstore.model.ClientRole;
import com.bookstore.repository.ClientRepository;
import com.bookstore.repository.ClientRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private static final String DEFAULT_ROLE = "ROLE_CLIENT";
    private ClientRepository clientRepository;
    private ClientRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setRoleRepository(ClientRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addWithDefaultRole(Client client) {
        ClientRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        client.getClientRoles().add(defaultRole);
        String passwordHash = passwordEncoder.encode(client.getPassword());
        client.setPassword(passwordHash);
        clientRepository.save(client);
    }
}
