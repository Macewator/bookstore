package com.bookstore.service;

import com.bookstore.model.*;
import com.bookstore.repository.*;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bookstore.util.Action.*;

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

    public List<Long> getAllBookIsbnFromFavorites() {
        return favoritesRepository.findAll().stream()
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

    public String redirectForEntityType(Type entityType, Long isbn, Long entity_id, String entity_url, String status, int page) {
        switch (entityType) {
            case TYPE_BOOK:
                return "redirect:/books/details?isbn=" + isbn;
            case TYPE_AUTHOR:
                return "redirect:/authors/books?entity_id=" + entity_id + "&page=" + page;
            case TYPE_PUBLISHER:
                return "redirect:/publishers/books?entity_id=" + entity_id + "&page=" + page;
            case TYPE_STATUS:
                if(entity_id != 999){
                    return "redirect:/" + entity_url + "/books/" + status + "?page=" + page;
                }else {
                    return "redirect:/books/filter/" + status + "?page=" + page;
                }
            default:
                return "redirect:/books?page=" + page;
        }
    }

    public String redirectForAction(
            String action, Long entity_id, String type, String option, String value, String category, String status, Boolean search, String sort_type, int page) {
        if(entity_id != 999){
            if (search){
                return "redirect:/authors/books/sort?entity_id=" + entity_id + "&type=" + type + "&option=" + option + "&status=" + status + "&search=" + search + "&sort_type=" + sort_type + "&page=" + page;
            } else if (action.equals(ACTION_SORT_BOOKS.getAction())) {
                return "redirect:/authors/books/sort?entity_id=" + entity_id + "&type=" + type + "&option=" + option + "&status=" + status + "&page=" + page;
            } else if (action.equals(ACTION_SEARCH_BOOKS.getAction())) {
                return "redirect:/authors/books/search?entity_id=" + entity_id + "&value=" + value + "&category=" + category + "&status=" + status + "&page=" + page;
            }
        }else {
            if (search) {
                return "redirect:/books/sort?type=" + type + "&option=" + option + "&status=" + status + "&search=" + search + "&sort_type=" + sort_type + "&page=" + page;
            } else if (action.equals(ACTION_SORT_BOOKS.getAction())) {
                return "redirect:/books/sort?type=" + type + "&option=" + option + "&status=" + status + "&page=" + page;
            } else if (action.equals(ACTION_SEARCH_BOOKS.getAction())) {
                return "redirect:/books/search?value=" + value + "&category=" + category + "&status=" + status + "&page=" + page;
            }
        }
        return "redirect:/books?page=" + page;
    }

}
