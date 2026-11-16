package ma.shopflow.expedition;
 
import static org.assertj.core.api.Assertions.assertThat;
 
import java.util.List;
import java.util.Random;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
@DisplayName("Export des bordereaux")
class ExportBordereauxTest {
 
    private final ExportBordereaux export = new ExportBordereaux(new CalculateurExpedition(), new Random(42));
    @Test
    @DisplayName("une ligne par colis, après l'en-tête")
    void uneLigneParColis() {
        String csv = export.exporter(List.of(new Colis(" Rabat ", 2.5, false), new Colis("Agadir", 1.0, true)));
        assertThat(csv.lines()).hasSize(3);
        assertThat(csv.lines().toList().get(0)).isEqualTo("ville;poids;frais;suivi");
        assertThat(csv.lines().toList().get(1)).startsWith("Rabat;2.5;30.00;SF");
        assertThat(csv.lines().toList().get(2)).startsWith("Agadir;1.0;60.00;SF");
    }
    @Test
    @DisplayName("le numéro de suivi a toujours six chiffres")
    void numeroDeSuiviSurSixChiffres() {
        String csv = export.exporter(List.of(new Colis("Oujda", 1.0, false)));
        assertThat(csv.lines().toList().get(1)).matches("Oujda;1\\.0;40\\.00;SF\\d{6}");
    }
}
