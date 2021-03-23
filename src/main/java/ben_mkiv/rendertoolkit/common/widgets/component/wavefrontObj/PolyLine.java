package ben_mkiv.rendertoolkit.common.widgets.component.wavefrontObj;

import java.util.List;

public class PolyLine {

    Vertex[] vertexes;

    public PolyLine(Vertex[] vertexes) {
        this.vertexes = vertexes;
    }

    public PolyLine(List<Vertex> vertexes) {
        this(vertexes.toArray(new Vertex[0]));
    }
    public Vertex[] getVertexes() {
        return vertexes;
    }
}
