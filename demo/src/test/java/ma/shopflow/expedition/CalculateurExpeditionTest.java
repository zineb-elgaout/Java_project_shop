package ma.shopflow.expedition;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
 
import java.math.BigDecimal;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
 
@DisplayName("Frais d'expédition")
class CalculateurExpeditionTest {
 
    private final CalculateurExpedition calculateur = new CalculateurExpedition();
 
    @ParameterizedTest(name = "E1 : {0} -> {1} DH")
    @CsvSource({"Casablanca, 25.00", "Rabat, 25.00", "Kénitra, 25.00", "Agadir, 40.00", "Oujda, 40.00"})
    void tarifDeBaseSelonLaZone(String ville, BigDecimal attendu) {
        assertThat(calculateur.frais(ville, 1.0, 
false)).isEqualByComparingTo(attendu);
 }
 
    @ParameterizedTest(name = "E2 : {0} kg -> {1} DH")
    @CsvSource({"2.0, 25.00", "2.5, 30.00", "3.0, 30.00", "4.2, 40.00"})
    void supplementParKiloEntame(double poidsKg, BigDecimal attendu) {
        assertThat(calculateur.frais("Rabat", poidsKg, 
false)).isEqualByComparingTo(attendu);
    }
 
    @Test
    @DisplayName("E3 : l'express majore le tarif de 50 %")
    void expressMajoreDeMoitie() {
        assertThat(calculateur.frais("Agadir", 1.0, 
true)).isEqualByComparingTo("60.00");
    }
 
    @ParameterizedTest(name = "E4 : {0} kg est refusé")
    @CsvSource({"0", "-1", "30.5"})
    void poidsInvalideRefuse(double poidsKg) {
        assertThatThrownBy(() -> calculateur.frais("Rabat", poidsKg, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Poids invalide");
    }
 
    @ParameterizedTest(name = "E5 : \"{0}\" est une ville locale")
    @CsvSource({"' Rabat '", "rabat", "KÉNITRA"})
    void villeSaisieParLeClient(String ville) {
        assertThat(calculateur.frais(ville, 1.0, 
false)).isEqualByComparingTo("25.00");
    }
 
    @Test
    @DisplayName("une ville vide est refusée")
    void villeObligatoire() {
        assertThatThrownBy(() -> calculateur.frais("  ", 1.0, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La ville est obligatoire");
    }
}
