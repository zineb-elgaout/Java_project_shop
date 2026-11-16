package ma.shopflow.expedition;
 
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
 
/**
 * Exporte les bordereaux des colis au format CSV attendu par le transporteur.
 */
public class ExportBordereaux {
 
    private static final String EN_TETE = "ville;poids;frais;suivi";
 
    private final CalculateurExpedition calculateur;
    private final Random generateurSuivi;
 
    public ExportBordereaux(CalculateurExpedition calculateur, Random generateurSuivi) {
        this.calculateur = calculateur;
        this.generateurSuivi = generateurSuivi;
    }
 
    public String exporter(List<Colis> colis) {
        StringBuilder csv = new StringBuilder(EN_TETE).append('\n');
        for (Colis unColis : colis) {
            BigDecimal frais = calculateur.frais(unColis.ville(), unColis.poidsKg(), unColis.express());
            csv.append(unColis.ville().trim()).append(';')
                    .append(unColis.poidsKg()).append(';')
                    .append(frais).append(';')
                    .append(numeroDeSuivi()).append('\n');
}
        return csv.toString();
    }
 
    private String numeroDeSuivi() {
        return "SF%06d".formatted(generateurSuivi.nextInt(1_000_000));
    }
}
