package app;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;


public class TestApp extends PApplet
{

	ArrayList<String>			enteties;
	ArrayList<String>			kol;
	ArrayList<String>			text;

	HashMap<String, Integer>	test;


	public void setup()
	{
		test = new HashMap<String, Integer>();

		text = new ArrayList<String>();
		kol = new ArrayList<String>();
		enteties = new ArrayList<String>();
		enteties.add("Polen");

		String[] temp_text = loadStrings("text.txt");
		for (int i = 0; i < temp_text.length; i++) {
			String[] splitted_text = split(temp_text[i], ' ');
			for (int j = 0; j < splitted_text.length; j++) {
				String cleanString = Normalizer.normalize(splitted_text[j], Normalizer.Form.NFD)
						.replaceAll("[^\\p{ASCII}]", "").replaceAll("[^a-zA-Z0-9]+", "");
				text.add(cleanString);
			}
		}

		for (int i = 0; i < text.size(); i++) {
			String myWord = text.get(i);
			for (String myEntity : enteties) {
				if (myWord.equalsIgnoreCase(myEntity)) {
					ArrayList<String> kollokation1 = new ArrayList<String>();
					ArrayList<String> kollokation2 = new ArrayList<String>();

					for (int j = 0; j < 5; j++) {

						kollokation2.add(text.get(i + j));
						if (test.containsKey(text.get(i + j))) {
							int value = test.get(text.get(i + j));
							value += 1;
							test.put(text.get(i + j), value);
						} else {
							test.put(text.get(i + j), 1);
						}

						try {
							kollokation1.add(text.get((i - 4) + j));

							if (test.containsKey(text.get((i - 4) + j))) {
								int value = test.get(text.get((i - 4) + j));
								value += 1;
								test.put(text.get((i - 4) + j), value);
							} else {
								test.put(text.get((i - 4) + j), 1);
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
		Map<String, Integer> map = sortByValues(test);
		println(map);
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


	private void checkKollokation()
	{

	}


	public void draw()
	{

	}


	public static void main(String[] args)
	{
		PApplet.main(new String[] { "app.TestApp" });

	}
}
