package text;

import java.net.URL;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


public class VerbParser
{
	final static String[]	modi	= { "Infinitiv Präsens", "Partizip Präsens", "Partizip Perfekt",
			"Infinitiv Präsens", "Partizip Präsens", "" };


	public VerbParser()
	{

	}


	private int countChar(String _word, char _char)
	{
		int count = 0;
		int i = 0;
		boolean inrow = true;
		while (i < _word.length() - 1 && inrow) {

			if (_word.charAt(i) == _char) {
				count++;
				i++;
			} else {
				inrow = false;
			}
		}
		return count;

	}


	public Verb getNewVerb(String _verb)
	{
		Verb new_verb = new Verb();
		VerbForm verbform = new VerbForm();
		org.jsoup.nodes.Document temp_doc;
		String word = _verb;
		String stem = "";
		try {

			String url = "http://konjugator.reverso.net/konjugation-deutsch-verb-" + word + ".html";

			temp_doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

			Elements tabels = temp_doc.getElementsByTag("tbody");
			Elements cagetories = tabels.select("h3");
			Elements headlines = tabels.select("u");
			Elements verbforms = tabels.select("tr");

			String stuff = verbforms.html();
			stuff = stuff.replaceAll("< br/>", "<br/>");
			stuff = stuff.replaceAll("<br />", "<br/>");
			stuff = stuff.replaceAll("</h3>", "</h3> <br/>");
			// System.out.println(stuff);

			String cleanHTML = Jsoup.clean(stuff, Whitelist.none().addTags("br").addTags("u").addTags("h3"));
			cleanHTML = cleanHTML.replaceAll("<br />", "#");
			cleanHTML = cleanHTML.replaceAll("<u>", "##");
			cleanHTML = cleanHTML.replaceAll("</u>", "");
			cleanHTML = cleanHTML.replaceAll("<h3>", "###");
			cleanHTML = cleanHTML.replaceAll("</h3>", "");
			cleanHTML = cleanHTML.replaceAll("&nbsp;", "");

			// System.out.println(cleanHTML);
			String modus_head = "";
			String modus_sub = "";
			String modus = modus_head + " " + modus_sub;
			int timecount = 0;
			String[] forms2 = cleanHTML.split(System.getProperty("line.separator"));
			for (int i = 1; i < forms2.length; i++) {
				if (forms2[i].length() > 1) {
					if (forms2[i].charAt(1) == ' ') {
						forms2[i] = forms2[i].replaceAll("\\s*(?=\\s)", "");
					}
					if (forms2[i].length() > 2) {
						if (forms2[i].charAt(1) == ' ') {
							forms2[i] = forms2[i].substring(0);
						}

						int charCount = countChar(forms2[i], '#');

						String line = forms2[i].replaceAll("#", "");
						line = Jsoup.parse(line).text();
						switch (charCount) {
							case 1:
								if (timecount == 1) {
									new_verb.setStem(line);
								} else {
									String splitted_line[] = line.split(" ");
									String person = splitted_line[0];

									String konjugation = "";
									if (splitted_line.length > 1) {
										for (int j = 1; j < splitted_line.length; j++) {
											if (j > 1) {
												konjugation += " " + splitted_line[j];
											} else {
												konjugation += "" + splitted_line[j];
											}
										}
									} else {
										konjugation = person;
										person = "";
									}

									if (verbform.modus.equalsIgnoreCase("Imperativ")
											|| verbform.modus.equalsIgnoreCase("Infinitiv Perfekt")) {
										konjugation = person + "" + konjugation;
									}

									verbform.addForm(person, konjugation);
									// System.out.println("PERSON: " + person);
									// System.out.println("KONJUGATION: " + konjugation);
								}
								// System.out.println(line);
								break;
							case 2:
								modus_sub = line;
								modus = modus_head + " " + modus_sub;
								// System.out.println("---" + modus);
								if (timecount > 1) {
									if (verbform.forms.size() > 0) {
										new_verb.addVerbform(verbform.modus, verbform);

									}
									verbform = new VerbForm();
									verbform.setLemma(stem);
									verbform.setModus(modus);
								}

								break;
							case 3:
								modus_head = line;
								modus_sub = "";
								modus = modus_head + "" + modus_sub;

								// System.out.println("===" + modus);
								timecount++;
								if (timecount > 1) {
									if (verbform.forms.size() > 0) {
										new_verb.addVerbform(verbform.modus, verbform);
									}
								}
								verbform = new VerbForm();
								verbform.setLemma(stem);
								verbform.setModus(modus);
								break;
						}

					}
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(new_verb.lemma + " " + new_verb.verb_forms.size());
		// for (VerbForm vf : new_verb.verb_forms.values()) {
		// System.out.println();
		// System.out.println(vf.modus);
		// System.out.println();
		// for (Entry<String, String> e : vf.forms.entrySet()) {
		// String key = e.getKey();
		// String value = e.getValue();
		// value = Jsoup.parse(value).text();
		// System.out.println(key + " " + value);
		// }
		//
		// }
		return new_verb;
	}
}
