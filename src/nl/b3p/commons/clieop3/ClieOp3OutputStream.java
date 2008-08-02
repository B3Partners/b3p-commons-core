/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    private static final String allowed = " .()+&$*:;-/,%?@='\"";

    public static String replaceForbiddenChars(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c >= 'a' && c <= 'z') ||
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
        return replaceForbiddenChars(str);
    }

    public void endBestand() throws IOException {
        if (batchState != null) {
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

        if (batchState != null) {
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

        if (vasteOmschrijvingRegels != null) {
            batchState.vasteOmschrijvingenCount = vasteOmschrijvingRegels.length;
            int max = Math.min(MAX_REGELS, batchState.vasteOmschrijvingenCount);
            for (int i = 0; i < max; i++) {
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
            WoonplaatsBegunstigde woonplaatsBegunstigde) throws IOException {

        if (batchState == null) {
            throw new IllegalStateException("niet in batch");
        }

        if (batchState.postCount == MAX_POSTEN) {
            throw new IllegalStateException("maximaal aantal posten bereikt");
        }

        writeRecord(transactie);

        batchState.totaalBedragCenten = batchState.totaalBedragCenten.add(transactie.getBedragCenten());

        batchState.totaalRekeningnummers = batchState.totaalRekeningnummers.add(transactie.getRekeningnummberBegunstigde());
        batchState.totaalRekeningnummers = batchState.totaalRekeningnummers.add(transactie.getRekeningnummerBetaler());

        if (betalingskenmerk != null) {
            writeRecord(betalingskenmerk);
        }

        if (omschrijvingsRegels != null) {
            int maxOmschrijvingen = MAX_REGELS;
            maxOmschrijvingen = maxOmschrijvingen - batchState.vasteOmschrijvingenCount;
            if (betalingskenmerk != null) {
                maxOmschrijvingen--;
            }
            int max = Math.min(maxOmschrijvingen, omschrijvingsRegels.length);
            for (int i = 0; i < max; i++) {
                writeRecord(omschrijvingsRegels[i]);
            }
        }

        if (naamBegunstigde != null) {
            writeRecord(naamBegunstigde);
        }

        if (woonplaatsBegunstigde != null) {
            writeRecord(woonplaatsBegunstigde);
        }

        batchState.postCount++;
    }

    public BatchTotalen endBatch() throws IOException {

        if (batchState == null) {
            throw new IllegalStateException("niet in batch");
        }

        if (batchState.postCount == 0) {
            throw new IllegalStateException("minimaal een post per batch");
        }

        writeRecord(new BatchEnd(batchState.totaalBedragCenten, batchState.totaalRekeningnummers, batchState.postCount));

        BatchTotalen result = new BatchTotalen(batchState.totaalBedragCenten, batchState.totaalRekeningnummers, batchState.postCount, batchCount);

        batchState = null;

        return result;
    }

    /*
    public static void main(String[] args) throws Exception {
    
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
     */
}
