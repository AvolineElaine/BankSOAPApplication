package bank.commons;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Class that handles HTTP Basic authentication protocol
 */

public class BasicAuthentication implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String auth = containerRequestContext.getHeaderString("Authorization");
        if (auth == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"missing bank authentication data\"}").build());
        }

        String[] credentials = Base64.decodeAsString(auth.substring(6)).split(":");
        if (credentials.length < 2) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"missing bank login or password\"}").build());
        }
        if (!credentials[1].equals(Constants.BANK_PASSWORD)) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"wrong bank password\"}").build());
        }
        if (!credentials[0].equals(Constants.BANK_USERNAME)) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"wrong bank login\"}").build());
        }
    }
}
