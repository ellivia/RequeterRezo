package requeterRezo;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Structure regroupant les informations obtenus sur un mot par une requête sur
 * jeuxdemots.
 */
public class Mot {

    /**
     * Chaîne de caractère décrivant le mot.
     */
    protected final String nom;

    /**
     * ID du noeud dans rezoJDM.
     */
    protected long id;

    /**
     * Type du noeud dans rezoJDM (-1 par défaut, en cas d'absence de type).
     */
    protected int type = -1;

    /**
     * Bien souvent identique à "nom", mais propose parfois d'autres
     * informations (notamment pour les annotations).
     */
    protected String mot_formate;

    /**
     * Définition retourné par jeuxdemots (partie entre les balises "def").
     */
    protected String definition = "";

    /**
     * Poids du mot dans la langue française.
     */
    protected double poids_general;

    /**
     * Ensemble des relations entrantes associées au mot dans le réseau. Les
     * clés sont les noms des relations dans jeuxdemots, les valeurs sont des
     * couples "terme,poids". voir ({@link Terme})
     */
    protected HashMap<String, ArrayList<Terme>> relations_entrantes;

    /**
     * Ensemble des relations sortantes associées au mot dans le réseau. Les
     * clés sont les noms des relations dans jeuxdemots.
     */
    protected HashMap<String, ArrayList<Terme>> relations_sortantes;

    /**
     * Ensemble des annotations du mot dans le réseau. Voir {@link Annotation}.
     */
    protected ArrayList<Annotation> annotations;

    /**
     * Constructeur simple. Appelé seulement par le système lorsqu'il doit
     * construire un mot à partir d'une requête sur JDM.
     *
     * @param nom Mot à construire
     */
    protected Mot(String nom) {
        this.nom = nom;
        this.mot_formate = nom;
        this.relations_entrantes = new HashMap<>();
        this.relations_sortantes = new HashMap<>();
        this.annotations = new ArrayList<>();
    }

    /**
     * Constructeur paramétré complet. Appelé seulement par le système lorsqu'il
     * doit construire un mot à partir d'un fichier présent dans le cache.
     *
     *
     * @param nom Mot à construire
     * @param id ID du noeud dans rezoJDM
     * @param type type du noeud dans rezoJDM
     * @param description Définition extraite de JeuxDeMots
     * @param mot_formate Mot_formate extrait de JeuxDeMots
     * @param pg Poids dans la langue française extrait de JeuxDeMots
     * @param relations_entrantes Relations entrantes extraites de JeuxDeMots
     * @param relations_sortantes Relations sortantes extraites de JeuxDeMots
     * @param annotations Annotations des relations du mot
     */
    protected Mot(String nom, long id, int type, String mot_formate, double pg, String description,
            HashMap<String, ArrayList<Terme>> relations_entrantes,
            HashMap<String, ArrayList<Terme>> relations_sortantes,
            ArrayList<Annotation> annotations) {
        this.nom = nom;
        this.id = id;
        this.type = type;
        this.mot_formate = mot_formate;
        this.poids_general = pg;
        this.relations_entrantes = relations_entrantes;
        this.relations_sortantes = relations_sortantes;
        this.definition = description;
        this.annotations = annotations;
    }

    /**
     * Retourne le nom du mot ("lui-même")
     *
     * @return Le nom du mot
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le mot formaté (identique à {@link Mot#nom} sans dans le cas des
     * annotations.
     *
     * @return Retourne le mot formaté (identique à {@link Mot#nom} sans dans le
     * cas des annotations.
     */
    public String getMotFormate() {
        return this.mot_formate;
    }

    /**
     * Retourne l'id du noeud dans rezoJDM
     *
     * @return L'id du noeud dans rezoJDM
     */
    public long getID() {
        return this.id;
    }

    /**
     * Retourne le type du noeud dans rezoJDM
     *
     * @return Le type du noeud dans rezoJDM
     */
    public int getType() {
        return this.type;
    }

    /**
     * Retourne les relations entrantes du mot dans le réseau.
     *
     * @return Retourne les relations entrantes du mot dans le réseau.
     */
    public HashMap<String, ArrayList<Terme>> getRelations_entrantes() {
        return relations_entrantes;
    }

    /**
     * Retourne les relations sortantes du mot dans le réseau.
     *
     * @return Retourne les relations sortantes du mot dans le réseau.
     */
    public HashMap<String, ArrayList<Terme>> getRelations_sortantes() {
        return relations_sortantes;
    }

    /**
     * Retourne les annotations du mot.
     *
     * @return Retourne les annotations du mot.
     */
    public ArrayList<Annotation> getAnnotations() {
        return this.annotations;
    }

    /**
     * Retourne la définition du mot.
     *
     * @return Retourne la définition du mot.
     */
    public String getDefinition() {
        return this.definition;
    }

    /**
     * Retourne le poids du mot dans la langue française.
     *
     * @return Le poids du mot dans la langue française.
     */
    public double getPoids_general() {
        return poids_general;
    }

    /**
     * Ecrit toute la structure du Mot dans un fichier, pour une réutilisation
     * future (notamment dans le cache).
     *
     * @param mot Mot à conserver.
     * @param fichier Fichier où le Mot doit être stocké.
     * @throws IOException
     */
    public static void ecrire(Mot mot, File fichier) throws IOException {
        try (BufferedWriter ecrivain = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8"))) {
            //System.out.println(mot.getNom());
            ecrivain.write(mot.getNom());
            ecrivain.newLine();
            ecrivain.write(mot.getMotFormate());
            ecrivain.newLine();
            //id
            ecrivain.write(String.valueOf(mot.getID()));
            ecrivain.newLine();
            //type
            ecrivain.write(String.valueOf(mot.getType()));
            ecrivain.newLine();
            //System.out.println(mot.getPoids_général());
            ecrivain.write("" + mot.getPoids_general());
            ecrivain.newLine();
            ecrivain.write("<def>");
            ecrivain.newLine();
            ecrivain.write(mot.getDefinition());
            ecrivain.newLine();
            ecrivain.write("</def>");
            ecrivain.newLine();
            ecrivain.write("sortant");
            ecrivain.newLine();
            // pour toutes les relations dans les relations_sortantes
            for (Entry<String, ArrayList<Terme>> entree : mot.getRelations_sortantes().entrySet()) {
                ecrivain.write(entree.getKey());
                for (Terme motCible : entree.getValue()) {
                    ecrivain.write(";" + motCible.getNom() + ",," + motCible.getPoids());
                }
                ecrivain.newLine();
            }
            ecrivain.write("entrant");
            ecrivain.newLine();
            for (Entry<String, ArrayList<Terme>> entree : mot.getRelations_entrantes().entrySet()) {
                ecrivain.write(entree.getKey());
                for (Terme motCible : entree.getValue()) {
                    ecrivain.write(";" + motCible.getNom() + ",," + motCible.getPoids());
                }
                ecrivain.newLine();
            }
            ecrivain.write("annotation");
            ecrivain.newLine();
            for (Annotation annotation : mot.getAnnotations()) {
                ecrivain.write(annotation.toString());
                ecrivain.newLine();
            }
        }
    }

    /**
     * Retourne la structure de Mot depuis un fichier écrit par la fonction
     * "ecrire".
     *
     * @param chemin Chemin du fichier qui doit être lu.
     * @return Le Mot sauvegardé dans le fichier.
     * @throws IOException
     */
    public static Mot lire(String chemin) throws IOException {
        String nom;
        long id = -1;
        int type = -1;
        String description;
        String mot_formate;
        String poids_general = "";
        HashMap<String, ArrayList<Terme>> relations_sortantes;
        HashMap<String, ArrayList<Terme>> relations_entrantes;
        ArrayList<Annotation> annotations = new ArrayList<>();
        try (BufferedReader lecteur = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            nom = "";
            mot_formate = "";
            description = "";
            String[] divisions;
            String[] sous_divisions;
            relations_sortantes = new HashMap<>();
            relations_entrantes = new HashMap<>();
            // lecture du nom :
            if ((ligne = lecteur.readLine()) != null) {
                nom = ligne;
            }
            // lecture du mot_formate
            if ((ligne = lecteur.readLine()) != null) {
                mot_formate = ligne;
            }
            // lecture de l'id
            if ((ligne = lecteur.readLine()) != null) {
                id = Long.parseLong(ligne);
            }
            // lecture du type
            if ((ligne = lecteur.readLine()) != null) {
                type = Integer.parseInt(ligne);
            }
            // lecture du poids
            if ((ligne = lecteur.readLine()) != null) {
                poids_general = ligne;
            }
            //Saut de la ligne "<def>"
            lecteur.readLine();
            //lecture de la description
            while (((ligne = lecteur.readLine()) != null) && !(ligne.equals("</def>"))) {
                description += ligne;
            }
            //Saut de la ligne "sortant"
            lecteur.readLine();
            // lecture des relations sortantes
            while (((ligne = lecteur.readLine()) != null) && !(ligne.equals("entrant"))) {
                divisions = ligne.split(";");
                relations_sortantes.put(divisions[0], new ArrayList<>());
                for (int i = 1; i < divisions.length; ++i) {
                    sous_divisions = divisions[i].split(",,");
                    if (sous_divisions.length == 2) {
                       // relations_sortantes.get(divisions[0]).add(new Terme(sous_divisions[0], Double.parseDouble(sous_divisions[1])));
                    }
                }
            }
            //lecture des relations entrantes
            while (((ligne = lecteur.readLine()) != null) && !(ligne.equals("annotation"))) {
                divisions = ligne.split(";");
                relations_entrantes.put(divisions[0], new ArrayList<>());
                for (int i = 1; i < divisions.length; ++i) {
                    sous_divisions = divisions[i].split(",,");
                    if (sous_divisions.length == 2) {
                       // relations_entrantes.get(divisions[0]).add(new Terme(sous_divisions[0], Double.parseDouble(sous_divisions[1])));
                    }
                }
            }
            //lecture des annotations
            while ((ligne = lecteur.readLine()) != null) {
                divisions = ligne.split(";");
                annotations.add(new Annotation(divisions[0], divisions[1], divisions[2], divisions[3], Double.parseDouble(divisions[4])));
            }
        }
        return new Mot(nom, id, type, mot_formate, Double.parseDouble(poids_general), description, relations_entrantes, relations_sortantes, annotations);
    }

    /**
     * Construit l'URL d'un mot dans JeuxdeMot.
     *
     * @param mot Nom du mot dont il faut construire l'URL.
     * @return L'URL complète permettant de retrouver le mot sur le réseau.
     * @throws java.io.UnsupportedEncodingException
     */
    protected static String recupURL(String mot) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(mot, "LATIN1");
        return "http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + encode; //+ "&output=onlyxml";
    }
    
    protected static String recupURL(String mot, int typeRelation) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(mot, "LATIN1");
        return "http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + encode + "&rel=" + Integer.toString(typeRelation);
    }
    
    protected static String recupURL(String mot, int typeRelation, boolean sortantes, boolean entrantes) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(mot, "LATIN1");
        String url="http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + encode + "&rel=" + Integer.toString(typeRelation);
        if (!sortantes) {
        	url+="&relout=norelout";
        }
        if (!entrantes) {
        	url+="&relin=norelin";
        }
        return url;
    }
    
    protected static String recupURL(String mot, boolean sortantes, boolean entrantes) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(mot, "LATIN1");
        String url="http://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + encode;
        if (!sortantes) {
        	url+="&relout=norelout";
        }
        if (!entrantes) {
        	url+="&relin=norelin";
        }
        return url;
    }

    /**
     * Retourne une chaîne de caractères décrivant l'ensemble de la structure.
     * Pour une lecture / écriture dans un fichier, préférez
     * {@link Mot#ecrire(RequeterRezo.Mot, java.io.File)} et
     * {@link Mot#lire(java.lang.String)}
     *
     * @return Une chaîne de caractères décrivant l'ensemble de la structure
     * extraite depuis JeuxDeMots.
     */
    @Override
    public String toString() {
        return "Mot{"
                + "nom=" + nom + ", "
                + "id=" + id + ","
                + "type=" + type + ","
                + "mot_formate=" + mot_formate + ", "
                + "definition=" + definition + ", "
                + "relations_entrantes=" + relations_entrantes + ", "
                + "relations_sortantes=" + relations_sortantes + ", "
                + "annotations= " + annotations + "}";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    /**
     * Deux mots sont identiques s'il partage le même champ "nom"
     *
     * @param obj Mot à comparer
     * @return True si les deux mots partagent le champ "nom"
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mot other = (Mot) obj;
        return Objects.equals(this.nom, other.nom);
    }

    /**
     * Permet de changer le poids d'un mot (en local, n'a aucun effet sur le
     * réseau).
     *
     * @param poids_general Nouveau poids
     */
    public void setPoids_general(double poids_general) {
        this.poids_general = poids_general;
    }

    /**
     * Permet de changer les relations entrantes d'un mot (en local, n'a aucun
     * effet sur le réseau).
     *
     * @param relations_entrantes Nouvelles relations entrantes
     */
    public void setRelations_entrantes(HashMap<String, ArrayList<Terme>> relations_entrantes) {
        this.relations_entrantes = relations_entrantes;
    }

    /**
     * Permet de changer les relations sortantes d'un mot (en local, n'a aucun
     * effet sur le réseau).
     *
     * @param relations_sortantes Nouvelles relations sortantes
     */
    public void setRelations_sortantes(HashMap<String, ArrayList<Terme>> relations_sortantes) {
        this.relations_sortantes = relations_sortantes;
    }

    /**
     * Permet de changer les annotation d'un mot (en local, n'a aucun effet sur
     * le réseau).
     *
     * @param annotations Nouvelles annotations
     */
    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * Permet de changer la définition d'un mot (en local, n'a aucun effet sur
     * le réseau).
     *
     * @param definition Nouvelle définition du mot.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Permet de changer la forme formaté d'un mot (en local, n'a aucun effet
     * sur le réseau).
     *
     * @param mot_formate Nouveau mot formate.
     */
    public void setMotFormate(String mot_formate) {
        this.mot_formate = mot_formate;
    }

    /**
     * Permet de changer l'id d'un mot (local, n'a aucun effet sur le réseau).
     *
     * @param id Nouvel ID
     */
    public void setID(long id) {
        this.id = id;
    }

    /**
     * Permet de changer le type d'un mot (local, n'a aucun effet sur le réseau)
     *
     * @param type Nouveau type
     */
    public void setType(int type) {
        this.type = type;
    }

}
