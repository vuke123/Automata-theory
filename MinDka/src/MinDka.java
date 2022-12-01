import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class MinDka {
	
	static void makniNedohvatljivaSt(ArrayList<String> dohvatljivaSt,
			Map<String, HashMap<String, String>> mapaPrijelaza, String stanje) {
		if (dohvatljivaSt.contains(stanje))
			return;
		dohvatljivaSt.add(stanje);
		for (var entry : (mapaPrijelaza.get(stanje)).entrySet()) { 
			String string1;
			string1 = entry.getValue();
			makniNedohvatljivaSt(dohvatljivaSt, mapaPrijelaza, string1);
			}
	}


	public static void main(String args[]) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		Map<String, HashMap<String, String>> mapaPrijelaza = new HashMap<String, HashMap<String, String>>();

		String s = reader.readLine();
		String[] svaSt = s.split(",");
		s = reader.readLine();
		String[] ulazniZn = s.split(",");
		s = reader.readLine();
		String[] poljePrihvSt = s.split(",");
		s = reader.readLine();
		String pocetnoSt = s;

		s = reader.readLine();

		String[] prijelazi;
		String[] stanje_i_ulazni;
		String novoStanje;
		while (s!=null) {

			prijelazi = s.split("->");
			stanje_i_ulazni = prijelazi[0].split(",");
			novoStanje = prijelazi[1];

			HashMap<String, String> manjaMapa = new HashMap<String, String>();

			if (mapaPrijelaza.containsKey(stanje_i_ulazni[0])) {
				manjaMapa = mapaPrijelaza.get(stanje_i_ulazni[0]);
				manjaMapa.put(stanje_i_ulazni[1], novoStanje);
				mapaPrijelaza.replace(stanje_i_ulazni[0], manjaMapa);
			} else {
				manjaMapa.put(stanje_i_ulazni[1], novoStanje);
				mapaPrijelaza.put(stanje_i_ulazni[0],manjaMapa);
			}
			s = reader.readLine();
		}

		// prvo maknut nedohvatljiva
		ArrayList<String> dohvatljivaSt = new ArrayList<String>();
		makniNedohvatljivaSt(dohvatljivaSt, mapaPrijelaza, pocetnoSt);
		Collections.sort(dohvatljivaSt);
		TreeSet<String> prihvSt = new TreeSet<>();
		for (String x : poljePrihvSt) {
			if (dohvatljivaSt.contains(x)) {
				prihvSt.add(x);
			}
		}

		// treci algoritam

		// tablicu napravit
		Boolean[][] tablica = new Boolean[dohvatljivaSt.size()][dohvatljivaSt.size()];
		for (int i = 0; i < dohvatljivaSt.size(); i++) {
			for (int j = 0; j < dohvatljivaSt.size(); j++) {
				if (i > j) {
					if ((prihvSt.contains(dohvatljivaSt.get(i)) && !prihvSt.contains(dohvatljivaSt.get(j)))
							|| (!prihvSt.contains(dohvatljivaSt.get(i)) && prihvSt.contains(dohvatljivaSt.get(j)))) {
						tablica[i][j] = true;
					} else {
						tablica[i][j] = false;
					}
				}
				else {
					tablica[i][j] = false;
				}
			}
		}
		// rjesili smo stanja suprotnih "prihvacanja"

		HashMap<Pair<String, String>, ArrayList<Pair<String, String>>> listaParova = new HashMap<Pair<String, String>, ArrayList<Pair<String, String>>>();

		for (int i = 0; i < dohvatljivaSt.size(); i++) {
			for (int j = 0; j < dohvatljivaSt.size(); j++) {
				if (!tablica[i][j] && i > j) {
					for (String x : ulazniZn) {
						String st1;
						String st2;
						st1 = ((mapaPrijelaza.get(dohvatljivaSt.get(i))).get(x));
						st2 = ((mapaPrijelaza.get(dohvatljivaSt.get(j))).get(x));
						
						int ind1 = dohvatljivaSt.indexOf(st1);
						int ind2 = dohvatljivaSt.indexOf(st2);
						
						if(ind1 < ind2) {
							int swapA; 
							String swapB = "";
							swapA = ind1;
							ind1 = ind2;
							ind2 = swapA;
							swapB = st1;
							st1 = st2;
							st2 = swapB;
						}
						if (tablica[ind1][ind2]) {
							tablica[i][j] = true;
							Pair<String, String> par = Pair.of(dohvatljivaSt.get(i), dohvatljivaSt.get(j));
							if (listaParova.containsKey(par)) {
								for (Pair<String, String> var : listaParova.get(par)) {
									tablica[dohvatljivaSt.indexOf(var.first)][dohvatljivaSt.indexOf(var.second)] = true;
								}
								listaParova.remove(par);
							}

						} else {
							if (ind1 != ind2) {
								Pair<String, String> par = Pair.of(st1, st2);
								Pair<String, String> povezaniPar = Pair.of(dohvatljivaSt.get(i), dohvatljivaSt.get(j));
								if (listaParova.containsKey(par)) {
									(listaParova.get(par)).add(povezaniPar);
								} else {
									ArrayList<Pair<String, String>> lista = new ArrayList<>();
									lista.add(povezaniPar);
									listaParova.put(par, lista);
								}
							}
						}
					}
				}

			}
		}

		// makni nepotrebna istovjetna
		ArrayList<Integer> indeksi = new ArrayList<Integer>();

		for (int i = 0; i < dohvatljivaSt.size(); i++) {
			for (int j = 0; j < dohvatljivaSt.size(); j++) {
				if (i > j) {
					if (!tablica[i][j]) {
						for(var prodi1: mapaPrijelaza.entrySet()) {
							for(var prodi2: prodi1.getValue().entrySet()) {
								if(prodi2.getValue() == dohvatljivaSt.get(i)) {
									prodi2.setValue(dohvatljivaSt.get(j));
								}
							}
						}
						indeksi.add(i);
					}
				}
			}
		}
		ArrayList<String> novaDohvatljivaSt = new ArrayList<String>();
       for(String nova: dohvatljivaSt) {
    	   if(!indeksi.contains(dohvatljivaSt.indexOf(nova))) {
    		   novaDohvatljivaSt.add(nova);
    	   }
       }
		
		
	 StringBuilder preostalaSt=new StringBuilder();
	 for(String st: novaDohvatljivaSt) {
		 preostalaSt.append(st); 
		 preostalaSt.append(",");
	 }
	 preostalaSt.deleteCharAt(preostalaSt.length() - 1);
	 System.out.println(preostalaSt.toString());
	 
	 StringBuilder abeceda=new StringBuilder();
	 for(String st: ulazniZn) {
		 abeceda.append(st); 
		 abeceda.append(",");
	 }
	 abeceda.deleteCharAt(abeceda.length() - 1);
	 System.out.println(abeceda.toString());
	 
	 StringBuilder prihvacena = new StringBuilder();
	 for(String st: prihvSt) {
		 if(novaDohvatljivaSt.contains(st)) {
			 prihvacena.append(st);
			 prihvacena.append(",");
		 }
	 }
	 if(prihvacena.length() > 0) {
	 prihvacena.deleteCharAt(prihvacena.length() - 1);
	 }
	 System.out.println(prihvacena.toString());
	 
	 System.out.println(pocetnoSt);
	 
	 StringBuilder prijelaz = new StringBuilder();
	 for (var entry1 : mapaPrijelaza.entrySet()) {
		 if(novaDohvatljivaSt.contains(entry1.getKey())) {
	       prijelaz.append(entry1.getKey());
	  	 for (var entry2 : mapaPrijelaza.get(entry1.getKey()).entrySet()) {
	  		 int dlt = prijelaz.length();
	  		 prijelaz.append(",");
	  		 prijelaz.append(entry2.getKey());
	  		 prijelaz.append("->");
	  		 prijelaz.append(entry2.getValue());
	  		 System.out.println(prijelaz.toString());
	  		 prijelaz.delete(dlt, prijelaz.length());
	  	 }
	  	 prijelaz.delete(0, prijelaz.length());
		 }
	}
	}
}

class Pair<U, V> {
	public final U first;
	public final V second;

	private Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object o) {

		if ((this.first == ((Pair) o).first && this.second == ((Pair) o).second)
				|| (this.second == ((Pair) o).first && this.first == ((Pair) o).second)) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Pair<?, ?> pair = (Pair<?, ?>) o;

		if (!first.equals(pair.first)) {
			return false;
		}
		return second.equals(pair.second);
	}

	@Override
	public int hashCode() {
		return 31 * first.hashCode() + second.hashCode();
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public static <U, V> Pair<U, V> of(U a, V b) {
		return new Pair<>(a, b);
	}

}
