/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.descriptors.*;
import org.jetbrains.kotlin.descriptors.annotations.AnnotationUtilKt;
import org.jetbrains.kotlin.descriptors.annotations.Annotations;
import org.jetbrains.kotlin.descriptors.annotations.AnnotationsImpl;
import org.jetbrains.kotlin.descriptors.impl.*;
import org.jetbrains.kotlin.name.Name;
import org.jetbrains.kotlin.resolve.scopes.receivers.ExtensionReceiver;
import org.jetbrains.kotlin.types.KotlinType;
import org.jetbrains.kotlin.types.Variance;

import java.util.Collections;

import static org.jetbrains.kotlin.resolve.DescriptorUtils.getDefaultConstructorVisibility;
import static org.jetbrains.kotlin.resolve.descriptorUtil.DescriptorUtilsKt.getBuiltIns;

public class DescriptorFactory {
    private static class DefaultConstructorDescriptor extends ConstructorDescriptorImpl {
        public DefaultConstructorDescriptor(@NotNull ClassDescriptor containingClass, @NotNull SourceElement source) {
            super(containingClass, null, Annotations.Companion.getEMPTY(), true, Kind.DECLARATION, source);
            initialize(Collections.<TypeParameterDescriptor>emptyList(), Collections.<ValueParameterDescriptor>emptyList(),
                       getDefaultConstructorVisibility(containingClass));
        }
    }

    private DescriptorFactory() {
    }

    @NotNull
    public static PropertySetterDescriptorImpl createDefaultSetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations
    ) {
        return createSetter(propertyDescriptor, annotations, true, false, propertyDescriptor.getSource());
    }

    @NotNull
    public static PropertySetterDescriptorImpl createSetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations,
            boolean isDefault,
            boolean isExternal,
            @NotNull SourceElement sourceElement
    ) {
        return createSetter(propertyDescriptor, annotations, isDefault, isExternal, propertyDescriptor.getVisibility(), sourceElement);
    }

    @NotNull
    public static PropertySetterDescriptorImpl createSetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations,
            boolean isDefault,
            boolean isExternal,
            @NotNull Visibility visibility,
            @NotNull SourceElement sourceElement
    ) {
        PropertySetterDescriptorImpl setterDescriptor =
                new PropertySetterDescriptorImpl(propertyDescriptor, annotations, propertyDescriptor.getModality(),
                                                 visibility, !isDefault, isDefault, isExternal,
                                                 CallableMemberDescriptor.Kind.DECLARATION, null, sourceElement);
        setterDescriptor.initializeDefault();
        return setterDescriptor;
    }

    @NotNull
    public static PropertyGetterDescriptorImpl createDefaultGetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations
    ) {
        return createGetter(propertyDescriptor, annotations, true, false);
    }

    @NotNull
    public static PropertyGetterDescriptorImpl createGetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations,
            boolean isDefault,
            boolean isExternal
    ) {
        return createGetter(propertyDescriptor, annotations, isDefault, isExternal, propertyDescriptor.getSource());
    }

    @NotNull
    public static PropertyGetterDescriptorImpl createGetter(
            @NotNull PropertyDescriptor propertyDescriptor,
            @NotNull Annotations annotations,
            boolean isDefault,
            boolean isExternal,
            @NotNull SourceElement sourceElement) {
        return new PropertyGetterDescriptorImpl(propertyDescriptor, annotations, propertyDescriptor.getModality(),
                                                propertyDescriptor.getVisibility(), !isDefault, isDefault, isExternal,
                                                CallableMemberDescriptor.Kind.DECLARATION, null, sourceElement);
    }

    @NotNull
    public static ConstructorDescriptorImpl createPrimaryConstructorForObject(
            @NotNull ClassDescriptor containingClass,
            @NotNull SourceElement source
    ) {
        return new DefaultConstructorDescriptor(containingClass, source);
    }

    @NotNull
    public static SimpleFunctionDescriptor createEnumValuesMethod(@NotNull ClassDescriptor enumClass) {
        AnnotationsImpl annotations = AnnotationsImpl.createWithNoTarget(
                AnnotationUtilKt.createDeprecatedAnnotation(getBuiltIns(enumClass), "Use 'values' property instead", "this.values")
        );

        SimpleFunctionDescriptorImpl values =
                SimpleFunctionDescriptorImpl.create(enumClass, annotations, DescriptorUtils.ENUM_VALUES,
                                                    CallableMemberDescriptor.Kind.SYNTHESIZED, enumClass.getSource());
        return values.initialize(null, null, Collections.<TypeParameterDescriptor>emptyList(),
                                 Collections.<ValueParameterDescriptor>emptyList(),
                                 getBuiltIns(enumClass).getArrayType(Variance.INVARIANT, enumClass.getDefaultType()),
                                 Modality.FINAL, Visibilities.PUBLIC);
    }

    @NotNull
    public static PropertyDescriptor createEnumValuesProperty(@NotNull ClassDescriptor enumClass) {
        PropertyDescriptorImpl values =
                PropertyDescriptorImpl.create(
                        enumClass, Annotations.Companion.getEMPTY(), Modality.FINAL, Visibilities.PUBLIC, /* isVar */ false,
                        DescriptorUtils.ENUM_VALUES, CallableMemberDescriptor.Kind.SYNTHESIZED, enumClass.getSource(),
                        /* lateInit = */ false, /* isConst = */ false
                );

        KotlinType type = getBuiltIns(enumClass).getArrayType(Variance.INVARIANT, enumClass.getDefaultType());

        PropertyGetterDescriptorImpl getter = createDefaultGetter(values, Annotations.Companion.getEMPTY());

        values.initialize(getter, null);
        getter.initialize(type);
        values.setType(type, Collections.<TypeParameterDescriptor>emptyList(), null, (KotlinType) null);

        return values;
    }

    @NotNull
    public static SimpleFunctionDescriptor createEnumValueOfMethod(@NotNull ClassDescriptor enumClass) {
        SimpleFunctionDescriptorImpl valueOf =
                SimpleFunctionDescriptorImpl.create(enumClass, Annotations.Companion.getEMPTY(), DescriptorUtils.ENUM_VALUE_OF,
                                                    CallableMemberDescriptor.Kind.SYNTHESIZED, enumClass.getSource());
        ValueParameterDescriptor parameterDescriptor = new ValueParameterDescriptorImpl(
                valueOf, null, 0, Annotations.Companion.getEMPTY(), Name.identifier("value"), getBuiltIns(enumClass).getStringType(),
                /* declaresDefaultValue = */ false,
                /* isCrossinline = */ false,
                /* isNoinline = */ false,
                null,
                enumClass.getSource()
        );
        return valueOf.initialize(null, null, Collections.<TypeParameterDescriptor>emptyList(),
                                  Collections.singletonList(parameterDescriptor), enumClass.getDefaultType(),
                                  Modality.FINAL, Visibilities.PUBLIC);
    }

    @Nullable
    public static ReceiverParameterDescriptor createExtensionReceiverParameterForCallable(
            @NotNull CallableDescriptor owner,
            @Nullable KotlinType receiverParameterType
    ) {
        return receiverParameterType == null
               ? null
               : new ReceiverParameterDescriptorImpl(owner, new ExtensionReceiver(owner, receiverParameterType));
    }
}
