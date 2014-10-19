package text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.German;


public class Analyzer
{
	JLanguageTool					langTool;

	List<ValuableWord>				all_words;
	HashMap<String, ValuableWord>	word_map;

	Hyphenator						hy;


	public Analyzer()
	{
		all_words = new ArrayList<ValuableWord>();
		word_map = new HashMap<String, ValuableWord>();

		hy = new Hyphenator();

		// load the hyphenator rule file
		try {
			hy.loadTable(new java.io.BufferedInputStream(new java.io.FileInputStream("data/hyphenator/dehypha.tex")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// load german language tool lexicon
		try {
			langTool = new JLanguageTool(new German());
			langTool.activateDefaultPatternRules();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * count the divisions of the word
	 * 
	 * @example
	 * @return analyzedSentenc
	 */

	public LinkedList<ValuableWord> analyzedSentence(String _sentence)
	{
		// Create a new temporary & empty list of words to return
		LinkedList<ValuableWord> words = new LinkedList<ValuableWord>();

		// Create an empty analyzed sentence object
		AnalyzedSentence sentence;

		try {
			// Get the analyzed sentence from the languatool
			sentence = langTool.getAnalyzedSentence(_sentence.replace("#", ""));

			// For each anaylzed word (AnalyzedTokenReadings)

			for (AnalyzedTokenReadings t : sentence.getTokensWithoutWhitespace()) {
				AnalyzedToken at = t.getAnalyzedToken(0);
				// Get the text of the word
				String txt = t.getToken();

				System.out.println("Analyzer: " + txt + " [" + at.getPOSTag() + "]");

				// If the word has PartOfSpeech-Tags
				if (t.isTagged()) {

					// Get the base POS-Tag
					String pos_tag = at.getPOSTag().split(":")[0];

					// If the POS-Tag is a valuable word tag
					if (isValuableWord(pos_tag)) {
						// Create a new word
						ValuableWord new_word = new ValuableWord(txt, getWortgroup(pos_tag), getKasus(at.getPOSTag()),
								countHyphen(txt), getDivisions(txt), getPhonetics(txt));
						if (at.getPOSTag().split(":").length > 2) {
							switch (pos_tag) {
								case "VER":
									new_word.setNum(at.getPOSTag().split(":")[2]);
									new_word.setPers(at.getPOSTag().split(":")[1]);

									break;

								case "SUB":
									new_word.setGen(at.getPOSTag().split(":")[3]);
									new_word.setNum(at.getPOSTag().split(":")[2]);
									new_word.setPersTag("3" + at.getPOSTag().split(":")[2]);
									if (at.getPOSTag().split(":")[2].equalsIgnoreCase("SIN")) {
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("MAS")) {
											new_word.setPrefix("der");
										}
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("NEU")) {
											new_word.setPrefix("das");
										}
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("FEM")) {
											new_word.setPrefix("die");
										}
									}
									if (at.getPOSTag().split(":")[2].equalsIgnoreCase("PLU")) {
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("MAS")) {
											new_word.setPrefix("die");
										}
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("NEU")) {
											new_word.setPrefix("die");
										}
										if (at.getPOSTag().split(":")[3].equalsIgnoreCase("FEM")) {
											new_word.setPrefix("die");
										}
									}
									break;

								case "EIG":
									new_word.setGen(at.getPOSTag().split(":")[3]);
									new_word.setNum(at.getPOSTag().split(":")[2]);
									break;
							}
						}

						if (at.getLemma() != null) {
							new_word.setLemma(at.getLemma().replace("[", "").replace("]", ""));
						}

						if (getLastWord() != null) {
							// If the POS-Tag is 'SUB'
							if (pos_tag.equalsIgnoreCase("SUB")) {
								// If last word is an article add article
								if (getLastWord().pos.equalsIgnoreCase("ART")) {
									// new_word.setPrefix(getLastWord().text);
								}
							}
							// If the POS-Tag is 'VER' or 'PA2'
							if (pos_tag.equalsIgnoreCase("VER") || pos_tag.equalsIgnoreCase("PA2")) {
								// System.out.println("____________ I AM VERB " + getLastWord().pos);
								// If the last word is an substantive add verb to substantive
								if (getLastWord().pos.equalsIgnoreCase("SUB")) {
									getLastWord().setSuffix(new_word.text);
									// System.out.println("____________" + getLastWord().text + " " + new_word.text);
								}
							}
						}
						// Set the ending of the word
						new_word.setEnding(getRhymeEnding(new_word.divisions));
						// Add word to our temporary word list
						words.add(new_word);
						// Add word to our overall word list
						all_words.add(new_word);
						// word_map.put(txt, new ValuableWord(txt, pos));
					}
				}
				// if word contains '#' its a hashtag
				if (txt.contains("#")) {
					String pos = "HASHTAG";
					words.add(new ValuableWord(txt, pos, "", countHyphen(txt), getDivisions(txt), getPhonetics(txt)));
					all_words
							.add(new ValuableWord(txt, pos, "", countHyphen(txt), getDivisions(txt), getPhonetics(txt)));

					// word_map.put(txt, new ValuableWord(txt, pos));

				}
				// if word contains '@' its a user
				if (txt.contains("@")) {
					String pos = "USER";
					words.add(new ValuableWord(txt, pos, "", countHyphen(txt), getDivisions(txt), getPhonetics(txt)));
					all_words
							.add(new ValuableWord(txt, pos, "", countHyphen(txt), getDivisions(txt), getPhonetics(txt)));

					// word_map.put(txt, new ValuableWord(txt, pos));

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println();
		// System.out.println("analyser: finished analysing");
		// System.out.println();

		return words;
	}


	/**
	 * count the devisions of the word
	 * 
	 * @example
	 * @param word
	 */

	private int countHyphen(String _word)
	{
		int counter = getDivisions(_word).length;
		return counter;
	}


	/**
	 * get the divisions of the word
	 * 
	 * @example
	 * @param word
	 */

	private String[] getDivisions(String _word)
	{
		String word = hy.hyphenate(_word);

		String[] temp_divisions = word.split("-");
		String[] divisions = null;

		if (temp_divisions.length > 1) {
			divisions = new String[temp_divisions.length - 1];
			if (temp_divisions[temp_divisions.length - 1].length() == 1) {
				for (int i = 0; i < divisions.length; i++) {
					if (i == temp_divisions.length - 2) {
						divisions[i] = temp_divisions[i] + temp_divisions[i + 1];
					} else {
						divisions[i] = temp_divisions[i];
					}
				}
			} else {
				divisions = temp_divisions;
			}
		} else {
			divisions = new String[temp_divisions.length];

			divisions = temp_divisions;
		}

		// System.out.println(word + " has " + counter + " syllables");

		return divisions;
	}

	String[]	exceptions	= { "en", "ik", "er", "el", "ern", "on", "et", "is", "ung", "ich", "ig", "es", "elt", "em",
			"ens", "am", "eln" };

	char[]		vocale		= { 'a', 'e', 'i', 'o', 'u', 'ö', 'ü', 'ä' };


	/**
	 * get the ending of the word that is used for rhyming algorithm
	 * 
	 * @example
	 * @param divisions
	 */

	private String getRhymeEnding(String[] divisions)
	{
		String rhyme_ending = "";

		for (int i = 0; i < divisions[divisions.length - 1].length() - 1; i++) {
			if (hasChar(divisions[divisions.length - 1].charAt(i), vocale)) {

				for (int j = i; j < divisions[divisions.length - 1].length(); j++) {
					rhyme_ending += divisions[divisions.length - 1].charAt(j);
				}

				if (hasString(rhyme_ending, exceptions) && divisions.length > 1) {

					for (int l = 0; l < divisions[divisions.length - 2].length(); l++) {
						// System.out.println(" ______ " + divisions[divisions.length - 2].charAt(0));
						if (hasChar(divisions[divisions.length - 2].charAt(l), vocale)) {
							String addition = "";
							for (int m = l; m < divisions[divisions.length - 2].length(); m++) {
								addition += divisions[divisions.length - 2].charAt(m);
							}
							// System.out.println("#####################" + addition + divisions[divisions.length - 1]);
							return addition + divisions[divisions.length - 1];

						}
					}
				} else {

					return rhyme_ending;
				}

			}
		}

		return rhyme_ending;

	}


	/**
	 * get the phonetics of the word
	 * 
	 * @example
	 * @param word
	 */

	private String[] getPhonetics(String _word)
	{
		String word = hy.hyphenate(_word);

		String[] divisions = word.split("-");
		String[] phonetics = new String[divisions.length];
		// for (int i = 0; i < divisions.length; i++) {
		// phonetics[0] = meta.doubleMetaphone(word);
		phonetics[0] = "";
		// }

		// System.out.println(word + " has " + counter + " syllables");

		return phonetics;
	}


	/**
	 * check if word is a valuable word
	 * 
	 * @example
	 * @param partofspeech
	 */

	private boolean isValuableWord(String _pos)
	{
		String pos = _pos;
		boolean isValuableWord = false;
		switch (pos) {
			case "SUB":
				isValuableWord = true;
				break;
			case "EIG":
				isValuableWord = true;
				break;
			case "ADJ":
				isValuableWord = true;
				break;
			case "VER":
				isValuableWord = true;
				break;
			case "PRP":
				isValuableWord = true;
				break;
			case "PRO":
				isValuableWord = true;
				break;
			case "ART":
				isValuableWord = true;
				break;
			case "ADV":
				isValuableWord = true;
				break;
			case "PA2":
				isValuableWord = true;
				break;
			default:
				isValuableWord = false;
				break;

		}
		return isValuableWord;
	}


	/**
	 * get the last word in the list
	 * 
	 * @example
	 */

	private ValuableWord getLastWord()
	{

		ValuableWord last_word;

		if (all_words.size() > 0) {
			last_word = all_words.get(all_words.size() - 1);
			switch (last_word.pos) {
				case "ART":
					last_word = last_word;
					break;
				case "PRP":
					last_word = last_word;
					break;
				case "SUB":
					last_word = last_word;
				default:
					break;
			}
			return last_word;

		} else {
			return null;
		}

	}


	/**
	 * get the kasus of the word
	 * 
	 * @example
	 * @param partofspeech
	 */

	private String getKasus(String _pos)
	{
		String kasus = null;
		if (_pos.split(":").length > 1 && _pos != null) {
			String wrtgrp = _pos.split(":")[0];

			switch (wrtgrp) {
				case "SUB":
					kasus = _pos.split(":")[1];
					break;
				case "ADJ":
					kasus = _pos.split(":")[1];
					break;
				case "VER":
					kasus = _pos.split(":")[1];
					break;
				case "PRP":
					kasus = _pos.split(":")[1];
					break;
				case "ART":
					kasus = _pos.split(":")[2];
					break;
				case "ADV":
					kasus = _pos.split(":")[1];
					break;
				case "PA2":
					kasus = _pos.split(":")[1];
					break;

				default:
					kasus = "";
					break;

			}
		}
		return kasus;

	}


	/**
	 * get the wortgroup
	 * 
	 * @example
	 * @param partofspeech
	 */

	private String getWortgroup(String _pos)
	{
		String wrtgrp = _pos;
		String pos_tag = null;
		switch (wrtgrp) {
			case "SUB":
				pos_tag = wrtgrp;
				break;
			case "EIG":
				pos_tag = wrtgrp;
				break;
			case "ADJ":
				pos_tag = wrtgrp;
				break;
			case "VER":
				pos_tag = wrtgrp;
				break;
			case "PRP":
				pos_tag = wrtgrp;
				break;
			case "PRO":
				pos_tag = wrtgrp;
				break;
			case "ART":
				pos_tag = wrtgrp;
				break;
			case "ADV":
				pos_tag = wrtgrp;
				break;
			case "PA2":
				pos_tag = wrtgrp;
				break;
			default:
				pos_tag = null;
				break;

		}
		return pos_tag;
	}


	/**
	 * check if list contains char
	 * 
	 * @example
	 */

	private boolean hasChar(char c, char[] array)
	{
		for (char x : array) {
			if (x == c) {
				return true;
			}
		}
		return false;
	}


	/**
	 * check if list contains string
	 * 
	 * @example
	 */

	private boolean hasString(String s, String[] array)
	{
		for (String x : array) {
			if (x.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

}
