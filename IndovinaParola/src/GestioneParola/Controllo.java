package GestioneParola;

/**
 *
 * @author pugliano_denis
 */
public class Controllo {
    public String cambia(Parola p) {
        String parola=p.getParola();
        int lunghezza=parola.length();
        parola.replaceAll(parola, "*");
        return parola;
    }
}
