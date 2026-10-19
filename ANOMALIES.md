
# Fiche d'anomalies — TP1 : Tests unitaires avec JUnit 5

**Projet :** ShopFlow  

---

## Anomalie 1 — La livraison n'est pas offerte à partir de 500 DH

### 1. Règle concernée

**R5 — Frais de livraison**

La livraison coûte 30 DH lorsque le sous-total est inférieur à 500 DH. Elle est offerte dès que le sous-total atteint ou dépasse 500 DH. Un panier vide ne paie pas de livraison.

### 2. Test qui révèle l'anomalie

**Classe :** `PanierTest`  
**Groupe :** `R5 : frais de livraison`  
**Méthode :** `laLivraisonEstOfferteAPartirDe500DH`

Le test paramétré vérifie plusieurs montants, notamment :

- 499,99 DH : livraison de 30 DH attendue.
- 500,00 DH : livraison gratuite attendue.
- 500,01 DH : livraison gratuite attendue.

Le test échoue pour un sous-total de 500,00 DH, car le panier facture encore 30 DH de livraison.

### 3. Cause de l'anomalie

Dans la méthode `fraisLivraison()` de la classe `Panier`, la condition utilisait l'opérateur `>` au lieu de `>=`.

```java
return sousTotal().compareTo(SEUIL_LIVRAISON_OFFERTE) > 0
        ? BigDecimal.ZERO.setScale(2)
        : FRAIS_LIVRAISON;
```

Cette condition exigeait que le sous-total soit strictement supérieur à 500 DH, contrairement à la règle métier.

Il s'agit d'une erreur de type *off-by-one*, liée à une mauvaise gestion de la valeur limite.

### 4. Correctif appliqué

L'opérateur `>` a été remplacé par `>=`.

```java
return sousTotal().compareTo(SEUIL_LIVRAISON_OFFERTE) >= 0
        ? BigDecimal.ZERO.setScale(2)
        : FRAIS_LIVRAISON;
```

**Résultat :** La livraison est désormais gratuite dès que le sous-total atteint 500 DH. Le test paramétré passe pour toutes les valeurs vérifiées.

---

## Anomalie 2 — La remise promotionnelle s'applique aussi aux frais de livraison

### 1. Règle concernée

**R6 — Code promotionnel**  
**R7 — Calcul du total**

La remise promotionnelle est calculée uniquement sur le sous-total des produits. Les frais de livraison ne doivent jamais être réduits par le code promo.

La formule attendue est :

**Total = Sous-total − Remise + Frais de livraison**

### 2. Test qui révèle l'anomalie

**Classe :** `PanierTest`  
**Groupe :** `R6 et R7 : remise et total`  
**Test :** application du code promotionnel `BIENVENUE10` sur une souris à 120 DH.

Résultats attendus :

- Sous-total : 120,00 DH
- Remise de 10 % : 12,00 DH
- Frais de livraison : 30,00 DH
- Total attendu : 138,00 DH

Le code initial calculait un total de 135,00 DH, car il appliquait une remise de 10 % sur le montant de 150 DH, comprenant les frais de livraison.

### 3. Cause de l'anomalie

Dans la méthode `total()` de la classe `Panier`, la remise était calculée sur le montant comprenant déjà les frais de livraison.

```java
public BigDecimal total() {
    BigDecimal montant = sousTotal().add(fraisLivraison());
    if (codePromo != null) {
        montant = montant.subtract(
                pourcentageDe(montant, codePromo.pourcentage()));
    }
    return montant;
}
```

Cette implémentation ne respectait pas la règle métier, car elle diminuait également les frais de livraison.

### 4. Correctif appliqué

La méthode `total()` a été remplacée par une formule directe respectant la règle R7.

```java
public BigDecimal total() {
    return sousTotal().subtract(remise()).add(fraisLivraison());
}
```

**Résultat :** La remise est désormais calculée uniquement sur le sous-total des produits. Pour une souris à 120 DH avec le code `BIENVENUE10`, le total est correctement fixé à 138,00 DH.

---

## Conclusion

Les tests unitaires JUnit 5 ont permis d'identifier deux anomalies réelles dans le code métier du panier ShopFlow :

1. Une erreur de comparaison à la limite de 500 DH pour la livraison gratuite.
2. Une remise promotionnelle appliquée à tort aux frais de livraison.

Les deux anomalies ont été corrigées, puis les tests ont été relancés afin de vérifier les correctifs.

À la fin du TP1, **les 25 tests unitaires sont attendus au vert**, couvrant les huit règles métier du panier et constituant une base de tests de non-régression pour les prochains TP.


---

## Anomalie 3 — Le stock n'est pas libéré après un paiement refusé

### 1. Règle concernée

**C5 — Gestion d'un paiement refusé**

Lorsqu'un paiement est refusé, le système doit libérer le stock réservé pour la commande avant de lever une exception `PaiementRefuseException`.

Aucune commande ne doit être enregistrée et aucune notification ne doit être envoyée si le paiement échoue.

### 2. Test qui révèle l'anomalie

**Classe de test :** Classe de test du service de commande  
**Méthode :** `unPaiementRefuseLibereLeStock()`

Le test utilise Mockito pour simuler les dépendances du service :

- Le stock est disponible pour le produit `SOUR-02`, en quantité de 2 unités.
- Le paiement est refusé avec le motif « Solde insuffisant ».

Le test vérifie ensuite trois comportements :

- Une exception `PaiementRefuseException` est levée avec le motif du refus.
- La méthode `stock.liberer("SOUR-02", 2)` est appelée.
- Aucune interaction n'a lieu avec les services `commandes` et `notifications`.

Le test échoue parce que la méthode `stock.liberer("SOUR-02", 2)` n'est jamais appelée, même si l'exception attendue est correctement levée.

### 3. Cause de l'anomalie

Dans le service de commande, lorsque le paiement était refusé, l'exception `PaiementRefuseException` était levée immédiatement, sans libérer le stock précédemment réservé.

Cette omission provoquait le blocage inutile des produits réservés. À terme, le stock disponible pouvait être incorrectement considéré comme épuisé, empêchant d'autres clients de commander les produits concernés.

Cette anomalie n'aurait pas été détectée par un test vérifiant uniquement la levée de l'exception. La vérification des interactions avec Mockito a permis d'identifier l'appel manquant.

### 4. Correctif appliqué

Le service a été corrigé afin de libérer les quantités réservées avant de lever l'exception.

```java
if (!resultat.accepte()) {
    for (LignePanier ligne : panier.lignes()) {
        stock.liberer(
                ligne.produit().reference(),
                ligne.quantite()
        );
    }
    throw new PaiementRefuseException(resultat.motifRefus());
}
```

**Résultat :** Le stock est libéré lorsque le paiement est refusé. Aucune commande n'est enregistrée et aucune notification n'est envoyée. Après correction, les 30 tests passent avec succès.
