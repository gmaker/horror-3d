attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord;

uniform mat4 u_projectionMatrix;
uniform mat4 u_transformMatrix;

varying vec4 v_color;
varying vec2 v_texCoords;
varying float v_br;

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord;
    vec4 pos = u_projectionMatrix * u_transformMatrix * a_position;
    v_br =  clamp(1.0 - pos.z / 128.0, 0.0, 1.0);

    gl_Position = pos;
}