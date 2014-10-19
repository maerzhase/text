package text;

import java.util.LinkedHashMap;


public class VerbForm
{
	public String							lemma;
	public String							modus;

	public LinkedHashMap<String, String>	forms;


	public VerbForm()
	{
		this("", "");
	}


	public VerbForm(String _lemma, String _modus)
	{
		this.lemma = _lemma;
		this.modus = _modus;
		this.forms = new LinkedHashMap<String, String>();
	}


	public VerbForm(String _modus)
	{
		this.lemma = "";
		this.modus = _modus;
		this.forms = new LinkedHashMap<String, String>();
	}


	public void setLemma(String _lemma)
	{
		this.lemma = _lemma;
	}


	public void setModus(String _modus)
	{
		this.modus = _modus;
	}


	public void addForm(String _person, String _konjugation)
	{
		this.forms.put(_person, _konjugation);
	}


	public String getVerb(String _person)
	{
		return this.forms.get(_person);
	}
}
