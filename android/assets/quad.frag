#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform vec3 u_fogColor;

varying vec4 v_color;
varying vec2 v_texCoords;
varying float v_br;

void main() {
	vec4 col = texture2D(u_texture, v_texCoords);
	if (col.a < 0.9) discard;
	col.rgb = col.rgb * v_br + (1 - v_br) * u_fogColor;
	gl_FragColor = col;
}