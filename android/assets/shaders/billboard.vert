attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec2 a_offs;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;

uniform vec2 u_waveData;

varying vec4 v_col;
varying vec2 v_uv;
varying float v_br;
varying vec4 v_pos;

float rand(vec2 v){
    return fract(sin(dot(v, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    float wave = 0.0;
    if (a_offs.y > 0.5) {
        wave = sin(u_waveData.x + (rand(a_pos.xz / 512.0)) * 3.1415) * u_waveData.y*0.3 ;
    }
    vec4 p = u_projectMatrix * (u_viewMatrix * u_modelMatrix * vec4(a_pos, 1.0) + vec4(a_offs.x, a_offs.y, wave + 0.5, 0.0));

    v_pos = p;
    v_col = vec4(a_col.rgb, a_col.a - 1.0 / abs(p.z) * 2.0);
    v_uv = a_uv;

    gl_Position = p;

}