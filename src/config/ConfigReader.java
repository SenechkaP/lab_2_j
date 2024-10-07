package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties = new Properties();

    public ConfigReader(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public ConfigReader() throws IOException {
        try (FileInputStream fis = new FileInputStream("src/config/config.ini")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
