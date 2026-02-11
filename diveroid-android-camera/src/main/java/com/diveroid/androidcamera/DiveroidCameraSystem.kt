/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diveroid.androidcamera

import android.app.Application
import com.google.jetpackcamera.core.camera.CameraXCameraSystem
import com.google.jetpackcamera.core.camera.lowlight.LowLightBoostAvailabilityChecker
import com.google.jetpackcamera.core.camera.lowlight.LowLightBoostEffectProvider
import com.google.jetpackcamera.core.camera.lowlight.LowLightBoostFeatureKey
import com.google.jetpackcamera.core.camera.postprocess.ImagePostProcessor
import com.google.jetpackcamera.core.camera.postprocess.ImagePostProcessorFeatureKey
import com.google.jetpackcamera.core.common.FilePathGenerator
import com.google.jetpackcamera.settings.SettableConstraintsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Provider

class DiveroidCameraSystem(
    application: Application,
    defaultDispatcher: CoroutineDispatcher,
    iODispatcher: CoroutineDispatcher,
    constraintsRepository: SettableConstraintsRepository,
    filePathGenerator: FilePathGenerator,
) : CameraXCameraSystem(
    application = application,
    defaultDispatcher = defaultDispatcher,
    iODispatcher = iODispatcher,
    constraintsRepository = constraintsRepository,
    filePathGenerator = filePathGenerator,
    availabilityCheckers = emptyMap<LowLightBoostFeatureKey, @JvmSuppressWildcards Provider<LowLightBoostAvailabilityChecker>>(),
    effectProviders = emptyMap<LowLightBoostFeatureKey, @JvmSuppressWildcards Provider<LowLightBoostEffectProvider>>(),
    imagePostProcessors = emptyMap<ImagePostProcessorFeatureKey, @JvmSuppressWildcards Provider<ImagePostProcessor>>()
) {
    fun getCameraZoomFactor() : List<Pair<String, Float>> {
        return CameraZoomInfoUtils.getCameraZoomFactor(cameraProvider.availableCameraInfos)
    }
}