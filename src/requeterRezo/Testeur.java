package requeterRezo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Testeur {

	public static void main(String[] args) throws IOException, MalformedURLException, InterruptedException {
		RequeterRezo rezo = new RequeterRezo();  
		Mot c= rezo.requete("_SW");
		//ArrayList<Mot> res = Testeur.getMots(rezo,"pamplemousse",17);

		/*
		for(Mot m: res){
			System.out.println(m);
		}
		//System.out.print(m);
		//}
		 * */
		/*
		for(Mot m: plusBasIs_A("canard",new ArrayList<>())) {
			System.out.println(m.nom);
	}
		 */
		System.out.println("====");


		for(Mot m: chaineIs_A("cocotier")) {
			System.out.println(m.nom);
		}


		//plusHautIs_A("ravioli",new ArrayList<>());
		//RequeterRezo rezo = new RequeterRezo();       
		//Mot mot = rezo.requete("pomme",6);
		/*
		ArrayList<String> test= new ArrayList<String>();
		test.add("1");
		test.add("2");
		test.add("3");
		Collections.reverse(test);
		for(String m: test) {
			System.out.println(m);
		}
		 */

	}


	public static ArrayList<Mot> getMots(RequeterRezo rezo, String nom, int typeRelation) throws IOException, MalformedURLException, InterruptedException{
		ArrayList<Mot> pos=new ArrayList<>();
		Mot motPrincipal = rezo.requete(nom,typeRelation,Filtre.FiltreRelationsEntrantes);
		Mot motPos;
		String div[];
		String nomPos;
		for (ArrayList<Terme> termes : motPrincipal.getRelations_sortantes().values()) {
			for(Terme terme: termes) {				
				//System.out.println(terme.getTerme());
				//        		nom=terme.getTerme().substring(1,terme.getTerme().length()-1);
				//System.out.println(nom);
				pos.add(rezo.requete(terme.getNom()));
				//        		System.out.println(motPos);
				//        		pos.add(motPos);
			}
		}
		//rezo.sauvegarder();  

		return pos;
	}



	// dans les relations sortantes
	public static ArrayList<Mot> plusHautIs_A(String nom, ArrayList<Mot> chaine) throws IOException, MalformedURLException, InterruptedException{
		RequeterRezo rezo = new RequeterRezo();       
		Mot mot = rezo.requete(nom,6);
		chaine.add(mot);
		if (mot.getRelations_sortantes().size()==0) {
			return chaine;
		}
		else {
			int test=0;
			//Méthode à refaire
			ArrayList<Terme> termes = mot.getRelations_sortantes().values().iterator().next();
			int i = 0;
			boolean trouve = false;
			while(!trouve && i < termes.size()) {
				Terme terme = termes.get(i);
				if(terme.getPoids()> 24) {
					System.out.println(terme.getNom());
					return plusHautIs_A(terme.getNom(), chaine);
				}
				++i;
			}
//			for (ArrayList<Terme> termes : mot.getRelations_sortantes().values()) {
//				if(test==0) {
//					test++;
//					System.out.println(termes.get(0).terme);
//					return plusHautIs_A(termes.get(0).terme, chaine);
//				}
//			}
		}
		return new ArrayList<>();
	}

	public static ArrayList<Mot> plusBasIs_A(String nom, ArrayList<Mot> chaine) throws IOException, MalformedURLException, InterruptedException{
		RequeterRezo rezo = new RequeterRezo();       
		Mot mot = rezo.requete(nom,6);
		chaine.add(mot);
		if (mot.getRelations_entrantes().size()==0) {
			//System.out.println(mot);
			return chaine;
		}
		else {
			int test=0;
			//Méthode à refaire
			ArrayList<Terme> termes = mot.getRelations_entrantes().values().iterator().next();
			int i = 0;
			boolean trouve = false;
			Terme terme;
			while(!trouve && i < termes.size()) {
				terme = termes.get(i);
				if(terme.getPoids()>24) {
					trouve = true;
					System.out.println(terme.getNom());
					return plusBasIs_A(terme.getNom(), chaine);
				}
				++i;
			}
			
//			int i = 0;
//			boolean trouve = false;
//			for (ArrayList<Terme> termes : mot.getRelations_entrantes().values()) {
//				//terme.getPoids() > 25
//				if(test==0) {
//					test++;
//					r = random.nextInt(termes.size());
//					System.out.println(termes.get(r).terme);
//					return plusBasIs_A(termes.get(r).terme, chaine);
//				}
//			}
		}
		return null;
	}

	public static ArrayList<Mot> chaineIs_A(String nom) throws IOException, MalformedURLException, InterruptedException{
		ArrayList res=new ArrayList<>();
		ArrayList<Mot> tmp= new ArrayList<>();
		tmp=plusBasIs_A(nom, new ArrayList<>());
		Collections.reverse(tmp);
		res.addAll(tmp);
		res.addAll(plusHautIs_A(nom,new ArrayList<>()));
		return res;

	}
	/*
	public static ArrayList<Mot> plusHautIs_A2(String nom) throws IOException, MalformedURLException, InterruptedException{
		RequeterRezo rezo = new RequeterRezo();       
		Mot mot = rezo.requete(nom,6);

		if (mot.getRelations_sortantes().size()==0) {
			//System.out.println(mot);
			return new ArrayList<>();
		}
		else {
			int test=0;
			for (ArrayList<Terme> termes : mot.getRelations_sortantes().values()) {
				if(test==0) {
					test++;
					//System.out.println(termes.get(0).terme);
				  plusHautIs_A2(termes.get(0).terme).add(mot);
				}
				}
		}
		return new ArrayList<>();
	}
	 */


}
