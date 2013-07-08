package de.qx.game.omikron.client.bezier.list;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;

import java.util.List;

/**
 * Date: 07.06.13
 * Time: 16:35
 */
public class BezierPathListModel extends Model {

    public final static String TEST = "test";
    public final static String ENTRIES = "entries";

    private ArrayListModel<BezierPathListEntry> entries;
    private boolean test;

    public BezierPathListModel() {
        entries = new ArrayListModel<BezierPathListEntry>();
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        firePropertyChange(TEST, this.test, test);
        this.test = test;
    }

    public List<BezierPathListEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayListModel<BezierPathListEntry> entries) {
        firePropertyChange(ENTRIES, this.entries, entries);
        this.entries = entries;
    }
}
