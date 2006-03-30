/*
 * $Id: ClieOp3OutputStream.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.clieop3;

import java.io.*;
import java.math.BigInteger;

import org.apache.commons.logging.*;

import nl.b3p.commons.clieop3.record.*;

/**
 * Gebaseerd op de ClieOp3 specificatie van Interpay januari 2005 en die van
 * ING Bank.
 *
 * TODO impl incasso's
 */
public class ClieOp3OutputStream {

    private static final Log log = LogFactory.getLog(ClieOp3OutputStream.class);

    /**
     * Maximaal aantal posten per batch
     */
    public static final int MAX_POSTEN = 100000;

    /**
     * Lengte van een record
     */
    public static final int RECORD_LENGTH = 50;

    /**
     * Maximaal aantal regels per post (incl vaste omschrijvingen, omschrijvingen
     * en betalingskenmerk)
     */
    private static final int MAX_REGELS = 4;

    private OutputStreamWriter writer;

    private BatchState batchState = null;

    private class BatchState {
        int vasteOmschrijvingenCount;
        BigInteger totaalBedragCenten;
        BigInteger totaalRekeningnummers;
        int postCount = 0;
    }

    private int batchCount = 0;

    private BigInteger totaalRekeningnummers = new BigInteger("0");

    public ClieOp3OutputStream(OutputStream out, BestandStart bestand) throws IOException {
        this.writer = new OutputStreamWriter(out, "US-ASCII");
        writeRecord(bestand);
    }

    private void writeRecord(Record record) throws IOException {
        writer.write(record.getRecordData(), 0, RECORD_LENGTH);

        /* records moeten worden afgesloten door CR LF */
        writer.write('\r');
        writer.write('\n');
    }

    /* XXX dit vereist de Normalizer class uit de sun.* hierarchie. Echter in
     * Java 1.6 zal deze wel beschikbaar zijn in java.text.Normalizer.
     *
     * Alternatief zou zijn ICU4J.
     */

    private static Class normalizerClass;
    private static boolean normalizerInitialized = false;

    public static String removeAccents(String text) {
        if(!normalizerInitialized) {
            try {
                normalizerClass = Class.forName("sun.text.Normalizer");
                log.debug("using sun.text.Normalizer class for normalization");
            } catch(ClassNotFoundException e) {
                log.warn("class sun.text.Normalizer not found, no normalization used");
            }
            normalizerInitialized = true;
        }
        if(normalizerClass != null) {
            return sun.text.Normalizer.decompose(text, false, 0)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        } else {
            return text;
        }
    }

    private static final String allowed = " .()+&$*:;-/,%?@='\"";

    public static String replaceForbiddenChars(String text) {
        char[] chars = text.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if((c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c >= '0' && c <= '9') ||
               allowed.indexOf(c) != -1) {
                continue;
            }
            chars[i] = '?';
        }
        return new String(chars);
    }

    public static String cleanClieOp3String(String str) {
        return replaceForbiddenChars(removeAccents(str));
    }

    public void endBestand() throws IOException {
        if(batchState != null) {
            endBatch();
        }
        writeRecord(new BestandEnd());
        writer.flush();
    }

    public void startBatch(
        BatchStart batch,
        VasteOmschrijving[] vasteOmschrijvingRegels,
        Opdrachtgever opdrachtgever)
    throws IOException {

        if(batchState != null) {
            endBatch();
        }

        /* TODO check max. aantal batches? */

        /* TODO check transactiegroup? */
        /* TODO check rekeningnummer opdrachtgever? */

        batch.setVolgnummer(++batchCount);
        writeRecord(batch);

        batchState = new BatchState();
        batchState.postCount = 0;
        batchState.totaalBedragCenten = new BigInteger("0");
        batchState.totaalRekeningnummers = new BigInteger("0");

        if(vasteOmschrijvingRegels != null) {
            batchState.vasteOmschrijvingenCount = vasteOmschrijvingRegels.length;
            int max = Math.min(MAX_REGELS, batchState.vasteOmschrijvingenCount);
            for(int i = 0; i < max; i++) {
                writeRecord(vasteOmschrijvingRegels[i]);
            }
        }

        writeRecord(opdrachtgever);
    }

    public void addBetaalPost(
        Transactie transactie,
        Betalingskenmerk betalingskenmerk,
        Omschrijving[] omschrijvingsRegels,
        NaamBegunstigde naamBegunstigde,
        WoonplaatsBegunstigde woonplaatsBegunstigde
        ) throws IOException {

        if(batchState == null) {
            throw new IllegalStateException("niet in batch");
        }

        if(batchState.postCount == MAX_POSTEN) {
            throw new IllegalStateException("maximaal aantal posten bereikt");
        }

        writeRecord(transactie);

        batchState.totaalBedragCenten = batchState.totaalBedragCenten.add(transactie.getBedragCenten());

        batchState.totaalRekeningnummers = batchState.totaalRekeningnummers.add(transactie.getRekeningnummberBegunstigde());
        batchState.totaalRekeningnummers = batchState.totaalRekeningnummers.add(transactie.getRekeningnummerBetaler());

        if(betalingskenmerk != null) {
            writeRecord(betalingskenmerk);
        }

        if(omschrijvingsRegels != null) {
            int maxOmschrijvingen = MAX_REGELS;
            maxOmschrijvingen = maxOmschrijvingen - batchState.vasteOmschrijvingenCount;
            if(betalingskenmerk != null) {
                maxOmschrijvingen--;
            }
            int max = Math.min(maxOmschrijvingen, omschrijvingsRegels.length);
            for(int i = 0; i < max; i++) {
                writeRecord(omschrijvingsRegels[i]);
            }
        }

        if(naamBegunstigde != null) {
            writeRecord(naamBegunstigde);
        }
        
        if(woonplaatsBegunstigde != null) {
            writeRecord(woonplaatsBegunstigde);
        }

        batchState.postCount++;
    }

    public BatchTotalen endBatch() throws IOException {

        if(batchState == null) {
            throw new IllegalStateException("niet in batch");
        }

        if(batchState.postCount == 0) {
            throw new IllegalStateException("minimaal een post per batch");
        }

        writeRecord(new BatchEnd(batchState.totaalBedragCenten, batchState.totaalRekeningnummers, batchState.postCount));

        BatchTotalen result = new BatchTotalen(batchState.totaalBedragCenten, batchState.totaalRekeningnummers, batchState.postCount, batchCount);

        batchState = null;

        return result;
    }

    /* TODO omschrijven naar JUnit tests */
    public static void testRemoveAccents() {
        System.out.println(removeAccents("hoi"));
        System.out.println(removeAccents("hé"));
        System.out.println(removeAccents("O÷éç÷øs ÄörtjeStichting KèlszittingStichting BethaniëD'n MäölenhookStichting Varkensvlees Lekker HèStichting Weert-Haïti\n" +
            "Vélo l'EaulooStichting Ziekentriduüm Tegelen/BelfeldStichting 'D'n Speulbóngerd' SwolgenStg. Vrunj v. Sjötterie St. Urbanus 'Neel'\n" +
            "Ônger De PanneSpaarkas de GaaspièpStg. A.K. van Aand. in Menten-Tubée Beh. B.V.Stg. Christophorus Foundat. l'Viv, Oekraïne\n" +
            "Stichting 't Tienders KräntjeJoekskapel 'Angesóm'Part aux BénéficesStichting TürkiyemStichting Administratiekantoor Özelli\n" +
            "Véloklub 'Sèrum' 1971"
        ));
    }

    public static void testReplaceForbidden() {
        System.out.println(replaceForbiddenChars("`~!@#$%^&*()-=_+[]é{};\"'\\|'/?.>,<"));
    }

    /* TODO omschrijven naar JUnit tests */
    public static void main(String[] args) throws Exception {
        testRemoveAccents();
        testReplaceForbidden();

        ClieOp3OutputStream clieop = new ClieOp3OutputStream(
            System.out,
            new BestandStart(new java.util.Date(), "AVR01", 1, false)
        );
        clieop.startBatch(
            new BatchStart(BatchStart.TRANSACTIEGROEP_BETALINGEN, new BigInteger("0679411372"), "EUR"),
            new VasteOmschrijving[] {new VasteOmschrijving("Dit is de omschrijving Joehoe")},
            new Opdrachtgever(Opdrachtgever.NAW_GEWENST, null, "Ikke", Opdrachtgever.TESTCODE_PRODUCTIE)
        );
        clieop.addBetaalPost(
            new Transactie(
                Transactie.TRANSACTIE_ONZUIVERE_CREDITEURENBETALING,
                new BigInteger("100"),
                new BigInteger("0679411372"),
                new BigInteger("43252")
            ),
            new Betalingskenmerk("hoppa"),
            new Omschrijving[] {
                new Omschrijving("regel 1"),
                new Omschrijving("regel 2")
            },
            new NaamBegunstigde("Piet"),
            null
        );
        clieop.addBetaalPost(
            new Transactie(
                Transactie.TRANSACTIE_ONZUIVERE_CREDITEURENBETALING,
                new BigInteger("12345"),
                new BigInteger("0679411372"),
                new BigInteger("32894")
            ),
            new Betalingskenmerk("BETALINGSKENMERK"),
            new Omschrijving[] {
                new Omschrijving("regel 1"),
                new Omschrijving("regel 2")
            },
            new NaamBegunstigde("Klaas"),
            new WoonplaatsBegunstigde("Verweggistan")
        );

        clieop.endBestand();
    }
}
