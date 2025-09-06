package net.thenextlvl.perworlds.generator.adapter;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.generator.Generator;
import org.jspecify.annotations.NullMarked;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.util.Arrays;

@NullMarked
public class GroupSettingsAdapterGenerator extends Generator {
    public GroupSettingsAdapterGenerator() {
        super("net.thenextlvl.perworlds.adapter", "GroupSettingsAdapter");
    }

    @Override
    protected TypeSpec generate() {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        TagAdapter.class,
                        GroupSettings.class
                ))
                .addMethod(getDeserializeMethodSpec())
                .addMethod(getSerializeMethodSpec())
                .addAnnotation(NullMarked.class)
                .build();
    }

    private static MethodSpec getDeserializeMethodSpec() {
        var builder = MethodSpec.methodBuilder("deserialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(GroupSettings.class)
                .addParameter(Tag.class, "tag")
                .addParameter(TagDeserializationContext.class, "context")
                .addStatement("var root = tag.getAsCompound()")
                .addStatement("var settings = new net.thenextlvl.perworlds.group.PaperGroupSettings()");
        Arrays.stream(GroupSettings.class.getDeclaredMethods())
                .map(Method::getName)
                .distinct()
                .forEach(s -> builder.addStatement("root.optional($S).map($T::getAsBoolean).ifPresent(settings::$L)", s, Tag.class, s));
        return builder
                .addStatement("return settings")
                .build();
    }

    private static MethodSpec getSerializeMethodSpec() {
        var builder = MethodSpec.methodBuilder("serialize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(CompoundTag.class)
                .addParameter(GroupSettings.class, "settings")
                .addParameter(TagSerializationContext.class, "context")
                .addStatement("var tag = $T.empty()", CompoundTag.class);
        Arrays.stream(GroupSettings.class.getDeclaredMethods())
                .map(Method::getName)
                .distinct()
                .forEach(s -> builder.addStatement("tag.add($S, settings.$L())", s, s));
        return builder
                .addStatement("return tag")
                .build();
    }
}
