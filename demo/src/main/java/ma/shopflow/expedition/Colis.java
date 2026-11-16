package ma.shopflow.expedition;
 
/**
 * Un colis à remettre au transporteur.
 */
public record Colis(String ville, double poidsKg, boolean express) {
}
