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

package org.jetbrains.kotlin.cfg.pseudocode

import com.intellij.util.SmartFMap
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.renderer.DescriptorRenderer
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeUtils
import org.jetbrains.kotlin.types.checker.KotlinTypeChecker

public interface TypePredicate: (KotlinType) -> Boolean {
    override fun invoke(typeToCheck: KotlinType): Boolean
}

public data class SingleType(val targetType: KotlinType): TypePredicate {
    override fun invoke(typeToCheck: KotlinType): Boolean = KotlinTypeChecker.DEFAULT.equalTypes(typeToCheck, targetType)
    override fun toString(): String = targetType.render()
}

public data class AllSubtypes(val upperBound: KotlinType): TypePredicate {
    override fun invoke(typeToCheck: KotlinType): Boolean = KotlinTypeChecker.DEFAULT.isSubtypeOf(typeToCheck, upperBound)

    override fun toString(): String = "{<: ${upperBound.render()}}"
}

public data class ForAllTypes(val typeSets: List<TypePredicate>): TypePredicate {
    override fun invoke(typeToCheck: KotlinType): Boolean = typeSets.all { it(typeToCheck) }

    override fun toString(): String = "AND{${typeSets.joinToString(", ")}}"
}

public data class ForSomeType(val typeSets: List<TypePredicate>): TypePredicate {
    override fun invoke(typeToCheck: KotlinType): Boolean = typeSets.any { it(typeToCheck) }

    override fun toString(): String = "OR{${typeSets.joinToString(", ")}}"
}

public object AllTypes : TypePredicate {
    override fun invoke(typeToCheck: KotlinType): Boolean = true

    override fun toString(): String = "*"
}

// todo: simplify computed type predicate when possible
public fun and(predicates: Collection<TypePredicate>): TypePredicate =
        when (predicates.size()) {
            0 -> AllTypes
            1 -> predicates.first()
            else -> ForAllTypes(predicates.toList())
        }

public fun or(predicates: Collection<TypePredicate>): TypePredicate? =
        when (predicates.size()) {
            0 -> null
            1 -> predicates.first()
            else -> ForSomeType(predicates.toList())
        }

fun KotlinType.getSubtypesPredicate(): TypePredicate {
    return when {
        KotlinBuiltIns.isAnyOrNullableAny(this) && isMarkedNullable() -> AllTypes
        TypeUtils.canHaveSubtypes(KotlinTypeChecker.DEFAULT, this) -> AllSubtypes(this)
        else -> SingleType(this)
    }
}


private fun KotlinType.render(): String = DescriptorRenderer.SHORT_NAMES_IN_TYPES.renderType(this)

public fun <T> TypePredicate.expectedTypeFor(keys: Iterable<T>): Map<T, TypePredicate> =
        keys.fold(SmartFMap.emptyMap<T, TypePredicate>()) { map, key -> map.plus(key, this) }
