/* 채팅 화면에 표시 되는 메시지 데이터를 관리하는 클래스 ChatUiState를 다룸
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

package com.google.ai.sample.feature.chat

import androidx.compose.runtime.toMutableStateList

class ChatUiState(
    messages: List<ChatMessage> = emptyList() //messages 변수는 List<ChatMessage> 타입으로 채팅 메시지 목록 저장
) {
    private val _messages: MutableList<ChatMessage> = messages.toMutableStateList() //내부적으로는 _messages 변수를 사용하여 변경 가능한 상태로 유지 관리
    val messages: List<ChatMessage> = _messages

    //메시지 추가 함수로 새로운 ChatMessage 객체를 _messages 리스트에 추가
    fun addMessage(msg: ChatMessage) {
        _messages.add(msg)
    }

    //사용자가 입력한 메시지에 대한 처리 결과를 반영하는 역할
    fun replaceLastPendingMessage() { //가장 최근에 추가된 '처리중(ispending = true)'상태의 메시지를 업데이트
        val lastMessage = _messages.lastOrNull() //lastOrNull 함수는 먼저 마지막 메시지를 가져옴
        lastMessage?.let {
            val newMessage = lastMessage.apply { isPending = false  } //마지막 메시지가 존재한다면 '처리완료(ispending = flase)'로 변경된 새로운 메시지 객체를 만듦
            _messages.removeLast() //마지막 메시지를 삭제하고
            _messages.add(newMessage) //새로 만든 메시지 다시 추가
        }
    }
}
