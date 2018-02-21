package bank;

import bank.commons.BasicAuthentication;
import bank.commons.DatastoreHandler;
import bank.mappers.EmptyRequestsMapper;
import bank.mappers.JsonMappingExceptionMapper;
import bank.mappers.JsonParsingExceptionMapper;
import bank.mappers.UnrecognizedPropertyExceptionMapper;
import bank.services.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.jaxws.JaxwsHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Main class of the server
 */
public class GrizzlyServer {

    /**
     * Main server method
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DatastoreHandler.getInstance().initializeDataStore();

        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        ResourceConfig config = new ResourceConfig().packages("bank.resources");

        config.register(JacksonJaxbJsonProvider.class);
        config.register(BasicAuthentication.class);
        config.register(UnrecognizedPropertyExceptionMapper.class);
        config.register(JsonMappingExceptionMapper.class);
        config.register(JsonParsingExceptionMapper.class);
        config.register(EmptyRequestsMapper.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);

        NetworkListener networkListener = new NetworkListener("jaxws-listener", "0.0.0.0", 8002);
        server.getServerConfiguration().addHttpHandler(new JaxwsHandler(new AuthorizationSvc()), "/auth");
        server.getServerConfiguration().addHttpHandler(new JaxwsHandler(new GetAccountsSvc()), "/getAccounts");
        server.getServerConfiguration().addHttpHandler(new JaxwsHandler(new AddAccountSvc()), "/addAccount");
        server.getServerConfiguration().addHttpHandler(new JaxwsHandler(new DeleteAccountSvc()), "/deleteAccount");
        server.getServerConfiguration().addHttpHandler(new JaxwsHandler(new BankOperationSvc()), "/bankOperation");

        server.addListener(networkListener);
        server.start();
    }

    //number - account - do niczego nie przydatny
    //wscontext - czy na pewno używać
    //ObjectId - czy używać
    //authUtil
    //parsowanie exceptionów
    //transfer internal/external
    //adnotacje

}
