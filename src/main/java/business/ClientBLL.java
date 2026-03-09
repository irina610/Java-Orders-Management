package business;

import dataAccess.ClientDAO;
import dataAccess.GenericDAO;
import model.Client;
import java.util.List;
import java.util.Optional;

public class ClientBLL {
    private final GenericDAO<Client> clientDAO = new GenericDAO<>(Client.class);
    private final ClientDAO clientDAO2 = new ClientDAO();

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    public void addClient(Client client) {
        clientDAO.insert(client);
    }

    public void updateClient(Client client) {
        clientDAO.update(client, "id", client.getId());
    }

    public void deleteClient(int id) {
        clientDAO.delete("id", id);
    }

    public Optional<Client> findByEmail(String email) {
        return clientDAO2.findByEmail(email);
    }

}
