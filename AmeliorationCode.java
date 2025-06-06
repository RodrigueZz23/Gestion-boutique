// Importation des bibliothèques nécessaires pour les connexions SQL et l'interface graphique Swing
import java.sql.*; // Pour la connexion à la base de données MySQL
import javax.swing.*; // Pour l’interface graphique Swing

// Déclaration de la classe principale qui hérite de JFrame pour construire l'interface
public class AmeliorationCode extends JFrame {

    // Déclaration des composants Swing
    private JButton calculer;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5;
    private JPanel jPanel1;
    private JLabel montant_tva;
    private JTextField nom, prenom, prix_total_ht;

    // Constructeur qui initialise l’interface
    public AmeliorationCode() {
        initComponents(); // Méthode qui construit l’interface graphique
    }

    // Méthode principale pour lancer l’application
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Interface_Boutique().setVisible(true); // Affichage de la fenêtre
        });
    }

    // Méthode qui construit tous les composants de l’interface graphique
    private void initComponents() {
        // Initialisation des composants
        jPanel1 = new JPanel();
        jLabel1 = new JLabel("MA BOUTIQUE");
        jLabel2 = new JLabel("Nom");
        jLabel3 = new JLabel("Prenom");
        jLabel4 = new JLabel("Prix Total HT");
        jLabel5 = new JLabel("Montant Total TTC");

        nom = new JTextField();
        prenom = new JTextField();
        prix_total_ht = new JTextField();
        montant_tva = new JLabel("0"); // Initialisation à 0

        calculer = new JButton("Calculer");

        // Configuration du bouton "Calculer"
        calculer.setBackground(new java.awt.Color(102, 255, 102));
        calculer.setFont(new java.awt.Font("Segoe UI", 0, 14));
        calculer.addActionListener(evt -> calculerActionPerformed(evt)); // Action du bouton

        // Ajout des composants au JPanel avec layout
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup().addGap(245, 245, 245).addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createSequentialGroup().addGap(44, 44, 44)
                            .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(calculer)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel5))
                                    .addGap(44, 44, 44)
                                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nom).addComponent(prenom)
                                        .addComponent(prix_total_ht)
                                        .addComponent(montant_tva, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)))))))
                   
        );

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(15)
                    .addComponent(jLabel1)
                    .addGap(18)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2).addComponent(nom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3).addComponent(prenom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4).addComponent(prix_total_ht, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5).addComponent(montant_tva))
                    .addGap(30)
                    .addComponent(calculer)
                    .addContainerGap(200, Short.MAX_VALUE))
        );

        // Ajout du panel dans la fenêtre principale
        getContentPane().add(jPanel1);
        pack(); // Ajustement automatique de la taille de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Ferme l’application quand on clique sur la croix
    }

    // Action déclenchée lorsqu'on clique sur le bouton "Calculer"
    private void calculerActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Récupération des valeurs saisies par l'utilisateur
            String n = nom.getText();
            String p = prenom.getText();
            double pt = Double.parseDouble(prix_total_ht.getText());

            // Calcul de la réduction si le prix est supérieur à 200
            double reduction = (pt > 200) ? pt * 0.15 : 0.0;
            double prix_apres_reduction = pt - reduction;

            // Calcul de la TVA (20%)
            double ttc = prix_apres_reduction * 1.2;

            // Affichage du montant TTC dans l’interface
            montant_tva.setText(String.format("%.2f", ttc));

            // Enregistrement des données dans la base de données
            ajouter_bd(n, p, pt, ttc);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un montant HT valide !");
        }
    }

    // Méthode pour ajouter une transaction dans la base de données
    private void ajouter_bd(String n, String p, double pt, double ttc) {
        try {
            // Connexion à la base de données MySQL
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/boutique_exo2", "root", "");

            // Requête SQL d'insertion (id auto-incrémenté)
            String sql = "INSERT INTO historique VALUES (NULL, ?, ?, ?, ?)";

            // Préparation de la requête
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, n);
            ps.setString(2, p);
            ps.setDouble(3, pt);
            ps.setDouble(4, ttc);

            // Exécution de la requête
            ps.executeUpdate();

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Transaction enregistrée avec succès !");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion ou de requête SQL !");
            e.printStackTrace();
        }
    }
}
