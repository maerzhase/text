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
import java.util.Random;

import processing.core.PApplet;
import text.Analyzer;
import text.ValuableWord;


public class MarkowApp extends PApplet
{

	private Analyzer								analyzer;

	HashMap<String, String>							theTexts	= new HashMap<String, String>();
	HashMap<String, LinkedHashMap<String, Integer>>	theChain	= new HashMap<String, LinkedHashMap<String, Integer>>();
	LinkedList<ValuableWord>						theWords	= new LinkedList<ValuableWord>();
	List<String>									blacklist	= new ArrayList<String>();


	public void setup()
	{
		loadTexts();
		analyzer = new Analyzer();

		for (Entry<String, String> entry : theTexts.entrySet()) {
			theWords = analyzer.analyzedSentence(entry.getValue());
		}

		for (int i = 0; i < theWords.size(); i++) {
			String myWord = theWords.get(i).lemma;
			//println(theWords.get(i).pos);
			if (!theChain.containsKey(myWord)) {
				LinkedHashMap<String, Integer> emptyList = new LinkedHashMap<String, Integer>();
				theChain.put(myWord, emptyList);
			}

			if (i < theWords.size() - 1) {
				HashMap<String, Integer> existingList = theChain.get(myWord);
				String nextWord = theWords.get(i + 1).lemma;

				if (existingList.containsKey(nextWord)) {
					Integer value = existingList.get(nextWord);
					existingList.put(nextWord, value + 1);
				} else {
					existingList.put(nextWord, 1);
				}
			}
		}

		for (Entry<String, LinkedHashMap<String, Integer>> entry : theChain.entrySet()) {
			LinkedHashMap<String, Integer> existingList = entry.getValue();
			String theKey = entry.getKey();
			Map<String, Integer> newMap = sortByValues(existingList);
			theChain.put(theKey, (LinkedHashMap<String, Integer>) newMap);
		}

		Random random = new Random();
		List<String> keys = new ArrayList<String>(theChain.keySet());
		// String randomKey = keys.get(random.nextInt(keys.size()));

		String randomKey = "Heinrich";
		// println(randomKey);

		for (int i = 0; i < 100; i++) {
			LinkedHashMap<String, Integer> existingList = theChain.get(randomKey);
			LinkedList<String> ranking = new LinkedList<String>();
			for (String s : existingList.keySet()) {
				ranking.add(s);
			}

			int cnt = 1;
			String markowWord = ranking.get(ranking.size() - cnt);

			while (blacklist.contains(markowWord) && ranking.size() - cnt > 0) {
				cnt++;
				markowWord = ranking.get(ranking.size() - cnt);
			}
			Integer value = existingList.get(markowWord);
			println(markowWord + " (" + value + ") " + existingList);
			randomKey = markowWord;
			blacklist.add(randomKey);
			// existingList.remove(markowWord);
		}

		println("========================================");

		exit();
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


	public static void main(String[] args)
	{
		PApplet.main(new String[] { "app.MarkowApp" });
	}
}
