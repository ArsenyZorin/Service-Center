package softwarearchs.tests.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import softwarearchs.exceptions.InvalidSignIn;
import softwarearchs.exceptions.InvalidUser;
import softwarearchs.storage.MapperRepository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import static org.junit.Assert.*;
/**
 * Created by arseny on 19.06.17.
 */
public class StorageTest {
    MapperRepository repos;

    @Before
    public void setUp() throws Exception{
        repos = new MapperRepository();
    }

    @Test
    public void addUser() throws Exception{
        try{
            repos.addUser(new Receiver("testReceiver", "testReceiver",
                    "testReceiver", "testReceiver"),"123");
            assertTrue("User was added", true);
        }catch (Exception e){
            assertTrue(e.toString(), false);
        }
        assertNotNull("User not found", repos.findUser("testReceiver"));

        try{
            repos.addUser(new Master("testReceiver", "testReceiver",
                    "testReceiver", "testReceiver"), "123");
            assertTrue("User was added", false);
        } catch(Exception e){
            assertTrue(e.toString(), true);
        }

        try{
            repos.addUser(new Client("testClient", "testClient",
                    "testClient", "testClient"), "123");
            assertTrue("User was added", true);
        }catch (Exception e){
            assertTrue(e.toString(), false);
        }
        assertNotNull("User not found", repos.findUser("testReceiver"));
        assertNotNull("User not found", repos.findUser("testClient", "testClient",
                "testClient"));
    }

    @Test
    public void updateUser() throws Exception{
        try{
            Client client = new Client("updatedName", "updatedSurname",
                    "updatedPatronymic", "testClient");
            client.setPhoneNumber("8-911-025-64-89");
            client.seteMail("clientmail@mail.com");
            repos.updateUser(client);
            assertTrue("User was updated", true);
        } catch(InvalidUser e){
            assertTrue(e.toString(), false);
        }
    }

    @Test
    public void signIn() throws Exception {
        try {
            Receiver receiver = new Receiver("testSignReceiver",
                    "testSignReceiver", "testSignReceiver",
                    "testLogin");
            repos.addUser(receiver, "123");
            assertTrue("Receiver was added", true);
        } catch (InvalidUser e) {
            assertTrue(e.toString(), false);
        }

        User user = repos.findUser("testLogin");
        assertNotNull("User not found", false);

        try {
            repos.signIn(user.getLogin(), "1234");
            assertTrue("Authentication failed", true);
        } catch (InvalidSignIn e) {
            assertTrue(e.toString(), false);
        }

        try {
            repos.signIn(user.getLogin(), "123");
            assertTrue("Authentication succeed", true);
        } catch (InvalidSignIn e) {
            assertTrue(e.toString(), false);
        }
    }

    @After
    public void drop() throws Exception{
        repos.deleteUser("testLogin");
        repos.deleteUser("testReceiver");
        repos.deleteUser("testClient");
    }
}
