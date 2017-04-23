package softwarearchs.invoice;

import softwarearchs.storage.Repository;
import softwarearchs.user.Client;

import java.util.Date;

/**
 * Created by arseny on 09.04.17.
 */
public class BancAccount {
    private int accountNumber;
    private Client client;
    private Date validTill;
    private int cvc;

    public int getAccountNumber() {
        return accountNumber;
    }

    public Client getClient() {
        return client;
    }

    public Date getValidTill() {
        return validTill;
    }

    public int getCvc() {
        return cvc;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public boolean addBankAccount() { return (new Repository())
            .addBankAccount(this, this.client); }
}
