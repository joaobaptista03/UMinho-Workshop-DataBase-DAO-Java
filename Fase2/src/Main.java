package src;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import src.ui.*;
import src.business.*;
import src.data.OficinaDAO;

public class Main {
    public static void main(String[] args) throws ParseException {
        try {
            OficinaDAO oficinaDAO = new OficinaDAO();
            AuthTypeID auth = AuthenticationUI.authenticate(oficinaDAO);

            if (auth == null) {
                System.out.println("Autenticação falhada. A sair...");
                return;
            }

            System.out.println("Bem vindo/a à Oficina!\n");
            
            switch (auth.authType) {
                case 1:
                    new ClienteUI(oficinaDAO).execute();
                    break;
                case 2:
                    new FuncionarioUI(oficinaDAO, auth.id).execute();
                    break;
                case 3:
                    new AdministradorUI(oficinaDAO, auth.id).execute();
                    break;
                default:
                    System.out.println("Cargo inválido. A sair...");
            }

        } catch (IOException e) {
            System.out.println("Erro a iniciar a aplicação.: " + e.getMessage());
        }
    }
}
