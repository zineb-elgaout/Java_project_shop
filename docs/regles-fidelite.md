# Règles du programme de fidélité
Règles écrites en TDD au TP2 (classe `CalculateurFidelite`).
| Règle | Énoncé |
|---|---|
| F1 | Une commande rapporte 1 point par tranche entière de 10 DH. |
| F2 | Un client GOLD gagne deux fois plus de points qu'un client STANDARD. |
| F3 | Les points sont plafonnés à 500 par commande, après le multiplicateur. |
| F4 | Un montant négatif est refusé. |

Une commande refusée ne laisse aucune trace dans la base.

Les points sont crédités quand la commande passe au statut CONFIRMEE. 
