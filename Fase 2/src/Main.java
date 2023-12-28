package src;

import java.io.IOException;

import src.data.OficinaDAO;

public class Main {
    public static void main(String[] args) throws IOException {
        createDatabase();
    }

    private static void createDatabase() throws IOException {
        OficinaDAO oficinaDAO = new OficinaDAO();
        System.out.println("Database created successfully!");
    }
}