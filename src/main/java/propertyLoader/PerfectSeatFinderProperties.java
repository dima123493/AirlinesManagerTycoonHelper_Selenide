package propertyLoader;

import java.io.IOException;
import java.util.Properties;

public class PerfectSeatFinderProperties {
    private static final Properties props;

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    static {
        try (var fis = PerfectSeatFinderProperties.class.getClassLoader()
                .getResourceAsStream("finderWebsiteConfiguration.properties")) {
            props = new Properties();
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}