#version 130

varying vec2 texCoord0;

uniform sampler2D sampler;

void main()
{ 
    gl_FragColor = texture(sampler, texCoord0.xy);
}