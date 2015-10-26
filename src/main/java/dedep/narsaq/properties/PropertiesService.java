package dedep.narsaq.properties;

//todo: return Optional
public interface PropertiesService {
    String get(String key);
    Integer getInt(String key);
    Double getDouble(String key);
}
