package text;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


public class VerbParser2
{

	public VerbParser2()
	{

	}


	public Verb getNewVerb(String _verb)
	{
		Verb new_verb = null;
		org.jsoup.nodes.Document temp_doc;
		String word = _verb;
		String stem = "";
		try {
			temp_doc = Jsoup.connect("http://konjugator.reverso.net/konjugation-deutsch-verb-" + word + ".html").get();

			Elements tabels = temp_doc.getElementsByTag("tbody");
			Elements cagetories = tabels.select("h3");
			Elements headlines = tabels.select("u");
			Elements verbforms = tabels.select("font");
			System.out.println(verbforms.toString());
			if (verbforms != null) {
				for (int i = 0; i < verbforms.size(); i++) {

					String[] forms = verbforms.get(i).text().split(" ");
					switch (i) {
						case 0:
							stem = verbforms.get(i).text();
							// println("Infinitiv Präsens:");
							// println(stem);
							new_verb = new Verb(stem);
							break;
						case 1:
							String _partizitiv_präsens = verbforms.get(i).text().split(" ")[1];
							// println("Partizitiv Präsens: ");
							// println(_partizitiv_präsens);

							VerbForm partizitiv_präsens = new VerbForm("Partizitiv Präsens");
							partizitiv_präsens.addForm("", _partizitiv_präsens);
							new_verb.addVerbform("Partizitiv Präsens", partizitiv_präsens);

							break;
						case 2:
							String _partizitiv_perfect = verbforms.get(i).text().split(" ")[1];
							// println("Partizitiv Perfekt: ");
							// println(_partizitiv_perfect);

							VerbForm partizitiv_perfect = new VerbForm("Partizitiv Perfekt");
							partizitiv_perfect.addForm("", _partizitiv_perfect);
							new_verb.addVerbform("Partizitiv Perfekt", partizitiv_perfect);

							break;
						case 3:
							System.out.println("Indikativ Präsens: ");

							VerbForm indikativ_präsens = new VerbForm("Indikativ Präsens");
							System.out.println("--------------- " + forms.length);

							for (int j = 0; j < forms.length; j++) {
								System.out.println("--------------- " + forms[j]);
								if (forms.length > 2) {
									if (j % 2 == 0) {
										try {
											indikativ_präsens.addForm(forms[j + 1], forms[j + 2]);
											System.out.println(forms[j + 1] + " " + forms[j + 2]);
										} catch (ArrayIndexOutOfBoundsException e) {

										}
									}
								}
							}
							new_verb.addVerbform("Indikativ Präsens", indikativ_präsens);

							break;
						case 4:
							// println("Indikativ Präteritum: ");

							VerbForm indikativ_präteritum = new VerbForm("Indikativ Präteritum");

							for (int j = 0; j < forms.length; j++) {

								if (forms.length > 2) {
									if (j % 2 == 0) {
										try {
											indikativ_präteritum.addForm(forms[j + 1], forms[j + 2]);
											// println(forms[j + 1] + " " + forms[j + 2]);
										} catch (ArrayIndexOutOfBoundsException e) {

										}
									}
								}
							}
							new_verb.addVerbform("Indikativ Präteritum", indikativ_präteritum);

							break;
						case 5:
							// println("Indikativ Futur I: ");

							VerbForm indikativ_futur1 = new VerbForm("Indikativ Futur I");

							for (int j = 2; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										indikativ_futur1.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Indikativ Futur I", indikativ_futur1);

							break;
						case 7:
							System.out.println("Konjunktiv I Präsens: ");

							VerbForm kojunktiv1_präsens = new VerbForm("Konjunktiv I Präsens");

							for (int j = 1; j < forms.length; j += 2) {

								if (forms.length > 2) {
									try {
										kojunktiv1_präsens.addForm(forms[j], forms[j + 1]);

										System.out.println(forms[j] + " " + forms[j + 1]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv I Präsens", kojunktiv1_präsens);

							break;
						case 8:
							// println("Konjunktiv I Futur I: ");
							VerbForm kojunktiv1_futur1 = new VerbForm("Konjunktiv I Futur I");

							for (int j = 2; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										kojunktiv1_futur1.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv I Futur I", kojunktiv1_futur1);

							break;
						case 9:
							// println("Konjunktiv II Präsens: ");
							VerbForm kojunktiv2_präsens = new VerbForm("Konjunktiv II Präsens");

							for (int j = 1; j < forms.length; j += 2) {

								if (forms.length > 2) {
									try {
										kojunktiv2_präsens.addForm(forms[j], forms[j + 1]);
										// println(forms[j] + " " + forms[j + 1]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv II Präsens", kojunktiv2_präsens);

							break;
						case 10:
							// println("Konjunktiv II Futur I: ");
							VerbForm kojunktiv2_futur1 = new VerbForm("Konjunktiv II Futur I");

							for (int j = 2; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										kojunktiv2_futur1.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv II Futur I", kojunktiv2_futur1);

							break;

						case 11:
							// println("Infinitiv Perfekt: ");
							// println(forms[0] + " " + forms[1]);
							VerbForm infitiv_perfekt = new VerbForm("Infinitiv Perfekt");
							infitiv_perfekt.addForm("", forms[0] + " " + forms[1]);
							new_verb.addVerbform("Infinitiv Perfekt", infitiv_perfekt);

							break;

						case 12:
							// println("Indikativ Perfekt: ");
							VerbForm indikativ_perfekt = new VerbForm("Indikativ Perfekt");

							for (int j = 1; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										indikativ_perfekt.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Indikativ Perfekt", indikativ_perfekt);

							break;
						case 13:
							// println("Indikativ Plusquamperfekt: ");
							VerbForm indikativ_plusquamperfekt = new VerbForm("Indikativ Plusquamperfekt");

							for (int j = 1; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										indikativ_plusquamperfekt.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Indikativ Plusquamperfekt", indikativ_plusquamperfekt);

							break;

						case 14:
							System.out.println("Indikativ Futur II: ");
							VerbForm indikativ_futur2 = new VerbForm("Indikativ Futur II");

							for (int j = 2; j < forms.length; j += 4) {

								if (forms.length > 2) {
									try {
										indikativ_futur2.addForm(forms[j], forms[j + 1] + " " + forms[j + 2] + " "
												+ forms[j + 3]);
										System.out.println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2] + " "
												+ forms[j + 3]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Indikativ Futur II", indikativ_futur2);

							break;
						case 15:
							// println("Konjunktiv I Präteritum: ");
							VerbForm konjunktiv1_präteritum = new VerbForm("Konjunktiv I Präteritum");

							for (int j = 1; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										konjunktiv1_präteritum.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);

										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv I Präteritum", konjunktiv1_präteritum);

							break;
						case 16:
							// println("Konjunktiv I Futur II: ");
							VerbForm konjunktiv1_futur2 = new VerbForm("Konjunktiv I Futur II");

							for (int j = 2; j < forms.length; j += 4) {

								if (forms.length > 2) {
									try {
										konjunktiv1_futur2.addForm(forms[j], forms[j + 1] + " " + forms[j + 2] + " "
												+ forms[j + 3]);

										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2] + " " + forms[j +
										// 3]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv I Futur II", konjunktiv1_futur2);

							break;
						case 17:
							// println("Konjunktiv II Präteritum: ");
							VerbForm konjunktiv2_präteritum = new VerbForm("Konjunktiv II Präteritum");

							for (int j = 1; j < forms.length; j += 3) {

								if (forms.length > 2) {
									try {
										konjunktiv2_präteritum.addForm(forms[j], forms[j + 1] + " " + forms[j + 2]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}

							break;
						case 18:
							// println("Konjunktiv II Futur II: ");
							VerbForm konjunktiv2_futur2 = new VerbForm("Konjunktiv II Futur II");

							for (int j = 2; j < forms.length; j += 4) {

								if (forms.length > 2) {
									try {
										konjunktiv2_futur2.addForm(forms[j], forms[j + 1] + " " + forms[j + 2] + " "
												+ forms[j + 3]);
										// println(forms[j] + " " + forms[j + 1] + " " + forms[j + 2] + " " + forms[j +
										// 3]);
									} catch (ArrayIndexOutOfBoundsException e) {

									}
								}
							}
							new_verb.addVerbform("Konjunktiv II Futur II", konjunktiv2_futur2);

							break;
						default:
							// println("WORDLENGTH: " + forms.length);
							break;
					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new_verb;
	}
}
