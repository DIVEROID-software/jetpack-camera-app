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

import android.hardware.camera2.CameraCharacteristics
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraInfo

object CameraZoomInfoUtils {
    fun getCameraZoomFactor(cameraInfos: List<CameraInfo>) : List<Pair<String, Float>> {
        val zoomFactor : MutableList<Pair<String, Float>> = mutableListOf()
        val focalLens : MutableList<Pair<String, Float>> = mutableListOf()
        for (cameraInfo in cameraInfos) {
            for (physicalCameraInfo in cameraInfo.physicalCameraInfos) {
                val camera2CameraInfo = Camera2CameraInfo.from(physicalCameraInfo)
                val physicalCameraId = camera2CameraInfo.cameraId
                val result = getCameraPropertiesJSONObject(camera2CameraInfo)
                result.forEach {
                    focalLens.add(Pair(physicalCameraId ,it))
                }
            }
        }
        val mainLens = focalLens.firstOrNull() { 20.0 < it.second && it.second < 30.0 }
        mainLens?.let {
            focalLens.forEach {
                zoomFactor.add(Pair(it.first, it.second / mainLens.second))
            }
        } ?: run {
            focalLens.forEach {
                zoomFactor.add(Pair(it.first, it.second / 24.0f))
            }
        }

        return zoomFactor.sortedBy { it.second }
    }
}

@OptIn(ExperimentalCamera2Interop::class)
private fun getCameraPropertiesJSONObject(cameraInfo: Camera2CameraInfo): List<Float> {
    val focalLens : MutableList<Float> = mutableListOf()
    cameraInfo.getCameraCharacteristic(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
        ?.let {
            val sensorSize = cameraInfo.getCameraCharacteristic(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
            if (sensorSize != null) {
                val sensorWidth = sensorSize.width
                it.forEach {
                    // The 35mm film width is 36mm
                    val focalLengthIn35mm = (36.0f * it) / sensorWidth
                    Log.d("CameraXCameraSystem", "Available focal lengths:${cameraInfo.cameraId} -- ${it} -- ${focalLengthIn35mm}")
                    focalLens.add(focalLengthIn35mm)
                }
            }
        }
    return focalLens
}