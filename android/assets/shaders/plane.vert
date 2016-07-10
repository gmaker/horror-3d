attribute vec3 a_pos;
attribute vec4 a_col;
attribute vec2 a_uv;
attribute vec3 a_normal;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;
uniform float u_sprite;

varying vec4 v_col;
varying vec2 v_uv;
varying vec4 v_pos;
varying float v_intensity;

void main() {

    v_pos = u_viewMatrix * vec4(a_pos, 1.0);
    v_col = a_col;
    v_uv = a_uv;

    vec3 normal = normalize(a_normal);
    v_intensity = 1.0;
    if(normal.y < 0.5){
        if(normal.x > 0.5 || normal.x < -0.5)
            v_intensity *= 0.8;
        if(normal.z > 0.5 || normal.z < -0.5)
            v_intensity *= 0.6;
    }

    gl_Position = u_projectMatrix * u_viewMatrix * vec4(a_pos, 1.0);

}