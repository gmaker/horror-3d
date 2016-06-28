attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;

varying vec4 v_col;
varying vec2 v_uv;
varying float v_br;
varying vec4 v_pos;

void main() {
    vec4 p = u_projectMatrix * u_viewMatrix * u_modelMatrix * vec4(a_pos + vec3(a_offs, 0.0), 1.0);
    v_pos = p;
    v_col = a_col;
    v_uv = a_uv;

    gl_Position = p;

}