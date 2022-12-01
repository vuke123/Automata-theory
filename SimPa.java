package utrlab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimPa {

	public static void main(String args[]) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String s = reader.readLine();
		String[] ulazniNiz = s.split("\\|");

		s = reader.readLine();
		String[] skupStanja = s.split(",");

		s = reader.readLine();
		String[] ulazniZn = s.split(",");

		s = reader.readLine();
		String[] znakoviStoga = s.split(",");

		s = reader.readLine();
		String[] prihvSt = s.split(",");

		s = reader.readLine();
		String pocetnoStanje = s;
		s = reader.readLine();
		String pocetniStog = s;

		s = reader.readLine();

		HashMap<Triple<String, String, String>, Pair<String, String>> mapaPrijelaza = new HashMap<Triple<String, String, String>, Pair<String, String>>();

		while (s!=null) {
			String[] prviSplit = s.split("->");
			String[] lijeviDio = prviSplit[0].split(",");
			String[] desniDio = prviSplit[1].split(",");
			
			Triple<String, String, String> t = Triple.of(lijeviDio[0], lijeviDio[1], lijeviDio[2]);
			Pair<String, String> p = Pair.of(desniDio[0], desniDio[1]);
			mapaPrijelaza.put(t, p);
			s = reader.readLine();
		}
		// ucitali smo sve ulazne podatke
		int i = 0;
		int limit1 = ulazniNiz.length;
		while (i < limit1) {
			StringBuilder stog = new StringBuilder();
			stog.append(pocetniStog);
			String stanje = pocetnoStanje;
			StringBuilder sb = new StringBuilder();
			String znakovi[] = ulazniNiz[i].split(",");
			int j = 0;
			int limit2 = znakovi.length;
			boolean fail = false;
			while (stog != null && !fail && limit2 >= j) {
			
				sb.append(stanje);
				sb.append("#");
				sb.append(stog);
				sb.append("|");
				String temp = stog.substring(1);

				Triple<String, String, String> x;
				if (j < limit2) {
					x = Triple.of(stanje, znakovi[j], stog.substring(0, 1));
				} else {
					x = Triple.of(stanje, "$", stog.substring(0, 1));
				}
				while (mapaPrijelaza.get(x) == null && !fail && j != limit2 ) {
					temp = stog.substring(1);
					if(stog.length()==0) {
			        	stog.append("oidhsahuihasdoij");
			        }
					String ulazniKzna = x.getSecond();
					x = Triple.of(stanje, "$", stog.substring(0,1));
					Pair<String, String> y = mapaPrijelaza.get(x);
					if (y == null) {
						fail = true;
					} else {
						stanje = y.first;
						x = Triple.of(y.first, ulazniKzna, y.second.charAt(0)+"");
						stog.delete(0, stog.length());
						if(!(y.second.equals("$"))) {
							stog.append(y.second);
						}
						stog.append(temp);
						sb.append(y.first+ "#" + stog + "|");
					}
				}
				temp = stog.substring(1);
				if (fail) {
					sb.append("fail|");
					stanje = "fail";
				} else {
					if (j != limit2) {					
						Pair<String, String> value = mapaPrijelaza.get(x);
						stog.delete(0, stog.length());
						if (!(value.second.equals("$"))) {
							stog.append(value.second);
						}
						stog.append(temp);
						stanje = value.first;
					}
					j++;
				}
			}
			boolean zastavica = false;
			for (String prihv : prihvSt) {
				if (prihv.equals(stanje)) {
					sb.append("1");
					zastavica = true;
				}
			}
			if (!zastavica) {
				sb.append("0");
			}
			System.out.println(sb.toString());
			i++;
		}

	}

}

class Triple<T, U, V> {
	private final T first;
	private final U second;
	private final V third;

	public Triple(T first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}

	public T getFirst() {
		return first;
	}

	public U getSecond() {
		return second;
	}

	public V getThird() {
		return third;
	}
	public static <T, U, V> Triple<T, U, V> of(T a, U b, V c) {
		return new Triple<>(a, b, c);
	}
}

class Pair<U, V> {
	public final U first;
	public final V second;

	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object o) {

		if ((this.first == ((Pair<?, ?>) o).first && this.second == ((Pair<?, ?>) o).second)
				|| (this.second == ((Pair<?, ?>) o).first && this.first == ((Pair<?, ?>) o).second)) {
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

	public U getFirst() {
		return first;
	}

	public V getSecond() {
		return second;
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