package src;

import java.io.IOException;

import src.data.OficinaDAO;
import src.ui.Authentication;

public class Main {
    public static void main(String[] args) throws IOException {
        OficinaDAO oficinaDAO = new OficinaDAO();

        int authType = Authentication.authenticate(oficinaDAO);

        if (authType == -1) return;

        System.out.println("Welcome to the Car Service Center!");
    }
}
