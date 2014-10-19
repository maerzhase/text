package text;

import java.util.LinkedHashMap;


public class Verb
{
	public String							lemma;
	public LinkedHashMap<String, VerbForm>	verb_forms;


	public Verb()
	{
		this("");
	}


	public Verb(String _lemma)
	{
		this.lemma = _lemma;
		this.verb_forms = new LinkedHashMap<String, VerbForm>();
	}


	public void setStem(String _lemma)
	{
		this.lemma = _lemma;
	}


	public void addVerbform(String _modus, VerbForm _verbform)
	{
		verb_forms.put(_modus, _verbform);
	}


	public VerbForm getVerbform(String _verbform)
	{
		return verb_forms.get(_verbform);
	}

}
