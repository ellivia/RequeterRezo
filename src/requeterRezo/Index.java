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

/**
 * Index des mots rencontrés mais pas présents dans le cache. Met en relation un
 * mot avec un ensemble d'informations ({@link  IndexInfo}).
 *
 * @author Jimmy Benoits
 */
@SuppressWarnings("serial")
public class Index extends HashMap<String, IndexInfo> {

    /**
     * Constructeur de l'index dont la taille est initialisé grâce au nombre
     * maximal d'éléments du cache
     *
     * @param tailleMax Nombre maximum d'entrées     
     */
    protected Index(int tailleMax) {
        super(tailleMax * (4 / 3));
    }

    /**
     * Chargement de l'index des rencontrés mais non mis en cache à partir d'un
     * fichier créé lors d'une session précédente.
     *
     * @param fichier Chemin vers le fichier "indexAttente" de la session
     * précédente.
     * @param tailleMax Nombre maximum d'entrées
     * @return Un index instancié grâce au fichier.
     * @throws IOException
     * @throws ParseException
     */
    protected static Index chargerIndex(String fichier, int tailleMax) throws IOException, ParseException {
        Index index = new Index(tailleMax);
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String line;
            String[] tokens;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aaa");
            while ((line = reader.readLine()) != null) {
                tokens = line.split(";");
                index.put(
                        tokens[0],
                        new IndexInfo(Integer.parseInt(tokens[1]), df.parse(tokens[2]))
                );
            }
        }
        return index;
    }

    /**
     * Sauvegarde dans un fichier l'index des mots en attentes.
     *
     * @param index Index en attente.
     * @param chemin Chemin du fichier à écrire.
     * @throws IOException
     */
    protected static void sauvegarderIndex(Index index, String chemin) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin + "_tmp"))) {
            for (Entry<String, IndexInfo> entry : index.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue().toString());
                writer.newLine();
            }
            File file = new File(chemin);
            if (file.exists()) {
                file.delete();
            }
        }
        File _old = new File(chemin + "_tmp");
        File _new = new File(chemin);
        _old.renameTo(_new);
        _new.setReadOnly();
    }

    /**
     * Supprime un mot de l'index.
     *
     * @param mot Mot à supprimer.
     */
    protected void supprimer(String mot) {
        this.remove(mot);
    }

}
