#version 330

in vec2 v_texCoord;
in vec3 v_color;

out vec4 color;

uniform sampler2D sampler;

void main()
{
	vec4 texColor = texture2D(sampler, v_texCoord.xy);
	if(texColor.w == 0.0)
		discard;
	color = vec4(v_color, 1.0) * texColor;
}