attribute vec3 a_pos;

uniform mat4 u_projectMatrix;
uniform mat4 u_viewMatrix;

varying vec4 v_pos;

void main() {
    v_pos = vec4(a_pos, 1.0);
    gl_Position = u_projectMatrix * u_viewMatrix * v_pos;
}
