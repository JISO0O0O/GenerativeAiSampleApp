/* 텍스트 요약 뷰모델
 * 사용자가 입력한 텍스트 요약
 * GenerativeModel 객체를 사용하여 텍스트 요약 모델과 통신
 * UI 상태 관리(SummarizeUiState)를 통해 화면에 현재 진행 상황 표시
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.sample.feature.text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SummarizeViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    // _uistate 변수 : MutableStateFlow<SummarizeUiState> 타입으로 UI 상태 저장, SummarizeUiState는 열거형 클래스
    // Initial, Loading, Success, Error 등의 상태를 나타냄
    private val _uiState: MutableStateFlow<SummarizeUiState> =
        MutableStateFlow(SummarizeUiState.Initial)

    //stateflow 타입으로 공개되어 UI에서 최신 UI상태에 접근 가능
    val uiState: StateFlow<SummarizeUiState> =
        _uiState.asStateFlow()

    //사용자가 입력한 텍스트(inputText) 요약
    fun summarize(inputText: String) {
        _uiState.value = SummarizeUiState.Loading //UI 상태를 로딩중으로 변경

        val prompt = "Summarize the following text for me: $inputText" //요약 프로토콜 생성

        //백그라운드 스레드에서 요약 작업 수행
        viewModelScope.launch {
            // Non-streaming
            try {
                val response = generativeModel.generateContent(prompt) //generateContent 함수를 이용하여 요약 모델에게 텍스트를 전달 및 응답 받음(비스트리밍)
                response.text?.let { outputContent ->
                    _uiState.value = SummarizeUiState.Success(outputContent)  //서옹적인 경우 응답에서 요약된 텍스트(outputContent)를 추출하여 UI 상태 성공으로 업데이트
                }
            } catch (e: Exception) {
                _uiState.value = SummarizeUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    //스트리밍 방식으로 텍스트 요약
    fun summarizeStreaming(inputText: String) {
        _uiState.value = SummarizeUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"

        viewModelScope.launch {
            try {
                var outputContent = ""
                generativeModel.generateContentStream(prompt) //generateContentStream 함수를 이용하여 모델의 응답을 스트림 형태로 받음
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = SummarizeUiState.Success(outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = SummarizeUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}
