package dedep.narsaq.properties;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class PropertiesServiceImpl implements PropertiesService {

    private final Properties properties = new Properties();

    public static final String PROPERTIES_FILE = "app.properties";

    @Override
    public String get(String key) {
        return (String) getObj(key);
    }

    @Override
    public Integer getInt(String key) {
        return Integer.valueOf(get(key));
    }

    @Override
    public Double getDouble(String key) {
        return Double.valueOf(get(key));
    }

    private Object getObj(String key) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(in);
            return properties.get(key);
        } catch (IOException e) {
            throw new RuntimeException("Cannot get property for key = " + key, e);
        }
    }
}
