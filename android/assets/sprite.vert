attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;
uniform vec3 u_fogColor;

varying vec3 v_col;
varying vec2 v_uv;
varying float v_br;

void main() {
    v_uv = a_uv;
    vec4 p = u_projectMatrix * u_viewMatrix * u_modelMatrix * vec4(a_pos + vec3(a_offs, 0.0), 1.0);

    float br = 5.0 / p.z * 5.0;
    v_col = (a_col.rgb * br  + (1 - br) * u_fogColor);

    gl_Position = p;
}