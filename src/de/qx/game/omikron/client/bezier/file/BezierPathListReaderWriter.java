package de.qx.game.omikron.client.bezier.file;

import de.qx.game.omikron.client.bezier.list.BezierPathListEntry;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierPath;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 07.06.13
 * Time: 16:36
 */
class BezierPathListReaderWriter {

    private final BezierPathExporter bezierPathExporter;

    public BezierPathListReaderWriter() {
        bezierPathExporter = new BezierPathExporter();
    }

    public void exportBezierFile(List<BezierPathListEntry> entries, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            exportBezierFile(entries, bufferedWriter);

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BezierPathListEntry> importBezierFile(File file) {
        ArrayList<BezierPathListEntry> results = null;

        try {
            FileReader fileReader = new FileReader(file);

            final BufferedReader reader = new BufferedReader(fileReader);

            results = importBezierFile(reader);

            reader.close();
            fileReader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public void exportBezierFile(List<BezierPathListEntry> entries, BufferedWriter writer) throws IOException {
        for(BezierPathListEntry entry : entries) {
            writer.write(entry.getIdentifier());
            writer.write(";");

            final String serializedPath = bezierPathExporter.serialize(entry.getPath(), Vector2f.ZERO, 1f);
            writer.write(serializedPath);

            writer.write("\n");
        }
    }

    public ArrayList<BezierPathListEntry> importBezierFile(BufferedReader reader) throws IOException {
        ArrayList<BezierPathListEntry> entries = new ArrayList<BezierPathListEntry>();

        String line;
        while((line = reader.readLine()) != null) {
            String identifier = line.substring(0, line.indexOf(";"));
            final BezierPath bezierPath = bezierPathExporter.deserialize(line.substring(line.indexOf(";")+1), Vector2f.ZERO, 1f);

            entries.add(new BezierPathListEntry(identifier, bezierPath));
        }

        return entries;
    }
}
