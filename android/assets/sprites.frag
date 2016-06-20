#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec3 v_col;
varying vec2 v_uv;

void main() {
	vec4 col = texture2D(u_texture, v_uv);
	if (col.a < 0.9) discard;
	gl_FragColor = vec4(v_col, 1.0) * col;
}