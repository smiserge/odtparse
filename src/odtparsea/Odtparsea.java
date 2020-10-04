/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odtparsea;

/**
 *
 * @author serge
 */
import java.io.File;
import java.net.URI;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.list.List;

public class Odtparsea {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("ODT Parse - Ant");
        TextDocument outputOdt;
        try {
            outputOdt = TextDocument.newTextDocument();

            // add paragraph
            outputOdt.addParagraph("MET Operas");

            // add empty paragraph
            outputOdt.addParagraph("");

            int ColumnsTotal = 7;
            int RowsTotal = 10;

            // add table
            Table table = outputOdt.addTable(RowsTotal, ColumnsTotal);
            table.getCellByPosition(0, 0).setStringValue("Opera Name");
            table.getCellByPosition(1, 0).setStringValue("Composer");
            table.getCellByPosition(2, 0).setStringValue("Recording Date");
            table.getCellByPosition(3, 0).setStringValue("Artists");
            table.getCellByPosition(4, 0).setStringValue("Conductor");
            table.getCellByPosition(5, 0).setStringValue("MET Nightly Stream Date");
            table.getCellByPosition(6, 0).setStringValue("MET On-Demand Link");
            //Cell cell = table.getCellByPosition(1, 0);
            //cell.setStringValue("Composer");

            //File myFile = new File("C:/tmp/hw.odt");
            fillTableRow(table, 1, "Rusalka-2014.odt");

            outputOdt.save("hw.odt");
        } catch (Exception e) {
            System.err.println("ERROR: unable to create output file.");
        }
    }

    public static void fillTableRow(Table table, int row, String fileName) {
        TextDocument inputOdt;

        String operaTitle = "";
        String operaComposer = "";
        String operaRecDate = "";
        String operaArtists = "";
        String operaConductor = "";
        String operaStreamDate = "";
        String operaMetLink = "";

        try {
            inputOdt = TextDocument.loadDocument(fileName);
            // Opera title
            Paragraph par = inputOdt.getParagraphByIndex(0, false);
            operaTitle = par.getTextContent();
            System.out.println("Title: " + operaTitle);

            // Composer
            par = inputOdt.getParagraphByIndex(1, false);
            operaComposer = par.getTextContent();
            System.out.println("Composer: " + operaComposer);

            // Artists, Conductor & Recording Date
            par = inputOdt.getParagraphByIndex(3, false);

            String starring = par.getTextContent(); // e.g. "Starring Anna Netrebko, Dmitry Hvorostobsly. From February 8, 2014."
            String starDelimiter = "Starring: ";
            int starDelimiterPos = starring.indexOf(starDelimiter);
            if (starDelimiterPos != -1) {
                // chop off the "Starring" tag 
                operaArtists = starring.substring(starDelimiterPos + starDelimiter.length());

                // separte Artists and Date
                String dateDelimiter = ". From ";
                int dateDelimiterPos = operaArtists.indexOf(dateDelimiter);
                if (dateDelimiterPos != -1) {
                    operaRecDate = operaArtists.substring(dateDelimiterPos + dateDelimiter.length(), operaArtists.length() - 1);
                    operaArtists = operaArtists.substring(0, dateDelimiterPos);
                }

                // separate Artists and Conoductot
                String conductedDelimiter = ", conducted by ";
                int conductedDelimiterPos = operaArtists.indexOf(conductedDelimiter);
                if (conductedDelimiterPos != -1) {
                    operaConductor = operaArtists.substring(conductedDelimiterPos + conductedDelimiter.length());
                    operaArtists = operaArtists.substring(0, conductedDelimiterPos);
                }

            }

            System.out.println(operaRecDate);
            System.out.println(operaArtists);

            // MET Nightly Stream Date
            par = inputOdt.getParagraphByIndex(4, false);
            String metDateCandidate = par.getTextContent();
            String streamDateDelimiter = "MET Nightly Stream Date: ";
            int streamDateDelimiterPos = metDateCandidate.indexOf(streamDateDelimiter);
            if (streamDateDelimiterPos != -1) {
                operaStreamDate = metDateCandidate.substring(streamDateDelimiterPos + streamDateDelimiter.length());
            }

            System.out.println("Opera Stream Date: " + operaStreamDate);

            // MET Link
            par = inputOdt.getParagraphByIndex(5, false);
            String metLinkCandidate = par.getTextContent();
            String metLinkDelimiter = "MET On-Demand Link: ";
            int metLinkDelimiterPos = metLinkCandidate.indexOf(metLinkDelimiter);
            if (metLinkDelimiterPos != -1) {
                operaMetLink = metLinkCandidate.substring(metLinkDelimiterPos + metLinkDelimiter.length());
            }
            System.out.println("Opera Met Link: " + operaMetLink);

        } catch (Exception e) {
            System.err.println("ERROR: parsing error for flle: " + fileName);
        }

        table.getCellByPosition(0, row).setStringValue(operaTitle);
        table.getCellByPosition(1, row).setStringValue(operaComposer);
        table.getCellByPosition(2, row).setStringValue(operaRecDate);
        table.getCellByPosition(3, row).setStringValue(operaArtists);
        table.getCellByPosition(4, row).setStringValue(operaConductor);
        table.getCellByPosition(5, row).setStringValue(operaStreamDate);
        table.getCellByPosition(6, row).setStringValue(operaMetLink);

    }

}
