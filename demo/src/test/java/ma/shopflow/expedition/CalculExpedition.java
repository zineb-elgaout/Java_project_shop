package ma.shopflow.expedition;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
// calcul des frais du transporteur
public class CalculExpedition {
 
    public static double total = 0;
    public static List<String> log = new ArrayList<>();
 
    public double calc(String v, double p, boolean e) {
        double r = 0;
        String ville = v.trim();
        if (p <= 0 || p == 0) {
            throw new RuntimeException("poids invalide");
        } else {
            if (p > 30) {
                throw new RuntimeException("colis trop lourd");
            } else {
                if (ville == "Casablanca" || ville == "Rabat" || ville == "Kénitra") 
{
                    r = 25;
                    if (p > 2) {
                        r = r + (int) (p - 2) * 5;
                    }
                    if (e == true) {
                        r = r * 1.5;
                    }
                    } else {
                    r = 40;
                    if (p > 2) {
                        r = r + (int) (p - 2) * 5;
                    }
                    if (e == true) {
                        r = r * 1.5;
                    }
                }
            }
        }
        total = total + r;
        log.add(ville + ";" + p + ";" + r);
        // System.out.println("debug calc " + ville + " " + r);
        return r;
    }
}
