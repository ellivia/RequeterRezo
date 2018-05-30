package requeterRezo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Champs contenus dans Index. Regroupe le nombre d'occurrences (nombre de fois
 * où le mot a été requeté) ainsi que la date de la dernière requête.
 *
 *
 * @author Jimmy Benoits
 */
public class IndexInfo {

    /**
     * Nombre de fois où le terme a été demandé.
     */
    protected int occurrences;
    /**
     * Horodatage de la dernière demande.
     */
    protected Date dateOccurrences;

    /**
     * Incrémente le nombre d'occurrences à condition que consultation
     * précédente se soit faite dans un délais inférieur au délais de
     * péremption. Sinon, retour du compteur à 1.
     *
     * @param peremption Délais de péremption (en jour).
     */
    protected void incrementeOccurrences(int peremption) {
        //Si la dernière occurrence est plus vieille que le délais de péremption
        //alors on remet à zéro le compteur        
        if (RequeterRezo.perime(dateOccurrences, peremption)) {
            occurrences = 1;
        } else {
            ++occurrences;
        }
        dateOccurrences = new Date();
    }

    /**
     * Constructeur paramétré à partir d'un nombre d'occurrences et d'une date
     * donnée.
     *
     * @param occurrences Nombre d'occurrences.
     * @param date_occurrences Date de la dernière occurrence.
     */
    protected IndexInfo(int occurrences, Date date_occurrences) {
        this.occurrences = occurrences;
        this.dateOccurrences = date_occurrences;
    }

    /**
     * Constructeur par défaut, donne la date du jour et 1 seule occurrence.
     */
    protected IndexInfo() {
        occurrences = 1;
        dateOccurrences = new Date();
    }

    /**
     * Retourne une chaîne de caractères sous le format "occurrences;date de la
     * dernière occurrence"
     *
     * @return La chaîne de caractères décrivant l'objet.
     */
    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa");
        return occurrences + ";" + df.format(dateOccurrences);
    }

    /**
     * Getter du nombre d'occurrences.
     * @return Le nombre d'occurrences.
     */
    protected int getOccurrences() {
        return occurrences;
    }

    /**
     * Getter de la date de la dernière occurrence.
     * @return La date de la dernière occurrence.
     */
    protected Date getDateOccurrences() {
        return dateOccurrences;
    }

}
