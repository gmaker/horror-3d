attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;
uniform vec3 u_fogColor;

varying vec4 v_col;
varying vec2 v_uv;

void main() {
    v_uv = a_uv;
    vec4 p = u_projectMatrix * (u_viewMatrix * u_modelMatrix * vec4(a_pos, 1.0) + vec4(a_offs.x, a_offs.y, 0.5, 0.0));

    float br = 2048.0 / (dot(p.xyz, p.xyz) + 2048.0);
    v_col = vec4(a_col.rgb * br  + (1.0 - br) * u_fogColor, a_col.a);

    gl_Position = p;

}