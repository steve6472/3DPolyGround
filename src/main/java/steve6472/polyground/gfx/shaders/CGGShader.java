package steve6472.polyground.gfx.shaders;

import steve6472.sge.gfx.shaders.StaticShader3D;

public class CGGShader extends StaticShader3D
{
    public static Type ALBEDO, NORMAL, EMISSION, EMISSION_POS, EMISSION_TOGGLE;

    public CGGShader() {
        super("g");
    }

    public CGGShader(String path) {
        super(path);
    }

    protected void createUniforms() {
        this.addUniform("albedo", ALBEDO = new Type(EnumUniformType.INT_1));
        this.addUniform("normal", NORMAL = new Type(EnumUniformType.INT_1));
        this.addUniform("emission", EMISSION = new Type(EnumUniformType.INT_1));
        this.addUniform("emissionPos", EMISSION_POS = new Type(EnumUniformType.INT_1));
        this.addUniform("emissionToggle", EMISSION_TOGGLE = new Type(EnumUniformType.FLOAT_1));
    }
}
