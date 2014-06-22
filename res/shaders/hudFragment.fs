#version 330

in vec2 v_texCoord;

out vec4 fragColor;

uniform sampler2D sampler;

void main()
{ 
    fragColor = texture(sampler, v_texCoord.xy);
}