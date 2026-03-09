package business;

import dataAccess.BillDAO;
import model.Bill;
import java.util.List;

public class BillBLL {
    private final BillDAO billDAO = new BillDAO();

    public List<Bill> findByClientEmail(String clientEmail) {
        return billDAO.findByClientEmail(clientEmail);
    }

}
