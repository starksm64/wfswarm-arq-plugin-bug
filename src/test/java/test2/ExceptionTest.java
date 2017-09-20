package test2;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExceptionTest extends Arquillian {

    private @Inject
    BadBean bean;

    @Deployment
    public static WebArchive deploy() {
        //System.setProperty("swarm.resolver.offline", "true");
        //System.setProperty("swarm.debug.port", "8888");
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "exTest.jar")
                .addClass(BadBean.class)
                .addClass(ExceptionTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                ;

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "exTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void testInteger() {
        int testValue = bean.ok();
        System.out.printf("+++++ testInteger, testValue=%d\n", testValue);
        Assert.assertEquals(1, testValue);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInteger_Broken() {
        bean.causeIllegalArgumentException();
    }
}
