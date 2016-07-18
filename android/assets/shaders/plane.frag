#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_dithering;
uniform vec4 u_fogColor;
uniform sampler2D u_shadows;
uniform float u_screenWidth;
uniform float u_screenHeight;

varying vec4 v_col;
varying vec2 v_uv;
varying vec4 v_pos;
varying float v_intensity;

void main() {
	vec4 col = texture2D(u_texture, v_uv);
	if (col.a < 0.9) discard;

	vec2 c = gl_FragCoord.xy;
	c.x /= u_screenWidth;
	c.y /= u_screenHeight;
	float light = texture2D(u_shadows, c).a;

    float br = clamp(16.0 / dot(v_pos.xyz, v_pos.xyz) * 16.0, 0.0, 1.0);

	float dither = texture2D(u_dithering, mod(gl_FragCoord.xy / 1.0, 4.0) / 4.0).a * 16.0;
	if (br < dither) {
		br *= 0.86;
	}

	col = vec4(v_col.rgb * col.rgb * br + (1.0 - br) * u_fogColor.rgb, v_col.a);
	col.rgb += light * vec3(0.7, 0.5, 0.2)*0.5;

	gl_FragColor = col;
}