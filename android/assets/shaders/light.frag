#ifdef GL_ES
    precision mediump float;
#endif

uniform float u_cameraFar;
uniform vec3 u_lightPos;

varying vec4 v_pos;

void main(){
	gl_FragColor = vec4(length(v_pos.xyz - u_lightPos) / u_cameraFar);
}
