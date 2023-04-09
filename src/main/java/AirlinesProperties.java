import java.io.IOException;
import java.util.Properties;

public class AirlinesProperties {
    private static final Properties props;

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    static {
        try (var fis = AirlinesProperties.class.getClassLoader()
                .getResourceAsStream("airlinesManagerConfiguration.properties")) {
            props = new Properties();
            props.load(fis);
            String airlinesWebsiteBaseURL = props.getProperty("airlines-manager-base-url");
            System.out.println("=== airlinesWebsiteBaseURL = " + airlinesWebsiteBaseURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
