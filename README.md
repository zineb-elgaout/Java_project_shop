# ShopFlow
Boutique en ligne de matériel informatique : catalogue, panier et commande.

## Construire et tester
Prérequis : JDK 21 et Maven 3.9.
```
mvn test
```
Les règles métier détaillées sont dans le dossier `docs`

## Règles du panier
- R3 : au plus 10 unités d’un même produit.
- R5 : livraison 30 DH, offerte dès que le sous-total atteint 500 DH.
- R6 : un code promo donne de 1 à 50 % de remise sur le sous-total.
- R8 : pas de code promo sur un panier vide.
## Équipe
- Salma : responsable qualité
- Nadia : responsable produit
- Yassine : développeur
