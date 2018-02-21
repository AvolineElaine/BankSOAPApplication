package bank.commons;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
/**
 * Class that executes mapping between bank number and IP address
 */
public class IpMapper {
    private static IpMapper Instance = new IpMapper();
    private Map<String, String> bankToIpMap;

    public void initialize() throws IOException, URISyntaxException {
        bankToIpMap = new HashMap<>();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Files.lines(Paths.get(classloader.getResource(Constants.bankMapFileName).toURI())).forEach(line -> {
             String[] splitLine = line.split("=");
             bankToIpMap.put(splitLine[0], splitLine[1]);
         });
    }

    /**
     * Gets instance of mapping class
     * @return instance the class
     */
    public static IpMapper getInstance() {
        return Instance;
    }


    /**
     * Gets map between bank number and IP
     * @return map between bank number and IP
     */
    public Map<String, String> getBankToIpMap() {
        return bankToIpMap;
    }
}
