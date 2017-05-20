package softwarearchs.test.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import softwarearchs.storage.Repository;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import static org.junit.Assert.*;

/**
 * Created by arseny on 25.04.17.
 */
public class RepositoryTest {
    Repository repository;
    @Before
    public void setUp() throws Exception {
        repository = new Repository();
    }

    @After
    public void tearDown() throws Exception {
        repository.clear();
        repository = null;
    }

    @Test
    public void addUser() throws Exception {
        assertTrue("Failed user adding", repository.addUser(
                new Receiver("name", "surname",
                        "patronymic", "login"), "pass"));
        assertNotNull("User not found", repository.findUser(
                "login"));

        assertFalse("User already exists", repository.addUser(
                new Receiver("name", "surname",
                        "patronymic", "login"), "pass"));
        assertTrue("Failed user adding", repository.addUser(
                new Receiver("name1", "surname1",
                        "patronymic1", "login1"), "pass"));

        assertNotNull("User not found", repository.findUser("login"));
        assertNotNull("User not found", repository.findUser("login1"));
    }

    @Test
    public void signIn() throws Exception{
        assertTrue("Failed user adding", repository.addUser(
                new Receiver("name", "surname",
                        "patronymic", "login"), "pass"));

        User user = repository.findUser("login");
        assertNotNull("Added user not found", user);

        assertFalse("Wrong password", repository.signIn(user.getLogin(), "pass2"));
        assertTrue("???", repository.signIn(user.getLogin(), "pwd") );
    }
}
