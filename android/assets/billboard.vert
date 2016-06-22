attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;
uniform vec3 u_fogColor;
uniform vec2 u_waveData;

varying vec4 v_col;
varying vec2 v_uv;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    v_uv = a_uv;
    float sz = 256.0;

    float wave = 1.0;
    if (a_offs.x < 0 || a_offs.y > 0) {
        wave = sin(u_waveData.x + rand(a_pos.xy / 128.0)) * u_waveData.y ;
    }
    vec4 p = u_projectMatrix * (u_viewMatrix * u_modelMatrix * vec4(a_pos, 1.0) + vec4(a_offs.x, a_offs.y, wave + 0.5, 0.0));

    float br = clamp(4.0 / (abs(p.z) / 4.0), 0.0, 1.0);
    v_col = vec4(a_col.rgb * br  + (1.0 - br) * u_fogColor, a_col.a);

    gl_Position = p;

}