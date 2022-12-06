package GestioneParola;

/**
 *
 * @author pugliano_denis
 */
public class Controllo {
    String daIndovinare;
    public Controllo(Parola p) {
        daIndovinare=p.getParola();
    }
    
    public String cambia(String tentativo) {
        String risultato="";
        if(tentativo.equals("jolly")){
            return "Jolly";
        }
        
        if(tentativo.equals(daIndovinare)) {
            return tentativo;
        }
        if(tentativo.length()<daIndovinare.length())
            return "Parola Troppo Corta";
        
        if(tentativo.length()>daIndovinare.length())
            return "Parola Troppo Lunga";
        
       
        
        for (int i = 0; i < daIndovinare.length(); i++) {
            if(daIndovinare.charAt(i)==tentativo.charAt(i)) {
                risultato+=daIndovinare.charAt(i);
            }else {
                risultato+="*";
            }
        }
        return risultato;
    }
}
