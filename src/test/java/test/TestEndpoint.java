package test;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/endp")
public class TestEndpoint {
    @Inject
    @TestQualifier
    private String value;

    @GET
    @Path("/dummy")
    public String dummy() {
        return "dummy";
    }
}
