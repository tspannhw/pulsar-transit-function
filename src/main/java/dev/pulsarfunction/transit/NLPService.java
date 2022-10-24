package dev.pulsarfunction.transit;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.StringJoiner;

public class NLPService {
    private static final Logger log = LoggerFactory.getLogger(NLPService.class);

    // Public Vars
    public static final String CURRENT_DIR = "/Users/tspann/Documents/code/pulsar-transit-function/models/";
//    public static final String CURRENT_FILE =  "/en-ner-person.bin";
    public static final String CURRENT_TOKEN_FILE =  "/en-token.bin";
    public static final String CURRENT_LOCATION_FILE = "/en-ner-location.bin";
//    public static final String CURRENT_DATE_FILE = "/en-ner-date.bin";
//    public static final String CURRENT_ORGANIZATION_FILE = "/en-ner-organization.bin";

    /**
     * getNER
     * @param modelDirectory
     * @param sentence
     * @return String
     */
    public String getNER(String modelDirectory, String sentence) {
        if ( sentence == null ) {
            return null;
        }
        InputStream inputStreamTokenizer = null;
        try {
            inputStreamTokenizer = new
                    FileInputStream(CURRENT_DIR + CURRENT_TOKEN_FILE);
        } catch (FileNotFoundException e) {
           e.printStackTrace();
           return null;
        }
        TokenizerModel tokenModel = null;

        StringJoiner locations = new StringJoiner(", ", "", "");

        try {
            tokenModel = new TokenizerModel(inputStreamTokenizer);

            TokenizerME tokenizer = new TokenizerME(tokenModel);

            String tokens[] = tokenizer.tokenize(sentence);

            InputStream inputStreamNameFinder = new
                    FileInputStream(CURRENT_DIR + CURRENT_LOCATION_FILE );
            TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);

            //Instantiating the NameFinderME class
            NameFinderME nameFinder = new NameFinderME(model);

            //Finding the names in the sentence
            Span nameSpans[] = nameFinder.find(tokens);

            String[] spanns = Span.spansToStrings(nameSpans, tokens);
            for (int i = 0; i < spanns.length; i++) {
                locations.add(spanns[i]);
            }

            nameFinder.clearAdaptiveData();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return locations.toString();
    }

    /**
     * localized testing
     * @param args
     */
    public static void main(String[] args) {
        NLPService nlp = new NLPService();
        System.out.println("Locations:" + nlp.getNER(null, "Tim Spann TRANSCOM, Jersey City in New Jersey, USA: football game on Michie Stadium at (Highlands) Air Force Vs Army on Route 100"));
    }
}