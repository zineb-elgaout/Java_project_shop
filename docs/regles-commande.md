# Règles de la prise de commande
Règles validées par Nadia, vérifiées par la classe `ServiceCommandeTest` (TP2).
| Règle | Énoncé |
|---|---|
| C1 | Un panier vide est refusé, sans contacter aucun service. |
| C2 | Produit en rupture : commande refusée, rien n'est réservé, aucun paiement 
n'est tenté. |
| C3 | Sinon, chaque ligne est réservée dans le stock, puis le total exact est 
débité. |
| C4 | Paiement accepté : commande enregistrée CONFIRMEE, puis e-mail de 
confirmation. |
| C5 | Paiement refusé : stock libéré, rien n'est enregistré, aucun e-mail n'est 
envoyé. |
| C6 | Une panne de l'envoi d'e-mail n'annule pas une commande payée. |
