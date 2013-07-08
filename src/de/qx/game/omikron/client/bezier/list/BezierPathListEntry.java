package de.qx.game.omikron.client.bezier.list;

import com.jgoodies.binding.beans.Model;
import de.qx.game.omikron.math.BezierPath;

/**
 * Date: 10.06.13
 * Time: 07:21
 */
public class BezierPathListEntry extends Model {

    public final static String IDENTIFIER = "identifier";
    public final static String PATH = "path";

    private BezierPath path;
    private String identifier;

    public BezierPathListEntry(String identifier) {
        this.identifier = identifier;
        this.path = new BezierPath();
        this.path.addCurve();
    }

    public BezierPathListEntry(String identifier, BezierPath bezierPath) {
        this.identifier = identifier;
        this.path = bezierPath;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        String oldValue = this.identifier;
        this.identifier = identifier;
        firePropertyChange(IDENTIFIER, oldValue, identifier);
    }

    public BezierPath getPath() {
        return path;
    }

    public void setPath(BezierPath path) {
        BezierPath oldValue = this.path;
        this.path = path;
        firePropertyChange(PATH, oldValue, path);
    }

    @Override
    public String toString() {
        return identifier;
    }
}
