package softwarearchs.tests.repair;

import org.junit.Before;
import org.junit.Test;
import softwarearchs.Main;
import softwarearchs.additional.BankAccount;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.repair.Invoice;
import softwarearchs.repair.Receipt;
import softwarearchs.storage.MapperRepository;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by arseny on 19.06.17.
 */
public class PaymentTest {
    MapperRepository repos;
    Receipt receipt;
    Invoice invoice;
    BankAccount account;

    @Before
    public void setUp() throws Exception{
        repos = new MapperRepository();
    }

    @Test
    public void createInvoice() throws Exception{
        try {
            receipt = repos.findAllReceipts().values().stream()
                    .filter(p -> p.getStatus().equals(ReceiptStatus.Ready_for_extr))
                    .sorted().findFirst().get();
            assertNotNull("Receipt was found",receipt);
            invoice = receipt.getReceiver().createInvoice(Main.stringFromDate(new Date()), receipt, "0.0");
            assertTrue("Invoice was created", true);
        } catch (Exception e){
            assertTrue(e.toString(), false);
        }

        try{
            account = repos.findAccount("1332");
            assertNotNull(account);
            account.payForRepair(invoice);
            assertTrue("Invoice paid", false);
        } catch (Exception e){
            assertTrue(e.toString(), true);
        }
    }
}
