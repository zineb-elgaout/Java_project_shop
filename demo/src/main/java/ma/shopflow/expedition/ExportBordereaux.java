package ma.shopflow.expedition;
 
import java.util.List;
import java.util.Random;
 
// export des bordereaux pour le transporteur (format CSV)
public class ExportBordereaux {
 
    public String export(List<String[]> colis) {
        String s = "ville;poids;frais;suivi\n";
        for (int i = 0; i < colis.size(); i++) {
            String[] c = colis.get(i);
            try {
                double p = Double.parseDouble(c[1]);
                boolean e = c[2].equals("express");
                String ville = c[0].trim();
                double r = 0;
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
                String suivi = "SF" + new Random().nextInt(1000000);
                s = s + ville + ";" + p + ";" + r + ";" + suivi + "\n";
                System.out.println("colis exporte : " + ville);
            } catch (Exception ex) {
            }
        }
        return s;
    }
}
