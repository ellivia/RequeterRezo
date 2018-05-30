package requeterRezo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Champs contenus dans l'index du cache. Regroupe un identifiant (permettant
 * notamment de construire un chemin vers le fichier correspondant), une date
 * d'entrée dans le cache, un nombre d'occurrences ainsi que la date de la
 * dernière occurrence.
 *
 * @author Jimmy Benoits
 */
public class CacheInfo {

    /**
     * Identifiant, permet de construire le chemin du fichier dans
     * l'arborescence.
     */
    protected final int ID;

    /**
     * Date de l'entrée dans le cache.
     */
    protected Date dateCache;

    /**
     * Nombre de fois où le terme à été rencontré.
     */
    protected int occurrences;

    /**
     * Date de la dernière rencontre du terme.
     */
    protected Date dateOccurrences;

    /**
     * Construit le chemin à partir de l'id. Exemple : id = 378 alors chemin
     * "/3/7/8.cache".
     *
     * @param ID ID du terme.
     * @return Le chemin du fichier.
     */
    protected static String construireChemin(int ID) {
        String res = "";
        String tmp = Integer.toString(ID);
        for (int i = 0; i < tmp.length(); ++i) {
            res += File.separator + tmp.charAt(i);
        }
        return res + ".cache";
    }

    /**
     * Incrémente le nombre d'occurrence et met à jour la date de la dernière
     * occurrence.
     */
    protected void incrementeOccurrences() {
        ++occurrences;
        dateOccurrences = new Date();
    }

    /**
     * Constructeur à partir de l'ID (nombre d'occurrences = 1 et les dates sont
     * les dates sont re-crées.
     *
     * @param ID Identifiant du mot.
     */
    protected CacheInfo(int ID) {
        this(ID, new Date(), 1, new Date());
    }

    /**
     * Constructeur à partir de l'ID et du nombre d'occurrences.
     *
     * @param ID Identifiant du mot.
     * @param occurrences Nombre d'occurences.
     */
    protected CacheInfo(int ID, int occurrences) {
        this(ID, new Date(), occurrences, new Date());
    }

    /**
     * Constructeur à partir de l'ID, du nombre d'occurrences ainsi que de la
     * date de la dernière occurrence.
     *
     * @param ID Identificant du mot.
     * @param occurrences Nombre d'occurrences.
     * @param dateOccurrences Date de la dernière occurrence.
     */
    protected CacheInfo(int ID, int occurrences, Date dateOccurrences) {
        this(ID, new Date(), occurrences, dateOccurrences);
    }

    /**
     * Constructeur à partir de l'ID, du nombre d'occurrences, de la date
     * d'entrée dans le cache ainsi que de la date de la dernière occurrence.
     *
     * @param ID Identifiant du mot.
     * @param dateCache Date d'entrée dans le cache.
     * @param occurrences Nombre d'occurrences.
     * @param dateOccurrences Date de la dernière occurrence.
     */
    protected CacheInfo(int ID, Date dateCache, int occurrences, Date dateOccurrences) {
        this.ID = ID;
        this.dateCache = dateCache;
        this.occurrences = occurrences;
        this.dateOccurrences = dateOccurrences;
    }

    /**
     * Retourne l'id.
     *
     * @return retourne l'id.
     */
    protected int getID() {
        return ID;
    }

    /**
     * Retourne la date d'entrée dans le cache.
     *
     * @return Retourne la date d'entrée dans le cache.
     */
    protected Date getDateCache() {
        return dateCache;
    }

    /**
     * Retourne le nombre d'occurrences.
     *
     * @return Retourne le nombre d'occurrences.
     */
    protected int getOccurrences() {
        return occurrences;
    }

    /**
     * Retourne la date de la dernière occurrence.
     *
     * @return Retourne la date de la dernière occurrence.
     */
    protected Date getDateOccurrences() {
        return dateOccurrences;
    }

    /**
     * Retourne une chaîne de caractères sous le format "id;date d'entrée dans
     * le cache;nombre d'occurences;date de dernière consultation dans le cache"
     *
     * @return La chaîne de caractères décrivant l'objet.
     */
    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa");
        return ID + ";" + df.format(dateCache) + ";" + occurrences + ";" + df.format(dateOccurrences);
    }

}
