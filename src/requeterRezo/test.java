package requeterRezo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 *
 * @author jimmy.benoits
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, MalformedURLException, InterruptedException {
         RequeterRezo rezo = new RequeterRezo();       
         Mot mot = rezo.requete("cocotier");
         System.out.println(mot);
         //rezo.sauvegarder();  
             
         
    }
   
}
