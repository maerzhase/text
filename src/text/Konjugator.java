package text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Konjugator
{

	private String[]				zeiten		= { "Infinitiv Präsens", "Partizitiv Präsens", "Partizitiv Perfekt",
			"Indikativ Präsens", "Indikativ Präteritum", "Indikativ Futur I", "Imperativ", "Konjunktiv I Präsens",
			"Konjunktiv I Futur I", "Konjunktiv II Präsens", "Konjunktiv II Futur I", "Infinitiv Perfekt",
			"Indikativ Perfekt", "Indikativ Plusquamperfekt", "Infinitiv Futur II", "Konjunktiv I Präteritum",
			"Konjunktiv I Futur II", "Konjunktiv II Präteritum", "Konjunktiv II Futur II" };

	private String[]				time_tags	= { "IFPR", "PAPR", "PAPE", "IDPR", "IDPT", "IDF1", "IMP", "K1PR",
			"K1F1", "K2PR", "K2F1", "IFPE", "IDPE", "IDPQ", "IDF2", "K1PT", "K1F2", "K2PT", "K2F2" };

	private ArrayList<String>		found;

	public HashMap<String, Verb>	dictionrary;

	private String					file_path;

	private VerbParser				vp;

	private LinkedList<String>		dativ;
	private LinkedList<String>		akkusativ;
	private LinkedList<String>		nominativ;

	private LinkedList<String>		blacklist;


	public Konjugator(String _filepath)
	{
		this.dictionrary = new HashMap<String, Verb>();
		this.found = new ArrayList<String>();
		this.dativ = loadLines("data/konjugator/dativ.txt");
		this.akkusativ = loadLines("data/konjugator/akkusativ.txt");
		this.nominativ = loadLines("data/konjugator/nominativ.txt");
		this.blacklist = loadLines("data/konjugator/blacklist.txt");

		vp = new VerbParser();
		setFilePath(_filepath);

		loadDictionary(_filepath);
	}


	public LinkedList<String> loadLines(String _filepath)
	{
		LinkedList<String> lines = null;
		BufferedReader reader = null;

		try {
			lines = new LinkedList<String>();
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(_filepath), "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}


	private ArrayList<Element> getElementsOf(NodeList _nList)
	{
		ArrayList<Element> elements = new ArrayList<Element>();
		for (int i = 0; i < _nList.getLength(); i++) {
			Node n = _nList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				elements.add(e);
			}
		}
		return elements;
	}


	public String getKonjugation(ValuableWord _vw, String _pers, String _modus)
	{

		String lemma = _vw.lemma;
		String konjugated_verb = "VERB NOT FOUND";
		if (dictionrary.containsKey(lemma)) {
			Verb verb = dictionrary.get(lemma);
			// System.out.println("KONJUGATIONSANFRAGE: " + lemma + " " + _pers + " " + _modus + " ");
			VerbForm verbform = verb.getVerbform(_modus);
			konjugated_verb = verbform.getVerb(_pers);
		}
		return konjugated_verb;
	}


	public String fixedCase(String lemma)
	{
		String fixed_case = null;
		if (dativ.contains(lemma)) {
			fixed_case = "DAT";
		} else if (akkusativ.contains(lemma)) {
			fixed_case = "AKK";
		} else if (nominativ.contains(lemma)) {
			fixed_case = "NOM";
		}
		return fixed_case;
	}


	public String getVerb(String _lemma, String _pers_tag, String _modus)
	{
		String lemma = _lemma;
		String konjugated_verb = null;
		String modus = _modus;

		if (dictionrary.containsKey(lemma)) {
			Verb verb = dictionrary.get(lemma);

			VerbForm verbform = verb.getVerbform(modus);
			// System.out.println(verbform.forms + " " + _pers_tag);

			if (_pers_tag.equalsIgnoreCase("3SIN")) {
				konjugated_verb = verbform.getVerb("er/sie/es");
			} else if (_pers_tag.equalsIgnoreCase("1SIN")) {
				konjugated_verb = verbform.getVerb("ich");
			} else if (_pers_tag.equalsIgnoreCase("2SIN")) {
				konjugated_verb = verbform.getVerb("du");
			} else if (_pers_tag.equalsIgnoreCase("1PLU")) {
				konjugated_verb = verbform.getVerb("wir");
			} else if (_pers_tag.equalsIgnoreCase("2PLU")) {
				konjugated_verb = verbform.getVerb("ihr");
			} else if (_pers_tag.equalsIgnoreCase("3PLU")) {
				konjugated_verb = verbform.getVerb("sie/Sie");
			} else {
				konjugated_verb = verbform.getVerb(_pers_tag);
			}
		}
		if (konjugated_verb == null) {
			return _lemma + "?";
		} else {
			return konjugated_verb;
		}
	}


	private void loadDictionary(String _filepath)
	{

		try {
			File file = new File(_filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList1 = doc.getElementsByTagName("verb");
			for (Element v : getElementsOf(nList1)) {
				String lemma = v.getAttribute("lemma");
				Verb new_verb = new Verb(lemma);
				for (Element vf : getElementsOf(v.getElementsByTagName("verbform"))) {
					String modus = vf.getAttribute("modus");
					String mod_tag = vf.getAttribute("mod_tag");
					VerbForm verbform = new VerbForm(modus);
					for (Element f : getElementsOf(vf.getElementsByTagName("form"))) {
						String person = f.getAttribute("person");
						String pers_tag = f.getAttribute("pers_tag");
						String form = f.getTextContent();
						verbform.addForm(person, form);
					}
					new_verb.addVerbform(modus, verbform);
				}
				dictionrary.put(new_verb.lemma, new_verb);

			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void getNewVerb(String _verb, int _theflag)
	{
		// System.out.println(dictionrary.containsKey(_verb) + " " + _verb);
		if (dictionrary.containsKey(_verb) == false && _theflag == 1) {
			if (vp.getNewVerb(_verb) != null) {
				Verb new_verb = vp.getNewVerb(_verb);
				dictionrary.put(new_verb.lemma, new_verb);
				addVerbToDictionary(new_verb);
				// System.out.println("*** Kojugator: Adding '" + _verb + "' to the dictionary");
			}
		} else {
			// System.out.println("*** Kojugator: '" + _verb + "' already exists");
		}
	}


	public void addVerbToDictionary(Verb _verb)
	{

		try {
			File temp_file = new File(file_path);
			Verb new_verb = _verb;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document temp_document = docBuilder.parse(temp_file);
			Element rootElement = temp_document.getDocumentElement();

			Element verb = temp_document.createElement("verb");
			verb.setAttribute("lemma", new_verb.lemma);

			for (VerbForm vf : new_verb.verb_forms.values()) {

				Element verbform = temp_document.createElement("verbform");
				verbform.setAttribute("modus", vf.modus);

				for (Map.Entry<String, String> e : vf.forms.entrySet()) {
					String key = e.getKey();
					String value = e.getValue();
					Element form = temp_document.createElement("form");
					form.setTextContent(value);
					form.setAttribute("person", key);
					verbform.appendChild(form);
				}

				verb.appendChild(verbform);
			}

			rootElement.appendChild(verb);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(temp_document);
			StreamResult result = new StreamResult(temp_file);
			transformer.transform(source, result);

		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}


	/**
	 * Set the filepath and load/create the file
	 * 
	 * @example
	 * @param filepath
	 */

	public void setFilePath(String _filepath)
	{
		this.file_path = _filepath;
		File temp_file = new File(file_path);

		if (!temp_file.exists()) {

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document temp_doc = docBuilder.newDocument();
				Element rootElement = temp_doc.createElement("verbs");
				temp_doc.appendChild(rootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;
				transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(temp_doc);
				StreamResult result = new StreamResult(temp_file);

				transformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
