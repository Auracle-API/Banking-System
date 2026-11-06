package Application.Controller;

import org.springframework.context.annotation.Configuration;
import service.BankRepositoryException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DBConfig {
    private static final Properties props = new  Properties();

    static {
        try (InputStream in = DBConfig.class.getClassLoader().getResourceAsStream("application.properties")){
                if (in == null){
                    throw new BankRepositoryException("application.properties not found on class path.");
                }
                props.load(in);
        } catch (IOException e){
            throw new BankRepositoryException("properties file not found");
        }
    }

    public static String getUrl() {return props.getProperty("spring.datasource.url");}
    public static String getUsername() {return props.getProperty("spring.datasource.username");}
    public static String getPassword() {return props.getProperty("spring.datasource.password");}
}
