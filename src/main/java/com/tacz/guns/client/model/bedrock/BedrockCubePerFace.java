package com.tacz.guns.client.model.bedrock;

import com.tacz.guns.client.resource.pojo.model.FaceItem;
import com.tacz.guns.client.resource.pojo.model.FaceUVsItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BedrockCubePerFace implements BedrockCube {
    private static final BedrockVertex EMPTY = new BedrockVertex(0, 0, 0, 0.0F, 0.0F);
    private static final BedrockVertex[] EMPTY_VERTEX = new BedrockVertex[]{EMPTY, EMPTY, EMPTY, EMPTY};
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;
    private final BedrockPolygon[] polygons;

    public BedrockCubePerFace(float x, float y, float z, float width, float height, float depth, float delta, float texWidth, float texHeight, FaceUVsItem faces) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + width;
        this.maxY = y + height;
        this.maxZ = z + depth;
        this.polygons = new BedrockPolygon[6];

        float xEnd = x + width;
        float yEnd = y + height;
        float zEnd = z + depth;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        xEnd = xEnd + delta;
        yEnd = yEnd + delta;
        zEnd = zEnd + delta;

        BedrockVertex vertex1 = new BedrockVertex(x, y, z, 0.0F, 0.0F);
        BedrockVertex vertex2 = new BedrockVertex(xEnd, y, z, 0.0F, 8.0F);
        BedrockVertex vertex3 = new BedrockVertex(xEnd, yEnd, z, 8.0F, 8.0F);
        BedrockVertex vertex4 = new BedrockVertex(x, yEnd, z, 8.0F, 0.0F);
        BedrockVertex vertex5 = new BedrockVertex(x, y, zEnd, 0.0F, 0.0F);
        BedrockVertex vertex6 = new BedrockVertex(xEnd, y, zEnd, 0.0F, 8.0F);
        BedrockVertex vertex7 = new BedrockVertex(xEnd, yEnd, zEnd, 8.0F, 8.0F);
        BedrockVertex vertex8 = new BedrockVertex(x, yEnd, zEnd, 8.0F, 0.0F);

        this.polygons[2] = getTexturedQuad(new BedrockVertex[]{vertex6, vertex5, vertex1, vertex2}, texWidth, texHeight, Direction.DOWN, faces);
        this.polygons[3] = getTexturedQuad(new BedrockVertex[]{vertex3, vertex4, vertex8, vertex7}, texWidth, texHeight, Direction.UP, faces);
        this.polygons[1] = getTexturedQuad(new BedrockVertex[]{vertex1, vertex5, vertex8, vertex4}, texWidth, texHeight, Direction.WEST, faces);
        this.polygons[4] = getTexturedQuad(new BedrockVertex[]{vertex2, vertex1, vertex4, vertex3}, texWidth, texHeight, Direction.NORTH, faces);
        this.polygons[0] = getTexturedQuad(new BedrockVertex[]{vertex6, vertex2, vertex3, vertex7}, texWidth, texHeight, Direction.EAST, faces);
        this.polygons[5] = getTexturedQuad(new BedrockVertex[]{vertex5, vertex6, vertex7, vertex8}, texWidth, texHeight, Direction.SOUTH, faces);
    }

    private BedrockPolygon getTexturedQuad(BedrockVertex[] positionsIn, float texWidth, float texHeight, Direction direction, FaceUVsItem faces) {
        FaceItem face = faces.getFace(direction);
        float u1 = face.getUv()[0];
        float v1 = face.getUv()[1];
        float u2 = u1 + face.getUvSize()[0];
        float v2 = v1 + face.getUvSize()[1];
        if (face == FaceItem.EMPTY) {
            return new BedrockPolygon(EMPTY_VERTEX, u1, v1, u2, v2, texWidth, texHeight, false, direction);
        }
        return new BedrockPolygon(positionsIn, u1, v1, u2, v2, texWidth, texHeight, false, direction);
    }


    @Override
    public void compile(MatrixStack.Entry entry, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        for (BedrockPolygon polygon : this.polygons) {
            Vector3f vector3f = new Vector3f(polygon.normal);
            vector3f.mul(matrix3f);
            float nx = vector3f.x();
            float ny = vector3f.y();
            float nz = vector3f.z();

            for (BedrockVertex vertex : polygon.vertices) {
                float x = vertex.pos.x() / 16.0F;
                float y = vertex.pos.y() / 16.0F;
                float z = vertex.pos.z() / 16.0F;
                Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
                vector4f.mul(matrix4f);
                consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, vertex.u, vertex.v, overlay, light, nx, ny, nz);
            }
        }
    }
}
