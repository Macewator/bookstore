package com.bookstore.service;

import com.bookstore.model.*;
import com.bookstore.repository.*;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Type;
import com.bookstore.util.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bookstore.util.Action.*;
import static com.bookstore.util.Type.*;

@Service
public class ClientService {

    private static final String DEFAULT_ROLE = "ROLE_CLIENT";

    private ClientRepository clientRepository;
    private ClientRoleRepository roleRepository;
    private FavoritesRepository favoritesRepository;
    private BookRepository bookRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientRoleRepository roleRepository,
                         FavoritesRepository favoritesRepository, BookRepository bookRepository,
                         PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.favoritesRepository = favoritesRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addWithDefaultRole(Client client, CreditCard creditCard) {
        ClientRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        client.getClientRoles().add(defaultRole);
        String passwordHash = passwordEncoder.encode(client.getPassword());
        client.setPassword(passwordHash);
        client.setCreditCards(creditCard);
        clientRepository.save(client);
    }

    public void checkRegisterData(Client client) {
        StringBuilder message = new StringBuilder();
        message.append("uwaga:");
        Optional<String> clientUserName = clientRepository.findUserName(client.getUserName());
        Optional<String> clientEmail = clientRepository.findEmail(client.getUserInfo().getEmail());
        Optional<String> clientTelephone = clientRepository.findTelephone(client.getUserInfo().getTelephone());
        if (clientUserName.isPresent()) {
            message.append(" podana nazwa użytkownika jest zajęta,");
        }
        if (clientEmail.isPresent()) {
            message.append(" podany email jest już w bazie,");
        }
        if (clientTelephone.isPresent()) {
            message.append(" podany telefon jest już w bazie");
        }
        if (!message.toString().equals("uwaga:")) {
            throw new IllegalArgumentException(message.toString());
        }
    }

    public void deleteClientAccount(String userName) {
        clientRepository.deleteByUserName(userName);
    }

    public void deleteClientFavorites(String userName) {
        favoritesRepository.deleteAllByClient_UserName(userName);
    }

    public Client findClientByUserName(String clientName) {
        return clientRepository.findByUserName(clientName)
                .orElseThrow(() -> new ClientNotFoundException("użytkonik nie istnieje"));
    }

    public Optional<Client> findClientForLogin(String clientName) {
        return clientRepository.findByUserName(clientName);
    }

    public ClientUpdateData createClientUpdData(String userName) {
        Client client = clientRepository.findByUserName(userName)
                .orElseThrow(() -> new ClientNotFoundException("użytkonik nie istnieje"));
        ClientUpdateData clientUpdateData = new ClientUpdateData();
        clientUpdateData.setUserName(client.getUserName());
        clientUpdateData.setUserInfo(client.getUserInfo());
        clientUpdateData.setAddress(client.getAddress());
        return clientUpdateData;
    }

    public void updateClient(ClientUpdateData clientUpdData, String userName) {
        Client clientUpd = clientRepository.findByUserName(userName)
                .orElseThrow(() -> new ClientNotFoundException("użytkonik nie istnieje"));
        StringBuilder message = new StringBuilder();
        checkUpdateData(message, clientUpd, clientUpdData);
        updateClientData(clientUpd, clientUpdData);
        clientRepository.save(clientUpd);
    }

    private void updateClientData(Client clientUpd, ClientUpdateData clientUpdData) {
        clientUpd.setUserName(clientUpdData.getUserName());
        clientUpd.setUserInfo(clientUpdData.getUserInfo());
        clientUpd.setAddress(clientUpdData.getAddress());
    }

    private void checkUpdateData(StringBuilder message, Client clientUpd, ClientUpdateData clientUpdData) {
        message.append("uwaga:");
        if (!clientUpdData.getUserName().equals(clientUpd.getUserName())) {
            Optional<String> clientUserName = clientRepository.findUserName(clientUpdData.getUserName());
            if (clientUserName.isPresent()) {
                message.append(" podana nazwa użytkownika jest zajęta,");
            }
        }
        if (!clientUpdData.getUserInfo().getEmail().equals(clientUpd.getUserInfo().getEmail())) {
            Optional<String> clientEmail = clientRepository.findEmail(clientUpdData.getUserInfo().getEmail());
            if (clientEmail.isPresent()) {
                message.append(" podany email jest już w bazie,");
            }
        }
        if (!clientUpdData.getUserInfo().getTelephone().equals(clientUpd.getUserInfo().getTelephone())) {
            Optional<String> clientTelephone = clientRepository.findTelephone(clientUpdData.getUserInfo().getTelephone());
            if (clientTelephone.isPresent()) {
                message.append(" podany telefon jest już w bazie");
            }
        }
        if (!message.toString().equals("uwaga:")) {
            throw new IllegalArgumentException(message.toString());
        }
    }

    public void checkAndUpdateClientPassword(Client client, String oldPassword, String newPassword) {
        String dbPassword = client.getPassword();
        String passwordPattern = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        if (!passwordEncoder.matches(oldPassword, dbPassword)) {
            throw new IllegalArgumentException("aktualne hasło jest niepoprawne");
        } else if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("nowe hasło musi być inne od obecnego");
        } else if (!Pattern.matches(passwordPattern, newPassword)) {
            throw new IllegalArgumentException("nowe hasło jest niepoprawne");
        } else {
            String passwordHash = passwordEncoder.encode(newPassword);
            client.setPassword(passwordHash);
            clientRepository.save(client);
        }
    }

    public Address findClientAddress(String clientName) {
        return clientRepository.findClientAddress(clientName);
    }

    public CreditCard findClientCreditCard(String clientName) {
        return findClientByUserName(clientName).getCreditCards();
    }

    public void updateClientCreditCard(String userName, CreditCard creditCard) {
        Client clientUpd = findClientByUserName(userName);
        CreditCard cardUpd = clientUpd.getCreditCards();
        cardUpd.setOwnerName(creditCard.getOwnerName());
        cardUpd.setCreditCardNumber(creditCard.getCreditCardNumber());
        cardUpd.setExpireDate(creditCard.getExpireDate());
        cardUpd.setCvvNumber(creditCard.getCvvNumber());
        clientUpd.setCreditCards(cardUpd);
        clientRepository.save(clientUpd);
    }

    public void addToFavorites(Long isbn, String userName) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new NoSuchElementException("książka nie istnieje"));
        Client client = findClientByUserName(userName);
        Favorites clientFavorites = new Favorites();
        clientFavorites.setBook(book);
        clientFavorites.setClient(client);
        favoritesRepository.save(clientFavorites);
    }

    public List<Book> removeFromFavorites(Long isbn, String userName) {
        favoritesRepository.deleteAllByClient_UserNameAndBook_Isbn(userName, isbn);
        return getAllClientFavorites(userName);
    }

    public List<Book> getAllClientFavorites(String userName) {
        return favoritesRepository.findAllByClient_UserName(userName).stream()
                .map(Favorites::getBook)
                .collect(Collectors.toList());
    }

    public List<Favorites> getAllFavorites() {
        return favoritesRepository.findAll();
    }

    public List<Long> getAllBookIsbnFromFavorites(String userName) {
        return favoritesRepository.findAllByClient_UserName(userName).stream()
                .map(Favorites::getBook)
                .map(Book::getIsbn)
                .collect(Collectors.toList());
    }

    public List<Integer> pageCounter(int totalPages) {
        List<Integer> pageNumbers = null;
        if (totalPages >= 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return pageNumbers;
    }

    public String redirectForEntityType(Type entityType, Long isbn, Long entity_id, String entity_url, String status, Integer page) {
        String baseUrl = "/books";
        UriBuilder baseBuilder = new UriBuilder(baseUrl);

        String extendUrl = entity_id != null ? "/" + entity_url + baseUrl : baseUrl + "/details";
        UriBuilder extendBuilder = new UriBuilder(extendUrl);

        String redirectUrl;

        if(entityType == TYPE_BOOK){
            redirectUrl = extendBuilder.param("isbn", isbn).build();
        }else if(entityType == TYPE_AUTHOR || entityType == TYPE_PUBLISHER){
            redirectUrl = extendBuilder.param("entity_id", entity_id).param("page", page).build();
        }else if(entityType == TYPE_STATUS){
            if (entity_id != null) {
                redirectUrl = extendBuilder.param("status", status).param("page", page).build();
            } else {
                redirectUrl = baseBuilder.param("status", status).param("page", page).build();
            }
        }else {
            redirectUrl = baseBuilder.param("page", page).build();
        }

        return "redirect:" + redirectUrl;
    }

    public String redirectForAction(
            String action, Long entity_id, String entity_name, String type, String option, String value,
            String category, String status, Boolean search, String sort_type, int page) {

        String url = entity_id != null ? "/" + entity_name + "/books/" : "/books/";
        String actionUrl = action.equals(ACTION_SEARCH_BOOKS.getAction()) ? url + "search" : url + "sort";
        String builtUrl = new UriBuilder(actionUrl)
                .param("entity_id", entity_id)
                .param("type", type)
                .param("value", value)
                .param("option", option)
                .param("category", category)
                .param("status", status)
                .param("search", search)
                .param("sort_type", sort_type)
                .param("page", page)
                .build();

        return "redirect:" + builtUrl;
    }
}
