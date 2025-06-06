// Importation des classes nécessaires pour la base de données
import java.sql.*;

// Importation des classes Swing pour l'interface graphique
import javax.swing.*;

public class JTable1 extends javax.swing.JFrame { // La classe hérite de JFrame pour créer une fenêtre

    public JTable1() {
        initComponents(); // Appel de la méthode pour initialiser les composants de l'interface
        setTitle("Gestion de la Boutique"); // Titre de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
    }

    private void initComponents() {
        // Création des composants de l'interface
        jPanel = new JPanel(); // Panneau principal
        titleLabel = new JLabel("MA BOUTIQUE"); // Titre principal
        nomLabel = new JLabel("Nom"); // Étiquette pour le champ nom
        prenomLabel = new JLabel("Prénom"); // Étiquette pour le champ prénom
        prixHTLabel = new JLabel("Prix total HT (€)"); // Étiquette pour le prix HT
        montantTTCLabel = new JLabel("Montant total TTC (€)"); // Étiquette pour le montant TTC

        nomField = new JTextField(); // Champ texte pour le nom
        prenomField = new JTextField(); // Champ texte pour le prénom
        prixHTField = new JTextField(); // Champ texte pour le prix HT
        montantTTCValue = new JLabel("0.00"); // Étiquette pour afficher le montant TTC calculé

        calculerBtn = new JButton("Calculer"); // Bouton pour déclencher le calcul

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Fermer l'application à la fermeture de la fenêtre

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // Mise en forme du titre (gras et taille 20)

        calculerBtn.setBackground(new java.awt.Color(102, 255, 102)); // Couleur du bouton
        calculerBtn.addActionListener(e -> calculerMontant()); // Action à effectuer lors du clic : appel à calculerMontant()

        // Utilisation du layout GroupLayout pour organiser les composants dans le panneau
        GroupLayout layout = new GroupLayout(jPanel);
        jPanel.setLayout(layout);
        layout.setAutoCreateGaps(true); // Espaces automatiques entre composants
        layout.setAutoCreateContainerGaps(true); // Espaces autour du conteneur

        // Configuration horizontale du layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleLabel)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(nomLabel)
                        .addComponent(prenomLabel)
                        .addComponent(prixHTLabel)
                        .addComponent(montantTTCLabel))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomField, 200, 200, 200)
                        .addComponent(prenomField)
                        .addComponent(prixHTField)
                        .addComponent(montantTTCValue)))
                .addComponent(calculerBtn)
        );

        // Configuration verticale du layout
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(titleLabel)
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nomLabel)
                    .addComponent(nomField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(prenomLabel)
                    .addComponent(prenomField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(prixHTLabel)
                    .addComponent(prixHTField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(montantTTCLabel)
                    .addComponent(montantTTCValue))
                .addGap(20)
                .addComponent(calculerBtn)
        );

        // Ajout du panneau à la fenêtre principale
        getContentPane().add(jPanel);
        pack(); // Ajuste automatiquement la taille de la fenêtre
    }

    // Méthode pour calculer le montant TTC
    private void calculerMontant() {
        // Récupération des valeurs saisies par l'utilisateur
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String prixHTStr = prixHTField.getText().trim();

        // Vérifie si un champ est vide
        if (nom.isEmpty() || prenom.isEmpty() || prixHTStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Arrête l'exécution si les champs ne sont pas remplis
        }

        try {
            // Conversion du prix en double
            double prixHT = Double.parseDouble(prixHTStr);

            // Calcul de la réduction si prix > 200
            double reduction = prixHT > 200 ? prixHT * 0.15 : 0;

            // Prix après réduction
            double prixApresReduction = prixHT - reduction;

            // Calcul de la TVA (20%)
            double tva = prixApresReduction * 0.20;

            // Calcul du montant TTC
            double montantTTC = prixApresReduction + tva;

            // Affichage du résultat dans le JLabel
            montantTTCValue.setText(String.format("%.2f", montantTTC));

            // Enregistrement dans la base de données
            enregistrerDansBD(nom, prenom, prixHT, montantTTC);

        } catch (NumberFormatException e) {
            // Gestion d'erreur si la saisie du prix est invalide
            JOptionPane.showMessageDialog(this, "Prix invalide. Veuillez entrer un nombre.", "Erreur de format", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour enregistrer les données dans la base de données MySQL
    private void enregistrerDansBD(String nom, String prenom, double prixHT, double montantTTC) {
        // Paramètres de connexion à la base
        String url = "jdbc:mysql://localhost:3306/boutique_exo2";
        String user = "root";
        String password = "";

        // Requête SQL d'insertion
        String sql = "INSERT INTO historique (nom, prenom, prix_ht, prix_ttc) VALUES (?, ?, ?, ?)";

        // Connexion à la base et exécution de la requête
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Remplissage des paramètres de la requête
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setDouble(3, prixHT);
            stmt.setDouble(4, montantTTC);

            int rows = stmt.executeUpdate(); // Exécution

            // Confirmation si l'enregistrement a réussi
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Transaction enregistrée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            // Affiche un message si erreur de base de données
            JOptionPane.showMessageDialog(this, "Erreur de connexion ou d'enregistrement : " + e.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Point d'entrée principal de l'application
    public static void main(String[] args) {
        // Définir le look and feel du système pour l'apparence native
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Lancer l'application en créant une instance de la fenêtre
        SwingUtilities.invokeLater(() -> new JTable1().setVisible(true));
    }

    // Déclaration des composants graphiques utilisés
    private JPanel jPanel;
    private JLabel titleLabel, nomLabel, prenomLabel, prixHTLabel, montantTTCLabel;
    private JTextField nomField, prenomField, prixHTField;
    private JLabel montantTTCValue;
    private JButton calculerBtn;
}
