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

package org.jetbrains.kotlin.load.kotlin.reflect

import org.jetbrains.kotlin.descriptors.PackagePartProvider
import org.jetbrains.kotlin.load.kotlin.ModuleMapping
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap

class RuntimePackagePartProvider(val classLoader : ClassLoader) : PackagePartProvider {

    val module2Mapping = ConcurrentHashMap<String, Lazy<ModuleMapping>>()

    fun registerModule(moduleName: String?) {
        if (moduleName == null) return

        module2Mapping.putIfAbsent(moduleName, lazy {
            val resourceAsStream: InputStream = classLoader.getResourceAsStream("META-INF/$moduleName.kotlin_module") ?: return@lazy ModuleMapping()

            try {
                return@lazy ModuleMapping(resourceAsStream.readBytes())
            }
            catch (e: Exception) {
                return@lazy ModuleMapping()
            }
        })
    }


    override fun findPackageParts(packageFqName: String): List<String> {
        return module2Mapping.values().map { it.value.findPackageParts(packageFqName) }.filterNotNull().flatMap { it.parts }.distinct()
    }
}