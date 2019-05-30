package ben_mkiv.rendertoolkit.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RenderUtils {

    static void getModelDimensions(IBakedModel model){

        for(BakedQuad bakedQuad : model.getQuads(null, null, 0)){
            for (VertexFormatElement element : bakedQuad.getFormat().getElements()) {
                if (!element.isPositionElement())
                    continue;

                int[] vertexData = bakedQuad.getVertexData();

            }
        }

    }

}
