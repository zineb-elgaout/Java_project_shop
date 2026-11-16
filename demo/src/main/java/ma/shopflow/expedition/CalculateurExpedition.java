package ma.shopflow.expedition;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
 
/**
 * Calcule les frais facturés par le transporteur pour un colis (règles E1 à E5).
 */
public class CalculateurExpedition {
 
    static final double POIDS_INCLUS_KG = 2;
    static final double POIDS_MAXIMAL_KG = 30;
    static final BigDecimal SUPPLEMENT_PAR_KILO = new BigDecimal("5.00");
    static final BigDecimal MAJORATION_EXPRESS = new BigDecimal("1.5");
 
    public BigDecimal frais(String ville, double poidsKg, boolean express) {
        verifierPoids(poidsKg);
        BigDecimal frais = Zone.de(ville).tarifDeBase().add(supplementPoids(poidsKg));
        if (express) {
            frais = frais.multiply(MAJORATION_EXPRESS);
        }
        return frais.setScale(2, RoundingMode.HALF_UP);
    }
 
    private static void verifierPoids(double poidsKg) {
        if (poidsKg <= 0 || poidsKg > POIDS_MAXIMAL_KG) {
            throw new IllegalArgumentException(
                    "Poids invalide : " + poidsKg + " kg (maximum " + POIDS_MAXIMAL_KG + " kg)");
        }
    }
 
    private static BigDecimal supplementPoids(double poidsKg) {
        if (poidsKg <= POIDS_INCLUS_KG) {
            return BigDecimal.ZERO;
        }
        long kilosEntames = (long) Math.ceil(poidsKg - POIDS_INCLUS_KG);
        return SUPPLEMENT_PAR_KILO.multiply(BigDecimal.valueOf(kilosEntames));
    }
}
