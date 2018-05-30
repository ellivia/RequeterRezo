package requeterRezo;

/**
 * Terme connexe au mot requêté. Les seuls informations connues sont le nom et
 * le poids.
 */
public class Terme {

    /**
     * Nom du terme connexe.
     */
    protected final Mot mot;

    /**
     * Poids du terme connexe.
     */
    protected final double poids;

    protected Terme(Mot mot, double poids) {
        /*
    	if (terme.contains("  ")) {
            terme = terme.replaceAll("  ", " ");
        }
        */
        this.mot = mot;
        this.poids = poids;
    }

    /**
     * Décrit l'objet sous la forme "mot=poids".
     *
     * @return Une chaîne de caractères décrivant l'objet sous la forme
     * "mot=poids".
     */
    
    //A changer si besoin de getMot()
    @Override
    public String toString() {
        return (this.getNom() + "=" + this.getPoids());
    }

    /**
     * Retourne le mot.
     *
     * @return Le mot.
     */
    public Mot getMot() {
        return mot;
    }
    
    public String getNom() {
    	return mot.getNom();
    }

    /**
     * Retourne le poids dans la langue française du mot (poids issu de rezoJDM)
     *
     * @return Retourne le poids dans la langue française du mot (poids issu de
     * rezoJDM)
     */
    public double getPoids() {
        return poids;
    }
}
