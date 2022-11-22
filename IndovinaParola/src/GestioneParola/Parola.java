package GestioneParola;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parola {
    
    String parola;
    
    public Parola() {
        parola = scegliParola();
    }

    
    public String getParola(){
        return parola;
    }
    
    
    
    private static String scegliParola() {
        ArrayList<String> list = new ArrayList<>();
        File file = new File("src/" + "parole" + ".txt");

        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                list.add(s.nextLine());
            }
            s.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parola.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        String scelta= list.get((int)(Math.random() * list.size()));
        return scelta;
    }
}
