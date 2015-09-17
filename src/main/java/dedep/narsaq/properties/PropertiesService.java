package dedep.narsaq.properties;

public interface PropertiesService {
    String get(String key);
    Integer getInt(String key);
    Double getDouble(String key);
}
