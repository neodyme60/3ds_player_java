package graphics.material.material;

import graphics.Color.colorRGBA;
import graphics.material.channel.channelInterface;

public class materialBase implements materialInterface {
    private String name = "no name";

    //ambiant color
    private colorRGBA ambiantColor = new colorRGBA().setWhite();

    //specular color
    private colorRGBA specularColor = new colorRGBA().setWhite();

    //diffuse color
    private colorRGBA diffuseColor = new colorRGBA().setWhite();

    private channelInterface diffuseChannel = null;
    private channelInterface environmentChannel = null;
    private channelInterface specularChannel = null;
    private channelInterface opacityChannel = null;
    private channelInterface bumpChannel = null;

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public colorRGBA getAmbiant() {
        return ambiantColor;
    }

    public void setAmbiant(colorRGBA _ambiantColor) {
        ambiantColor = _ambiantColor;
    }

    public colorRGBA getDiffuse() {
        return diffuseColor;
    }

    public void setDiffuse(colorRGBA _diffuseColor) {
        diffuseColor = _diffuseColor;
    }

    public colorRGBA getSpecular() {
        return specularColor;
    }

    public void setSpecularColor(colorRGBA _specularColor) {
        specularColor = _specularColor;
    }

    public boolean hasBumpChannel() {
        return bumpChannel != null;
    }

    public boolean hasSpecularChannel() {
        return specularChannel != null;
    }

    public boolean hasOpacityChannel() {
        return opacityChannel != null;
    }

    public boolean hasEnvironmentChannel() {
        return environmentChannel != null;
    }

    public boolean hasDiffuseChannel() {
        return diffuseChannel != null;
    }

    public channelInterface getDiffuseChannel() {
        return diffuseChannel;
    }

    public void setDiffuseChannel(channelInterface _diffuseChannel) {
        diffuseChannel = _diffuseChannel;
    }

    public channelInterface getEnvironmentChannel() {
        return environmentChannel;
    }

    public void setEnvironmentChannel(channelInterface _environmentChannel) {
        environmentChannel = _environmentChannel;
    }

    public channelInterface getOpacityChannel() {
        return opacityChannel;
    }

    public void setOpacityChannel(channelInterface _opacityChannel) {
        opacityChannel = _opacityChannel;
    }

    public channelInterface getBumpChannel() {
        return bumpChannel;
    }

    public void setBumpChannel(channelInterface _bumpChannel) {
        bumpChannel = _bumpChannel;
    }

    public channelInterface getSpecularChannel() {
        return specularChannel;
    }

    public void setSpecularChannel(channelInterface specularChannel) {
        this.specularChannel = specularChannel;
    }
}
