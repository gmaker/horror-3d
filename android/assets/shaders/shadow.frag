#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D u_depthMapDir;
uniform samplerCube u_depthMapCube;
uniform float u_cameraFar;
uniform vec3 u_lightPos;
uniform float u_type;

varying vec4 v_pos;
varying vec4 v_posLightTrans;

void main() {
	float intensity = 0.0;
	vec3 lightDir = v_pos.xyz - u_lightPos;
	float lenToLight = length(lightDir) / u_cameraFar;

	float lenDepthMap = -1.0;

	if(u_type == 1.0) {
		vec3 depth = (v_posLightTrans.xyz / v_posLightTrans.w) * 0.5 + 0.5;
		if (v_posLightTrans.z >= 0.0 && (depth.x >= 0.0) && (depth.x <= 1.0) && (depth.y >= 0.0) && (depth.y <= 1.0)) {
			lenDepthMap = texture2D(u_depthMapDir, depth.xy).a;
		}
	} else if(u_type == 2.0) {
		lenDepthMap = textureCube(u_depthMapCube, lightDir).a;
	}

	if(lenDepthMap < lenToLight - 0.005) {
	} else {
		intensity = 0.5 * (1.0 - lenToLight);
	}

	gl_FragColor = vec4(intensity);
}
