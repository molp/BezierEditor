package de.qx.game.omikron.client.bezier.editor;

import com.jgoodies.binding.beans.Model;
import de.qx.game.omikron.math.BezierPath;

/**
 * Date: 06.06.13
 * Time: 06:51
 */
public class BezierEditorModel extends Model {

    public static final String PATH = "path";

    private BezierPath path;

    public BezierEditorModel() {
    }

    public BezierEditorModel(BezierPath path) {
        this.path = path;
    }

    public BezierPath getPath() {
        return path;
    }

    public void setPath(BezierPath path) {
        BezierPath oldValue = this.path;
        this.path = path;
        firePropertyChange(PATH, oldValue, path);
    }
}