#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 color;

out vec2 v_texCoord;
out vec3 v_color;

uniform mat4 projectedTransform;

void main()
{
	gl_Position = projectedTransform * vec4(position, 1.0);
	v_texCoord = texCoord;
	v_color = color;
}