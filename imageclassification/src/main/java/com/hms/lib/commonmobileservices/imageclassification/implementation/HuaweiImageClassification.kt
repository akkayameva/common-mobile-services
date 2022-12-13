// Copyright 2020. Explore in HMS. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.lib.commonmobileservices.imageclassification.implementation

import android.graphics.Bitmap
import com.hms.lib.commonmobileservices.core.ErrorModel
import com.hms.lib.commonmobileservices.imageclassification.common.ClassificationResult
import com.hms.lib.commonmobileservices.imageclassification.common.ImageLabel
import com.huawei.hms.mlsdk.classification.MLImageClassificationAnalyzer
import com.huawei.hms.mlsdk.common.MLFrame

class HuaweiImageClassification(
    private val analyzer: MLImageClassificationAnalyzer
) : IImageClassification {
    override fun analyseImage(
        bitmap: Bitmap,
        callback: (classificationResult: ClassificationResult<List<ImageLabel>>) -> Unit
    ) {
        val frame = MLFrame.fromBitmap(bitmap)
        analyzer.asyncAnalyseFrame(frame)
            .addOnSuccessListener { classification ->
                callback(
                    ClassificationResult.Success(
                        classification.map {
                            ImageLabel(
                                name = it.name,
                                possibility = it.possibility
                            )
                        }
                    )
                )
            }.addOnFailureListener { e ->
                callback(
                    ClassificationResult.Error(
                        errorMessage = e.localizedMessage,
                        errorModel = ErrorModel(
                            message = e.message,
                            exception = e
                        )
                    )
                )
            }
    }

    override fun stopAnalyzer() {
        analyzer.stop()
    }
}