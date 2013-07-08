package de.qx.game.omikron.client.bezier.file;

import com.jgoodies.binding.beans.Model;

import java.io.File;

/**
 * Date: 16.06.13
 * Time: 11:11
 */
public class BezierFileModel extends Model{

    public final static String FILE = "file";
    public final static String FILENAME = "filename";

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        File oldFile = this.file;
        this.file = file;
        firePropertyChange(FILE, oldFile, file);

        if(file != null) {
            setFilename(file.getName());
        } else {
            setFilename(null);
        }
    }

    public void setFilename(String newFilename) {
        firePropertyChange(FILENAME, null, newFilename);
    }

    public String getFilename() {
        return FILENAME;
    }
}
