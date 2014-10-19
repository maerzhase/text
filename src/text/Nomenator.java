package text;

import java.io.IOException;


public class Nomenator
{

	Hyphenator	hy;


	public Nomenator()
	{
		hy = new Hyphenator();
		// load the hyphenator rule file
		try {
			hy.loadTable(new java.io.BufferedInputStream(new java.io.FileInputStream("data/hyphenator/dehypha.tex")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private String singularGenitivEnding(String _word)
	{
		String the_ending = "es";
		int hyphen = hy.hyphenate(_word).length();
		if (hyphen > 1) {
			the_ending = "s";
		}
		return the_ending;
	}


	private String pluralDativEnding(String _word)
	{

		String the_ending = "n";
		String ending = _word.charAt(_word.length() - 1) + "";
		if (ending.equalsIgnoreCase("n") || ending.equalsIgnoreCase("s")) {
			the_ending = "";
			return the_ending;
		}

		return the_ending;
	}


	public String declinate(String _word, String _kasus, String _gender, String _pers_tag)
	{
		_pers_tag = _pers_tag.substring(1);

		if (_pers_tag.equalsIgnoreCase("SIN")) {
			switch (_kasus) {
				case "NOM":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word;
					}
					break;
				case "AKK":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word;
					}
					break;
				case "DAT":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word;
					}
					break;
				case "GEN":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularGenitivEnding(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularGenitivEnding(_word);
					}
					break;
				default:
			}
		} else {

			switch (_kasus) {
				case "DAT":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + pluralDativEnding(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + pluralDativEnding(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + pluralDativEnding(_word);
					}
					break;
				default:
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word;
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word;
					}
					break;
			}
		}
		return _word;
	}

}
