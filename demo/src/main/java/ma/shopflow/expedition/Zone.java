package ma.shopflow.expedition;
 
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;
 
/**
 * Zone tarifaire du transporteur (règles E1 et E5).
 */
public enum Zone {
 
    LOCALE(new BigDecimal("25.00")),
    NATIONALE(new BigDecimal("40.00"));
 
    private static final Set<String> VILLES_LOCALES = Set.of("casablanca", "rabat", 
"kénitra");
 
    private final BigDecimal tarifDeBase;
 
    Zone(BigDecimal tarifDeBase) {
        this.tarifDeBase = tarifDeBase;
    }
 
    public BigDecimal tarifDeBase() {
        return tarifDeBase;
    }
 
    /**
     * Zone d'une ville saisie par le client : les espaces autour et la casse ne 
comptent pas.
     */
    public static Zone de(String ville) {
        if (ville == null || ville.isBlank()) {
            throw new IllegalArgumentException("La ville est obligatoire");
        }
        String villeNormalisee = ville.trim().toLowerCase(Locale.ROOT);
        return VILLES_LOCALES.contains(villeNormalisee) ? LOCALE : NATIONALE;
    }
}
