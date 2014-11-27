package graphics.Shader;

import graphics.rendering.renderList.renderListItem;
import graphics.videoBuffer_c;

public interface shaderInterface {
    void renderFace(videoBuffer_c b, renderListItem rl);
}
