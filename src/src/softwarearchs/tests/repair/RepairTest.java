package softwarearchs.tests.repair;

import org.junit.Before;
import org.junit.Test;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.repair.Device;
import softwarearchs.repair.Receipt;
import softwarearchs.storage.MapperRepository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import static org.junit.Assert.*;


/**
 * Created by arseny on 19.06.17.
 */
public class RepairTest {
    Receipt receipt;
    Receiver receiver;
    Master master;
    Client client;
    Device device;
    MapperRepository repos;

    @Before
    public void setUp() throws Exception{
        repos = new MapperRepository();
        receiver = (Receiver) User.signIn("Receiver2", "123");
        client = (Client) repos.findUser("User2");
        master = (Master)repos.findUser("Master1");
    }

    @Test
    public void createDevice() throws Exception {
        try {
            device = receiver.createDevice("serial123", "Mouse",
                    "Genius", "X5-50D", client, "2017-05-20",
                    "2018-05-20", "", "");
            receiver.addDevice(device);
            assertTrue("Device was created", true);
        } catch (Exception e) {
            assertTrue(e.toString(), false);
        }
        assertNotNull(repos.findDevice(device.getSerialNumber()));
        try {
            receipt = receiver.createReceipt("20170619191316" + receiver.getId(),
                    "2017-06-19", RepairType.Warranty.toString(), device,
                    "Broken wheel", "", ReceiptStatus.Opened.toString());
            repos.addReceipt(receipt);
            assertTrue("Receipt was created", true);
        } catch (Exception e) {
            assertTrue(e.toString(), false);
        }
        assertNotNull(repos.findReceipt(receipt.getReceiptNumber()));
        try {
            master.assignOnRepair(receipt);
            master.setRecStatus(ReceiptStatus.Under_Repair.toString(), receipt);
            assertTrue("Receipt status and master was updated", true);
        } catch (Exception e) {
            assertTrue(e.toString(), false);
        }
    }
}
