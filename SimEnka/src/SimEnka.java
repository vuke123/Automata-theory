import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class SimEnka {
	
	static void dopuniEpsilone(TreeSet<String> set1, TreeSet<String> set2,Map<String, HashMap<String, TreeSet<String>>> epsilonPrijelazi){
		Iterator<String> iter = set1.iterator();
		while(iter.hasNext()) {
			 String str = iter.next();
			 iter.remove(); 
			 set2.add(str);
			 if(epsilonPrijelazi.containsKey(str)) {
				 HashMap<String, TreeSet<String>> manjiPrijelazi = epsilonPrijelazi.get(str);
				 if(manjiPrijelazi.containsKey("$")) {
					 TreeSet<String> epsiloni = manjiPrijelazi.get("$"); 
					 for(String str2: epsiloni) {                 
						 if(!set2.contains(str2)) {
						 set1.add(str2);
						 dopuniEpsilone(set1, set2, epsilonPrijelazi); 
						 }
					 }
				 }
			 }
		 }  
	}

	public static void main(String args[]) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        Map<String, HashMap<String, TreeSet<String>>> mapaPrijelaza = new HashMap<String, HashMap<String, TreeSet<String>>>();
       
        String s = reader.readLine();  
        String [] ulazniNizovi = s.split("\\|");
        s = reader.readLine();
        String [] stanja = s.split(",");
        s = reader.readLine();
        String [] znakovi = s.split(",");
        s = reader.readLine();
        String [] prihvatljivaSt = s.split(",");
        s = reader.readLine();
        String pocetnoSt = s;
        
        s = reader.readLine(); 
        
        String [] prijelazi;
        String[] stanje_i_ulazni;  
        String[] novaStanja; 
        while (s!=null) {   
        	
        	prijelazi = s.split("->");
        	stanje_i_ulazni = prijelazi[0].split(","); 
        	novaStanja = prijelazi[1].split(",");
        	
        	TreeSet<String> skup = new TreeSet<>();
        	HashMap<String, TreeSet<String>> manjaMapa = new HashMap<String, TreeSet<String>>();
        	
            if(mapaPrijelaza.containsKey(stanje_i_ulazni[0])) {
            	manjaMapa = mapaPrijelaza.get(stanje_i_ulazni[0]);
                if(manjaMapa.containsKey(stanje_i_ulazni[1])) {
                	skup = manjaMapa.get(stanje_i_ulazni[1]); 
                	for(String novoStanje: novaStanja) {
                		skup.add(novoStanje);
                	}
                	manjaMapa.replace(stanje_i_ulazni[1], skup);
                } else {
                	for(String novoStanje: novaStanja) {
                		skup.add(novoStanje);
                	}
                    manjaMapa.put(stanje_i_ulazni[1], skup);
                }
                mapaPrijelaza.replace(stanje_i_ulazni[0], manjaMapa);
            } else {
            	for(String novoStanje: novaStanja) {
            		skup.add(novoStanje);
            	}
            	manjaMapa.put(stanje_i_ulazni[1], skup);
            	mapaPrijelaza.put(stanje_i_ulazni[0], manjaMapa);
            }
            s = reader.readLine();   
        }
        
        
        
        TreeSet<String> lebdeci = new TreeSet<String>(); 
    	TreeSet<String> pomocni = new TreeSet<String>();
    	LinkedHashSet<TreeSet<String>> svaStanja = new LinkedHashSet<TreeSet<String>>();
    	
    	lebdeci.add(pocetnoSt); 
        for(String ulazniNiz: ulazniNizovi) {          
        	StringBuilder sb=new StringBuilder();
        	dopuniEpsilone(lebdeci, pomocni, mapaPrijelaza); 
        	lebdeci.addAll(pomocni); 
        	for(String st:lebdeci) {
        		sb.append(st);
        		sb.append(",");
        	}
        	sb.deleteCharAt(sb.length()-1);
        	sb.append("|");
            pomocni = new TreeSet<String>();  
          
            boolean prviPut = true;
            boolean zastavica = false;
            for(String ulaz: ulazniNiz.split(",")) {
            	zastavica = false;
            	for(String stanje: lebdeci) {
            		if(mapaPrijelaza.containsKey(stanje)) {
            			HashMap<String, TreeSet<String>> manjiPrijelazi2 = mapaPrijelaza.get(stanje);
            			if(manjiPrijelazi2.containsKey(ulaz)) {
            				zastavica = true;
            				TreeSet<String> setStanja = manjiPrijelazi2.get(ulaz);
            				for(String i: setStanja) {
            					pomocni.add(i);
            				}
            			}
            			
            		} 
            	}
                if(prviPut) {
                	prviPut = false;
                }
            	if(zastavica == false && !prviPut) {
            		pomocni.add("#");
            		sb.append("#|");       		          		
            	} else {
            	lebdeci=pomocni;
            	pomocni = new TreeSet<String>();
                dopuniEpsilone(lebdeci, pomocni, mapaPrijelaza);
                lebdeci.addAll(pomocni);        
                for(String st:lebdeci) {
            		sb.append(st);
            		sb.append(",");
            	}
                sb.deleteCharAt(sb.length()-1);
            	sb.append("|");
            	}
            	pomocni = new TreeSet<String>();
             }
            
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
 
            lebdeci = new TreeSet<String>();
            lebdeci.add(pocetnoSt);        
                     
       }     
    }
}
















