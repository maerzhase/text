package text;

public class Articulator
{

	public Articulator()
	{

	}


	public String indirect(String _kasus, String _gender)
	{

		switch (_kasus) {
			case "NOM":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "ein";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "eine";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "ein";
				}
				break;
			case "AKK":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "einen";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "eine";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "ein";
				}
				break;
			case "DAT":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "einen";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "einer";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "einem";
				}
				break;
			case "GEN":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "eines";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "einer";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "eines";
				}
				break;
			default:

				break;
		}
		return null;
	}


	public String direct(String _kasus, String _gender, String _pers_tag)
	{
		_pers_tag = _pers_tag.substring(1);

		if (_pers_tag.equalsIgnoreCase("PLU")) {
			_gender = _pers_tag;
		}
		switch (_kasus) {
			case "NOM":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "der";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "die";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "das";
				}
				if (_gender.equalsIgnoreCase("PLU")) {
					return "die";
				}
				break;
			case "AKK":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "den";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "die";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "das";
				}
				if (_gender.equalsIgnoreCase("PLU")) {
					return "die";
				}
				break;
			case "DAT":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "dem";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "der";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "dem";
				}
				if (_gender.equalsIgnoreCase("PLU")) {
					return "den";
				}
				break;
			case "GEN":
				if (_gender.equalsIgnoreCase("MAS")) {
					return "des";
				}
				if (_gender.equalsIgnoreCase("FEM")) {
					return "der";
				}
				if (_gender.equalsIgnoreCase("NEU")) {
					return "des";
				}
				if (_gender.equalsIgnoreCase("PLU")) {
					return "der";
				}
				break;
			default:

				break;
		}
		return null;
	}

}
