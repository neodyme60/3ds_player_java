package graphics.material.texture.io;

public class ioBitmapFile implements ioFormatInterface {
    private String fileUri = "";

    public ioBitmapFile(String _fileUri) {
        fileUri = _fileUri;
    }

    public String getUri() {
        return fileUri;
    }
}
