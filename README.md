# Gestion-boutique

# Application Java - Gestion de Boutique

Cette application Java Swing permet de calculer le montant TTC d’un achat avec une réduction appliquée pour les achats supérieurs à 200 €, et enregistre les transactions dans une base de données MySQL.

---

# Fonctionnalités

- Saisie du nom, prénom et prix total hors taxe (HT) d’un achat.
- Calcul automatique du montant TTC après réduction (15% si > 200 €) et TVA (20%).
- Affichage du montant TTC calculé.
- Enregistrement automatique de la transaction dans une base MySQL.

---

# Prérequis

- Java JDK 8 ou supérieur installé.
- MySQL installé et en fonctionnement sur `localhost` avec un utilisateur `root` sans mot de passe (modifiable dans le code).
- Base de données `boutique_exo2` avec la table `historique` créée (voir ci-dessous).

---

# Installation de la base de données

Exécutez ce script SQL pour créer la base et la table nécessaires :

```sql
CREATE DATABASE IF NOT EXISTS boutique_exo2;

USE boutique_exo2;

CREATE TABLE IF NOT EXISTS historique (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50),
  prenom VARCHAR(50),
  prix_total_ht DOUBLE,
  prix_total_ttc DOUBLE
);
