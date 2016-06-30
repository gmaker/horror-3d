attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec3 a_normal;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;
uniform float u_block;

varying vec4 v_col;
varying vec2 v_uv;
varying float v_br;
varying vec4 v_pos;

void main() {

    vec4 p = u_projectMatrix * u_viewMatrix * u_modelMatrix * vec4(a_pos, 1.0);

    v_pos = p;
    v_col = a_col;

    float isz = 1.0 / 4.0;
    v_uv = (a_uv  + vec2(mod(u_block, 4.0), int(u_block / 4.0))) * isz;

    gl_Position = p;

}