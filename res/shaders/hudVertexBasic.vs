#version 130

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;

varying vec2 texCoord0;

void main()
{
    gl_Position = vec4(position, 1.0);
    texCoord0 = texCoord;
}