package text;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class ValuableWord
{
	public String	text;
	public String	pos;
	public int		hyphen;
	public int		count;
	public String[]	divisions;
	public String[]	phonetics;
	public String	prefix;
	public String	suffix;
	public String	kasus;
	public String	ending;
	public String	lemma;

	public String	pers_tag;

	public String	num;
	// verb related variables
	public String	pers;

	// noun related variables
	public String	gen;

	public int		hasRhyme	= 0;

	public int		subRhyme	= 0;
	public int		adjRhyme	= 0;
	public int		verRhyme	= 0;


	public ValuableWord(String _text, String _pos, String _kasus, int _hyphen, String[] _divisions, String[] _phonetics)
	{
		this.text = _text;
		this.pos = _pos;
		this.count = 1;
		this.hyphen = _hyphen;
		this.divisions = _divisions;
		this.phonetics = _phonetics;
		this.prefix = "";
		this.suffix = "";
		this.kasus = _kasus;
		this.ending = "";
		this.lemma = "";

		this.pers_tag = "";

		this.num = "";
		this.pers = "";
		this.gen = "";
	}


	public ValuableWord()
	{
		String[] test = { "" };
		this.text = "";
		this.pos = "";
		this.count = 1;
		this.hyphen = 1;
		this.divisions = test;
		this.phonetics = test;
		this.prefix = "";
		this.suffix = "";
		this.kasus = "";
		this.ending = "";
		this.lemma = "";

		this.pers_tag = "";

		this.num = "";
		this.pers = "";
		this.gen = "";
	}

	ArrayList<WordProperty>	properties	= new ArrayList<WordProperty>();


	public void addPropertie(WordProperty... properties)
	{
		for (WordProperty property : properties) {
			setProperties(property);
		}
	}


	public void setProperties(WordProperty property)
	{
		properties.add(property);
	}


	public boolean check(ValuableWord vw)
	{
		boolean theFlag = false;
		Boolean[] flags = new Boolean[properties.size()];
		for (int i = 0; i < flags.length; i++) {
			WordProperty property = properties.get(i);
			flags[i] = false;

			String methodName = (String) property.getKey();
			java.lang.reflect.Method method;
			try {
				method = this.getClass().getMethod(methodName, ValuableWord.class, WordProperty.class);
				flags[i] = (boolean) method.invoke(this, vw, property);
				// System.out.println(flags[i]);
			} catch (SecurityException e) {
				// ...
			} catch (NoSuchMethodException e) {
				// ...
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Boolean flag : flags) {
			if (flag == false) {
				return false;
			} else {
				theFlag = true;
			}
		}
		return theFlag;
	}


	public boolean lemmaEquals(ValuableWord vw, WordProperty property)
	{
		String value = (String) property.getValue();
		if (vw.lemma.equalsIgnoreCase(value)) {
			return false;
		} else {
			return true;
		}
	}


	public boolean firstCharLemmaIs(ValuableWord vw, WordProperty property)
	{
		char value = (char) property.getValue();
		if (!vw.lemma.equalsIgnoreCase("")) {
			if (Character.toUpperCase(vw.lemma.charAt(0)) != Character.toUpperCase(value)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}


	public boolean firstCharIs(ValuableWord vw, WordProperty property)
	{
		char value = (char) property.getValue();
		if (Character.toUpperCase(vw.text.charAt(0)) != Character.toUpperCase(value)) {
			return false;
		} else {
			return true;
		}
	}


	public boolean equalLemmaSize(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (vw.lemma.length() != value) {
			return false;
		} else {
			return true;
		}
	}


	public boolean maxHyphen(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (value < vw.hyphen) {
			return false;
		} else {
			return true;
		}
	}


	public boolean minHyphen(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (value > vw.hyphen) {
			return false;
		} else {
			return true;
		}
	}


	public boolean equalHyphen(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (value != vw.hyphen) {
			return false;
		} else {
			return true;
		}
	}


	public boolean hasEnding(ValuableWord vw, WordProperty property)
	{
		String value = (String) property.getValue();

		// System.out.println("COMPARING:" + value + " " + vw.pos);
		if (value.equalsIgnoreCase(vw.ending) == false) {
			return false;
		} else {
			return true;
		}
	}


	public boolean posTag(ValuableWord vw, WordProperty property)
	{
		String value = (String) property.getValue();

		// System.out.println("COMPARING:" + value + " " + vw.pos);
		if (value.equalsIgnoreCase(vw.pos) == false) {
			return false;
		} else {
			return true;
		}
	}


	public boolean subRhyme(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (vw.subRhyme < value) {
			return false;
		} else {
			return true;
		}
	}


	public boolean adjRhyme(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (vw.adjRhyme < value) {
			return false;
		} else {
			return true;
		}
	}


	public boolean verRhyme(ValuableWord vw, WordProperty property)
	{
		int value = (int) property.getValue();
		if (vw.verRhyme < value) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setHasRhyme(int _hasRyhme)
	{
		this.hasRhyme = _hasRyhme;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setPersTag(String _pers_tag)
	{
		this.pers_tag = _pers_tag;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setLemma(String _lemma)
	{
		this.lemma = _lemma;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setNum(String _num)
	{
		this.num = _num;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setPers(String _pers)
	{
		this.pers = _pers;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setGen(String _gen)
	{
		this.gen = _gen;
	}


	/**
	 * Set the word which was directly in front of this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setPrefix(String _prefix)
	{
		this.prefix = _prefix;
	}


	/**
	 * Set the word which was directly after this word, in the analyzed sentence.
	 * 
	 * @example
	 * @param word
	 */

	public void setSuffix(String _suffix)
	{
		this.suffix = _suffix;
	}


	/**
	 * Set the "ending" used for rhyme algorythm
	 * 
	 * @example
	 * @param word
	 */

	public void setEnding(String _ending)
	{
		this.ending = _ending;
	}


	/**
	 * Increments its own count-variable, determining how often it appeared in the analyzed text.
	 * 
	 * @example
	 */

	public void incrementCount()
	{
		this.count++;
	}

}
