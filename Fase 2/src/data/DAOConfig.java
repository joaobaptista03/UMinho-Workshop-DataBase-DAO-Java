package src.data;

public class DAOConfig {
    static final String USERNAME = "root";
    static final String PASSWORD = "Jopemoba@3642mysql";
    private static final String DATABASE = "OficinaDB";
    //private static final String DRIVER = "jdbc:mariadb";       // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";           // Usar para MySQL
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
}