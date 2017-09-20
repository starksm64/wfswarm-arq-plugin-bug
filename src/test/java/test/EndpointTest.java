package test;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.inject.spi.DefinitionException;
import javax.enterprise.inject.spi.Extension;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

public class EndpointTest extends Arquillian {
    /**
     * The base URL for the container under test
     */
    @ArquillianResource
    private URL baseURL;

    @Deployment(testable=true)
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive createDeployment() throws IOException {
        //System.setProperty("swarm.resolver.offline", "true");
        //System.setProperty("swarm.debug.port", "8888");
        WebArchive webArchive = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClass(TestEndpoint.class)
                .addClass(TestQualifier.class)
                .addAsServiceProviderAndClasses(Extension.class, TestExtension.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.printf("WebArchive: %s\n", webArchive.toString(true));
        return webArchive;
    }

    @RunAsClient
    @Test()
    public void verifyIssuerClaim() throws Exception {
        String uri = baseURL.toExternalForm() + "/endp/dummy";
        WebTarget echoEndpointTarget = ClientBuilder.newClient()
                .target(uri)
            ;
        Response response = echoEndpointTarget.request(MediaType.APPLICATION_JSON).get();
        String replyString = response.readEntity(String.class);
    }
}
