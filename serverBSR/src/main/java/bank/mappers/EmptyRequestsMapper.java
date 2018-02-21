package bank.mappers;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


/**
 * Mapper filtering empty-body JSONs
 */
@PreMatching
public class EmptyRequestsMapper implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (!containerRequestContext.hasEntity()) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Payload is null\"}")
                    .type(MediaType.APPLICATION_JSON_TYPE).build());
        }
    }
}
