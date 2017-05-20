package softwarearchs.test.receipt;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by arseny on 23.04.17.
 */
@RunWith(Arquillian.class)
public class ReceiverTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createReceipt() throws Exception {
    }

    @Test
    public void updateReceipt() throws Exception {
    }

    @Test
    public void createDevice() throws Exception {
    }

    @Test
    public void updateDevice() throws Exception {
    }

    @Test
    public void createClient() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(softwarearchs.user.Receiver.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
