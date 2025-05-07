package engine.tools;

public class TextureResult {
    private int[] texture;
    private boolean isValid;

    public TextureResult(int[] texture, boolean isValid) {
        this.texture = texture;
        this.isValid = isValid;
    }

    public int[] getTexture() {
        return this.texture;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
