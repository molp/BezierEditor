package de.qx.game.omikron.client.bezier.file;

import de.qx.game.omikron.client.bezier.list.BezierPathListEntry;
import de.qx.game.omikron.client.bezier.list.BezierPathListModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 07.06.13
 * Time: 16:35
 */
public class BezierFileController {

    private BezierFileModel model;
    private BezierPathListModel pathListModel;

    private BezierPathListReaderWriter exporter;

    public BezierFileController(BezierPathListModel pathListModel) {
        this.model = new BezierFileModel();
        this.pathListModel = pathListModel;

        exporter = new BezierPathListReaderWriter();
    }

    public void save(List<BezierPathListEntry> entries) {
        exporter.exportBezierFile(entries, model.getFile());
    }

    public BezierFileModel getModel() {
        return model;
    }

    public void open(File file) {
        model.setFile(file);

        final ArrayList<BezierPathListEntry> entries = exporter.importBezierFile(file);

        pathListModel.getEntries().clear();
        for(BezierPathListEntry entry : entries) {
            pathListModel.getEntries().add(entry);
        }

    }
}
