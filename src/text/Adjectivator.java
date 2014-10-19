package text;

public class Adjectivator
{
	public Adjectivator()
	{

	}


	private String singularEnding1(String _word)
	{
		String the_ending = "e";
		String ending = _word.charAt(_word.length() - 1) + "";

		if (ending.equalsIgnoreCase("e")) {
			the_ending = "";
			return the_ending;
		}
		return the_ending;
	}


	private String singularEnding2(String _word)
	{
		String the_ending = "en";
		String ending = _word.charAt(_word.length() - 1) + "";

		if (ending.equalsIgnoreCase("e")) {
			the_ending = "n";
			return the_ending;
		}
		return the_ending;
	}


	public String direct(String _word, String _kasus, String _gender, String _pers_tag)
	{
		_pers_tag = _pers_tag.substring(1);
		if (_pers_tag.equalsIgnoreCase("SIN")) {
			switch (_kasus) {
				case "NOM":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding1(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding1(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding1(_word);
					}
					break;
				case "AKK":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding1(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding1(_word);
					}
					break;
				case "DAT":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding2(_word);
					}
					break;
				case "GEN":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding2(_word);
					}
					break;
				default:
			}
		} else {
			return _word + singularEnding2(_word);
		}
		return _word;
	}


	public String indirect(String _word, String _kasus, String _gender, String _pers_tag)
	{
		_pers_tag = _pers_tag.substring(1);
		if (_pers_tag.equalsIgnoreCase("SIN")) {
			switch (_kasus) {
				case "NOM":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + "er";
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding1(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + "es";
					}
					break;
				case "AKK":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding1(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding1(_word);
					}
					break;
				case "DAT":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + singularEnding2(_word);
					}
					break;
				case "GEN":
					if (_gender.equalsIgnoreCase("MAS")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("FEM")) {
						return _word + singularEnding2(_word);
					}
					if (_gender.equalsIgnoreCase("NEU")) {
						return _word + "es";
					}
					break;
				default:
			}
		} else {
			return _word + singularEnding2(_word);
		}
		return _word;
	}
}
