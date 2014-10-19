package text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class Ranking
{

	public List<ValuableWord>				all_words;
	public HashMap<String, ValuableWord>	word_map;

	public HashMap<ValuableWord, Integer>	ranking;
	private int								CHECKSUM	= 0;

	private HashMap<String, String>			rhymes;

	public Konjugator						kon;


	public Ranking(Konjugator _kon)
	{
		all_words = new ArrayList<ValuableWord>();
		word_map = new HashMap<String, ValuableWord>();
		ranking = new HashMap<ValuableWord, Integer>();
		rhymes = new HashMap<String, String>();
		kon = _kon;
	}


	/**
	 * add words to the ranking word list
	 * 
	 * @example
	 * @param words
	 */

	public void addWords(List<ValuableWord> _words)
	{
		all_words.addAll(_words);
	}

	String	phonetic_2, phonetic2_2;


	/**
	 * generate the ranking
	 * 
	 * @example
	 */

	public void makeRanking()
	{
		System.out.println("");
		System.out.println("Ranking: calculating ranking");
		System.out.println("*******************************");

		// for each word of all the words we added
		for (ValuableWord vw : all_words) {
			// if the word already exists in our word map - increment its count
			if (word_map.containsKey(vw.text)) {
				ValuableWord temp_vw = word_map.get(vw.text);
				temp_vw.incrementCount();
			}
			// if the word doesn't exist add to our word map
			else {
				word_map.put(vw.text, vw);
			}
		}

		// for each word in our word map
		for (ValuableWord vw : word_map.values()) {

			// add the word with it's count in our ranking map
			ranking.put(vw, vw.count);
		}

		// sort our ranking according to the 'word count'
		ranking = sortHashMapByValues(ranking);

		// DO SOMETHING WITH OUR RANKING
		for (ValuableWord vw : ranking.keySet()) {
			String phonetic = vw.ending;
			int subRhyme = 0;
			int adjRhyme = 0;
			int verRhyme = 0;
			ArrayList<ValuableWord> rhymegroup = new ArrayList<ValuableWord>();

			boolean has_rhyme = false;
			for (ValuableWord vw2 : ranking.keySet()) {

				String phonetic2 = vw2.ending;
				if (!vw.text.equalsIgnoreCase(vw2.text)) {

					// System.out.println(vw.text + " " + vw2.text);
					if (phonetic.equalsIgnoreCase(phonetic2) && !phonetic.equalsIgnoreCase("")
							&& !rhymes.containsKey(vw.text)) {
						System.out.println("REIM : " + "(" + vw.count + ") " + vw.text + " [" + phonetic + "] " + " : "
								+ vw2.text + " [" + phonetic2 + "] " + "(" + vw2.count + ")" + " " + vw.lemma + " "
								+ vw2.lemma + " " + vw.pos + " " + vw2.pos);
						rhymes.put(vw2.text, phonetic);
						has_rhyme = true;
						rhymegroup.add(vw2);
						if (rhymegroup.contains(vw) == false) {
							rhymegroup.add(vw);
						}
						rhymegroup.add(vw);

						if (vw2.pos.equalsIgnoreCase("VER")) {
							verRhyme++;
						} else if (vw2.pos.equalsIgnoreCase("ADJ")) {
							adjRhyme++;
						} else if (vw2.pos.equalsIgnoreCase("SUB") || vw2.pos.equalsIgnoreCase("EIG")) {
							subRhyme++;
						}

					}
				}

			}

			if (has_rhyme) {
				if (vw.pos.equalsIgnoreCase("VER")) {
					verRhyme++;
				} else if (vw.pos.equalsIgnoreCase("ADJ")) {
					adjRhyme++;
				} else if (vw.pos.equalsIgnoreCase("SUB") || vw.pos.equalsIgnoreCase("EIG")) {
					subRhyme++;
				}
			}

			for (ValuableWord rhymes : rhymegroup) {
				rhymes.subRhyme = subRhyme;
				rhymes.adjRhyme = adjRhyme;
				rhymes.verRhyme = verRhyme;
				// System.out.println(rhymes.text + " " + rhymes.subRhyme);
			}
			rhymegroup.clear();

			if (vw.pos.equalsIgnoreCase("VER")) {
				kon.getNewVerb(vw.lemma, 0);

			}

			if (!rhymes.containsKey(vw.text) && has_rhyme) {
				rhymes.put(vw.text, phonetic);
				System.out.println("");

			}

			if (vw.count > 1) {
				// System.out.println(vw.prefix + " " + vw.text + " " + vw.suffix + " " + vw.count + " " + vw.kasus
				// + " RHYME ENDING: " + vw.ending);
			} else {
				// System.out.println(vw.text + " " + vw.count);
			}

			// count the words and check if we didn't
			CHECKSUM += vw.count;
		}

		for (ValuableWord word : ranking.keySet()) {
			// System.out.println(word.hasRhyme);
		}

		if (CHECKSUM - all_words.size() == 0) {
			System.out.println("*******************************");
			System.out.println("Ranking: successfully created");
			System.out.println("*******************************");
		} else {
			System.out.println("*ERROR*ERROR*ERROR*ERROR*ERROR*");
			System.out.println("Ranking: CHECKSUM INCORRECT " + CHECKSUM + " != " + all_words.size());
			System.out.println("*ERROR*ERROR*ERROR*ERROR*ERROR*");
		}
	}


	/**
	 * retrieve the current generated ranking
	 * 
	 * @example
	 * @return ranking
	 */

	public HashMap<String, String> getRhymes()
	{

		return rhymes;
	}


	/**
	 * retrieve the current generated ranking
	 * 
	 * @example
	 * @return ranking
	 */

	public HashMap<ValuableWord, Integer> getRanking()
	{
		return ranking;
	}


	/**
	 * the ranking HashMap according to its integer count
	 * 
	 * @example
	 * @parameter ranking
	 */

	public LinkedHashMap<ValuableWord, Integer> sortHashMapByValues(HashMap<ValuableWord, Integer> passedMap)
	{
		List<ValuableWord> mapKeys = new ArrayList<ValuableWord>(passedMap.keySet());
		List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
		Collections.sort(mapValues);
		// Collections.sort(mapKeys);

		LinkedHashMap<ValuableWord, Integer> sortedMap = new LinkedHashMap<ValuableWord, Integer>();

		Iterator<Integer> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator<ValuableWord> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((ValuableWord) key, (Integer) val);
					break;
				}

			}

		}
		return sortedMap;
	}
}
