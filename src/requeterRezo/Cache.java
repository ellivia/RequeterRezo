package requeterRezo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Index des fichiers présents dans le cache. Composé de trois éléments : une
 * table connectant un mot (en plein texte) et un ensemble d'informations
 * nécessaires (voir {@link CacheInfo}), un compteur permettant d'affecter un id
 * au prochain terme à ajouter et un nombre de jour à partir duquel un fichier
 * dans le cache est considéré comme obsolète.
 *
 * @author Jimmy Benoits
 */
@SuppressWarnings("serial")
public class Cache extends HashMap<String, CacheInfo> {

    /**
     * Liste des IDs disponibles.
     */
    protected Queue<Integer> diponiblesID;
    /**
     * Liste des IDs déjà utilisé par le cache.
     */
    protected Queue<Integer> prisID;

    /**
     * Nombre d'heures à partir duquel un fichier dans le cache est considéré
     * comme obsolète.
     */
    protected final int peremption;

    /**
     * Constructeur unique, il est nécessaire de préciser le délais de
     * péremption (en heures).
     *
     * @param peremption Délais de péremption, en jour
     * @param tailleMax Nombre maximum d'entrées
     */
    protected Cache(int peremption, int tailleMax) {
        super(tailleMax * (4 / 3));
        this.peremption = peremption;
        this.diponiblesID = new LinkedList<>();
        //Par défaut, tout est disponible
        for (int i = 0; i < tailleMax; ++i) {
            this.diponiblesID.add(i);
        }
        this.prisID = new LinkedList<>();
    }

    /**
     * Chargement d'un Cache à partir d'un fichier existant créé lors d'une
     * précédente session. Attention, lors de la réduction de la taille du
     * cache, seuls les X premiers éléments
     *
     * @param fichier Chemin vers le fichier "indexCache" de la session
     * précédente.
     * @param peremption Délais de péremption (en jours).
     * @param tailleMax Nombre maximum d'entrées.
     * @param chemin_cache Chemin vers le dossier du cache de la session
     * précédente. Permet la suppression d'entrées dans le cas de la réduction
     * de taille du cache.
     * @return Un Cache instancié grâce au fichier.
     * @throws IOException
     * @throws ParseException
     */
    protected static Cache chargerCache(String fichier, int peremption, int tailleMax, String chemin_cache) throws IOException, ParseException {
        Cache cache = new Cache(peremption, tailleMax);
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String line;
            String[] tokens;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa");
            int id;
            File fichier_a_supprimer;
            //première ligne : les IDs déjà pris (dans la nouvelle limite)
            if ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    for (String token : line.split(";")) {
                        id = Integer.parseInt(token);
                        if (id < tailleMax) {
                            cache.rendreIndisponible(id);
                        } else {
                            //La taille du cache a été réduite : il faut supprimer l'entrée                        
                            fichier_a_supprimer = new File(chemin_cache + File.separator + CacheInfo.construireChemin(id));
                            if (fichier_a_supprimer.exists()) {
                                fichier_a_supprimer.delete();
                                //Suppression du dossier s'il est vide
                                fichier_a_supprimer = fichier_a_supprimer.getParentFile();
                                if (fichier_a_supprimer.list().length == 0) {
                                    fichier_a_supprimer.delete();
                                }
                            }
                        }
                    }
                }
            }
            if (line != null) {
                while ((line = reader.readLine()) != null) {
                    tokens = line.split(";");
                    id = Integer.parseInt(tokens[1]);
                    if (id < tailleMax) {
                        cache.put(
                                tokens[0],
                                new CacheInfo(id,
                                        df.parse(tokens[2]),
                                        Integer.parseInt(tokens[3]),
                                        df.parse(tokens[4]))
                        );
                    }
                }
            }
        }
        return cache;
    }

    /**
     * Sauvegarde dans un fichier l'index du cache.
     *
     * @param cache Index de cache.
     * @param fichier Chemin du fichier à écrire.
     * @throws IOException
     */
    protected static void sauvegarderCache(Cache cache, String fichier) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier + "_tmp"))) {
            //première ligne : les IDs déjà pris
            for (Integer i : cache.prisID) {
                writer.write("" + i + ";");
            }
            writer.newLine();
            for (Entry<String, CacheInfo> entry : cache.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue().toString());
                writer.newLine();
            }
            File file = new File(fichier);
            if (file.exists()) {
                file.delete();
            }
        }
        File _old = new File(fichier + "_tmp");
        File _new = new File(fichier);
        _old.renameTo(_new);
        _new.setReadOnly();
    }

    /**
     * Indique si un mot a passé, ou non, sa date de péremption.
     *
     * @param mot Mot à tester.
     * @return True si le mot est présent dans le cache depuis plus de jours que
     * le délais de péremption. False sinon.
     */
    protected boolean estPerime(String mot) {
        return RequeterRezo.perime(this.get(mot).getDateCache(), peremption);
    }

    /**
     * Ajoute un mot à l'index.
     *
     * @param mot Mot à ajouter.
     * @param occurrences Nombre d'occurrences (de requêtes) du mot dans avant
     * sa mise en cache (dans une fenêtre définié par le délais de péremption).
     */
    protected void ajouter(String mot, int occurrences) {
        int prochainID = this.diponiblesID.remove();
        this.put(mot, new CacheInfo(prochainID, occurrences));
        this.prisID.add(prochainID);
    }

    /**
     * Supprime un mot de l'index et rend disponible son ID.
     *
     * @param mot Mot à supprimer.
     */
    protected void supprimer(String mot) {
        if (this.containsKey(mot)) {
            int id = this.get(mot).ID;
            this.diponiblesID.add(id);
            this.prisID.remove(id);
            this.remove(mot);
        }
    }

    /**
     * Rend indisponible un id particulier. Utilisé notamment lors du chargement
     * d'un cache précédent.
     *
     * @param id
     */
    private void rendreIndisponible(int id) {
        this.diponiblesID.remove(id);
        this.prisID.add(id);
    }
}
