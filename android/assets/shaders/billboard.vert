attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;

varying vec4 v_col;
varying vec2 v_uv;
varying vec4 v_pos;

void main() {

    vec4 p = (u_viewMatrix * vec4(a_pos, 1.0) + vec4(a_offs.x, a_offs.y, 0.0, 0.0));

    v_pos = p;
    v_col = a_col;
    v_uv = a_uv;

    gl_Position = u_projectMatrix * p;

}