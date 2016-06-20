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

void main() {
    v_uv = a_uv;
    vec4 p = u_projectMatrix * (u_viewMatrix * vec4(a_pos, 1.0) + vec4(a_offs.x, a_offs.y, 0.5, 0.0));

    float br = 5.0 / p.z * 5.0;
    v_col = (a_col.rgb * br  + (1 - br) * u_fogColor);

    gl_Position = p;

}