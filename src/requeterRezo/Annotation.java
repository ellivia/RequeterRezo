package requeterRezo;

/**
 * Les annotations sont des objets de rezoJDM permettant d'ajouter une
 * information à une relation. Dans rezoJDM, les annotations se situent dans les
 * relations sortantes (souvent avec un type de relation "r_associated") même si
 * l'annotation concerne une relation entrante. Pour plus de clarté, le choix a
 * été fait de les différencier dans RequeterRezo. Dans rezoJDM, une annotation
 * est aussi un noeud comme les autresn accessible ici depuis
 * {@link Annotation#getId_annotation()}.
 */
public class Annotation {

    /**
     * ID de l'annotation dans rezoJDM. Permet de retrouver le noeud associé
     * dans le réseau.
     */
    String id_annotation;
    /**
     * Mot entrant de la relation annotée.
     */
    String mot_entrant;
    /**
     * Type de la relation annotée.
     */
    String type_relation;
    /**
     * Mot sortant de la relation annotée.
     */
    String mot_sortant;
    /**
     * Poids de l'annotation.
     */
    double poids;

    /**
     * Constructeur paramétré.
     *
     * @param id_annotation ID de l'annotation dans le réseau.
     * @param mot_entrant Mot entrant de la relation annotée.
     * @param type_relation Type de la relation annotée.
     * @param mot_sortant Mot sortant de la relation annotée.
     * @param poids Poids de l'annotation.
     */
    public Annotation(String id_annotation, String mot_entrant, String type_relation, String mot_sortant, double poids) {
        this.id_annotation = id_annotation;
        this.mot_entrant = mot_entrant;
        this.type_relation = type_relation;
        this.mot_sortant = mot_sortant;
        this.poids = poids;
    }

    /**
     * Retourne l'ID de l'annotation dans le réseau
     *
     * @return Retourne l'ID de l'annotation dans le réseau.
     */
    public String getId_annotation() {
        return id_annotation;
    }

    /**
     * Retourne le mot entrant de la relation annotée;
     *
     * @return Retourne le mot entrant de la relation annotée;
     */
    public String getMot_entrant() {
        return mot_entrant;
    }

    /**
     * Retourne le type de la relation annotée.
     *
     * @return Retourne le type de la relation annotée.
     */
    public String getType_relation() {
        return type_relation;
    }

    /**
     * Retourne le mot sortant de la relation annotée.
     *
     * @return Retourne le mot sortant de la relation annotée.
     */
    public String getMot_sortant() {
        return mot_sortant;
    }

    /**
     * Retourne le poids de l'annotation.
     *
     * @return Retourne le poids de l'annotation.
     */
    public double getPoids() {
        return poids;
    }

    /**
     * Retourne une chaîne décrivant l'annotation sous le forme : "id;mot
     * entrant;type relation;mot sortant;poids".
     *
     * @return Retourne une chaîne décrivant l'annotation sous le forme :
     * "id;mot entrant;type relation;mot sortant;poids".
     */
    @Override
    public String toString() {
        return id_annotation + ";" + mot_entrant + ";" + type_relation + ";" + mot_sortant + ";" + poids;
    }

}
