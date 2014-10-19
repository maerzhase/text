package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import text.Analyzer;
import text.ValuableWord;


public class App extends PApplet
{
	private Analyzer			analyzer;

	List<ValuableWord>			theWords	= new ArrayList<ValuableWord>();
	HashMap<String, String>		theTexts	= new HashMap<String, String>();
	ArrayList<String>			enteties;

	HashMap<String, Integer>	kollokations_ranking;


	public void setup()
	{
		kollokations_ranking = new HashMap<String, Integer>();

		enteties = new ArrayList<String>();
		enteties.add("Polen");

		loadTexts();

		analyzer = new Analyzer();
		for (Entry<String, String> entry : theTexts.entrySet()) {
			// theWords.clear();
			println(entry.getValue());
			theWords = analyzer.analyzedSentence(entry.getValue());
		}
		println("================================");
		println(theWords);
		for (int i = 0; i < theWords.size(); i++) {
			String myWord = theWords.get(i).lemma;
			for (String myEntity : enteties) {
				if (!myWord.equalsIgnoreCase(myEntity) && i > 7 && i < theWords.size() - 7) {
					ArrayList<String> kollokation1 = new ArrayList<String>();
					ArrayList<String> kollokation2 = new ArrayList<String>();

					for (int j = 0; j < 5; j++) {

						kollokation2.add(theWords.get(i + j).lemma);
						if (kollokations_ranking.containsKey(theWords.get(i + j).lemma)) {
							int value = kollokations_ranking.get(theWords.get(i + j).lemma);
							value += 1;
							kollokations_ranking.put(theWords.get(i + j).lemma, value);
						} else {
							kollokations_ranking.put(theWords.get(i + j).lemma, 1);
						}

						try {
							kollokation1.add(theWords.get((i - 4) + j).lemma);

							if (kollokations_ranking.containsKey(theWords.get((i - 4) + j).lemma)) {
								int value = kollokations_ranking.get(theWords.get((i - 4) + j).lemma);
								value += 1;
								kollokations_ranking.put(theWords.get((i - 4) + j).lemma, value);
							} else {
								kollokations_ranking.put(theWords.get((i - 4) + j).lemma, 1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					println(myWord + " id: " + i);
					println(kollokation1 + " " + kollokation2);

				}
			}
		}

		Map<String, Integer> map = sortByValues(kollokations_ranking);
		println(map);

		exit();
	}


	void loadTexts()
	{
		File folder = new File("data/texts/");
		println(folder.getAbsolutePath());

		File[] abstracts = folder.listFiles();
		// println(folder.listFiles());

		for (File f : abstracts) {
			try {
				if (!f.isHidden()) {

					ArrayList<String> lines = new ArrayList<String>();

					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getPath()),
							"UTF8"));
					String line = reader.readLine();
					while (line != null) {
						lines.add(line);
						line = reader.readLine();
					}
					String theAbstract = "";
					for (String s : lines) {
						theAbstract += s + " ";
					}
					theTexts.put(f.getName(), theAbstract);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private static HashMap sortByValues(HashMap map)
	{
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2)
			{
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}


	public void draw()
	{

	}


	public static void main(String[] args)
	{
		PApplet.main(new String[] { "app.App" });
	}
}
